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

    // Label span inside Account link (for safe open)
    private static final By accountLabelBy =
            By.cssSelector("#header > div > div.skip-links > div > a > span.label");

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

    /// Open Account dropdown safely (handles stale + scroll)
    public void openAccountDropdown() {
        var wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        for (int i = 0; i < 2; i++) {
            try {
                // Always find a FRESH element
                WebElement accountLabel = wait.until(d ->
                        d.findElement(accountLabelBy)
                );

                ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("arguments[0].scrollIntoView({block: 'center'});", accountLabel);

                accountLabel.click();
                return;

            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                if (i == 1) {
                    throw e;
                }
                // loop will retry with a fresh find
            }
        }
    }


    // ===== Women navigation =====
    private static final By womenMenuBy = By.linkText("WOMEN");
    private static final By viewAllWomenBy = By.linkText("View All Women");

    public void openAllWomenPage() {
        var actions = new org.openqa.selenium.interactions.Actions(webDriver);
        var womenMenu = webDriver.findElement(womenMenuBy);

        actions.moveToElement(womenMenu).perform();
        webDriver.findElement(viewAllWomenBy).click();
    }

    // ===== Men navigation =====
    private static final By menMenuBy = By.linkText("MEN");
    private static final By viewAllMenBy = By.linkText("View All Men");

    public void openAllMenPage() {
        var actions = new org.openqa.selenium.interactions.Actions(webDriver);
        var menMenu = webDriver.findElement(menMenuBy);

        actions.moveToElement(menMenu).perform();
        webDriver.findElement(viewAllMenBy).click();
    }

    // ===== Sale navigation =====
    private static final By saleMenuBy = By.linkText("SALE");
    private static final By viewAllSaleBy = By.linkText("View All Sale");

    public void openAllSalePage() {
        var actions = new org.openqa.selenium.interactions.Actions(webDriver);
        var saleMenu = webDriver.findElement(saleMenuBy);

        actions.moveToElement(saleMenu).perform();
        webDriver.findElement(viewAllSaleBy).click();
    }

    // ===== Privacy consent =====

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
        slowClick(accountDropdownBy, 200);
        slowClick(logoutAnchorBy, 300);
    }

    // ===== Utils =====

    public Optional<String> getTitle() {
        return Optional.ofNullable(webDriver.getTitle());
    }

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

    protected void slowClick(By locator, long delayMillis) {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        webDriver.findElement(locator).click();
    }
}
