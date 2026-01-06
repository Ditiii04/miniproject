package miniproject;

import org.apache.commons.lang3.Validate;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public abstract class BasePage {

    // ===== Privacy / Consent =====
    private static final By privacyPromptBy = By.className("privacy_prompt");
    private static final By privacyPromptOptInBy = By.id("privacy_pref_optin");
    private static final By privacyPromptSubmitBy = By.id("consent_prompt_submit");

    // ===== Account dropdown =====
    private static final By accountDropdownBy =
            By.cssSelector("#header > div > div.skip-links > div > a");

    private static final By accountDropdownRegisterAnchorBy =
            By.cssSelector("#header-account > div > ul > li:nth-child(5) > a");

    private static final By logoutAnchorBy = By.linkText("Log Out");

    // ===== Messages =====
    private static final By successMessageBy =
            By.cssSelector("li.success-msg span");

    protected final WebDriver webDriver;

    public BasePage(WebDriver webDriver) {
        Validate.notNull(webDriver, "webDriver is required");
        this.webDriver = webDriver;
    }

    // ===== Navigation =====

    public void openRegisterPage() {
        webDriver.findElement(accountDropdownBy).click();
        webDriver.findElement(accountDropdownRegisterAnchorBy).click();

        new WebDriverWait(webDriver, Duration.ofSeconds(5))
                .until(_ ->
                        Objects.equals(
                                webDriver.getTitle(),
                                PageType.CREATE_ACCOUNT.getTitle()
                        )
                );
    }

    public void openLoginPage() {
        webDriver.findElement(accountDropdownBy).click();
        webDriver.findElement(org.openqa.selenium.By.linkText("Log In")).click();
    }

    // ===== Women navigation =====
    private static final By womenMenuBy = By.linkText("WOMEN");              // adjust to actual text/case
    private static final By viewAllWomenBy = By.linkText("View All Women");  // submenu option

    public void openAllWomenPage() {
        var actions = new org.openqa.selenium.interactions.Actions(webDriver);
        var womenMenu = webDriver.findElement(womenMenuBy);

        actions.moveToElement(womenMenu).perform();
        webDriver.findElement(viewAllWomenBy).click();
    }


    // ===== Privacy consent =====

    /**
     * @return true if the privacy dialog was displayed and accepted,
     * false if the dialog was not present
     */
    public boolean tryAcceptConsent() {
        var wait = new WebDriverWait(webDriver, Duration.ofSeconds(3));

        try {
            var privacyPrompt = webDriver.findElement(privacyPromptBy);
            wait.until(_ -> privacyPrompt.isDisplayed());

            webDriver.findElement(privacyPromptOptInBy).click();
            webDriver.findElement(privacyPromptSubmitBy).click();

            new WebDriverWait(webDriver, Duration.ofSeconds(3))
                    .until(_ -> !privacyPrompt.isDisplayed());

            return true;
        } catch (TimeoutException | org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    // ===== Success message =====

    public boolean isSuccessMessageDisplayed() {
        try {
            return webDriver.findElement(successMessageBy).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ===== Logout (slowed down) =====

    public void logout() {
        // small pause between opening dropdown and clicking logout
        slowClick(accountDropdownBy, 200);
        slowClick(logoutAnchorBy, 300);
    }

    // ===== Utils =====

    public Optional<String> getTitle() {
        return Optional.ofNullable(webDriver.getTitle());
    }

    /**
     * Type text in a more "human" way (small delay between characters).
     */
    protected void typeSlowly(WebElement element, String text, long delayMillis) {
        element.clear();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Perform a click with a small delay before the click to make it look slower.
     */
    protected void slowClick(By locator, long delayMillis) {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        webDriver.findElement(locator).click();
    }
}
