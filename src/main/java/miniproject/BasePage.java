package miniproject;

import org.apache.commons.lang3.Validate;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public abstract class BasePage {

    private static final By privacyPromptBy = By.className("privacy_prompt");
    private static final By privacyPromptOptInBy = By.id("privacy_pref_optin");
    private static final By privacyPromptSubmitBy = By.id("consent_prompt_submit");

    private static final By accountDropdownBy = By.cssSelector("#header > div > div.skip-links > div > a");
    private static final By accountDropdownRegisterAnchorBy =  By.cssSelector("#header-account > div > ul > li:nth-child(5) > a");

    protected final WebDriver webDriver;

    public BasePage(WebDriver webDriver) {
        Validate.notNull(webDriver, "webDriver is required");

        this.webDriver = webDriver;
    }

    public void openRegisterPage() {
        var accountDropdown = webDriver.findElement(accountDropdownBy);
        accountDropdown.click();

        var registerAnchor = webDriver.findElement(accountDropdownRegisterAnchorBy);
        registerAnchor.click();

        new WebDriverWait(webDriver, Duration.ofSeconds(5))
                .until(_ -> Objects.equals(webDriver.getTitle(), PageType.CREATE_ACCOUNT.getTitle()));
    }

    /**
     *
     * @return true if the privacy dialog is displayed and the consent was submitted, or false if there was no
     * privacy dialog
     */
    public boolean tryAcceptConsent() {
        var privacyPrompt = webDriver.findElement(privacyPromptBy);
        var wait = new WebDriverWait(webDriver, Duration.ofSeconds(3));

        try {
            wait.until(_ -> privacyPrompt.isDisplayed());
            var optInRadioButton = webDriver.findElement(privacyPromptOptInBy);
            optInRadioButton.click();

            var submitButton = webDriver.findElement(privacyPromptSubmitBy);
            submitButton.click();

            var waitConsentApproved = new WebDriverWait(webDriver, Duration.ofSeconds(3));

            try {
                waitConsentApproved.until(_ -> !privacyPrompt.isDisplayed());
                return true;
            } catch (TimeoutException e) {
                throw new RuntimeException("Clicked submit opt-in, but the prompt dialog did not close.", e);
            }
        } catch (TimeoutException _) {
            return false;
        }
    }

   public Optional<String> getTitle() {
        var title = webDriver.getTitle();

        return Optional.ofNullable(title);
    }

}
