package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage extends BasePage {

    private static final By firstNameBy = By.id("firstname");
    private static final By middleNameBy = By.id("middlename");
    private static final By lastNameBy = By.id("lastname");
    private static final By emailBy = By.id("email_address");
    private static final By passwordBy = By.id("password");
    private static final By confirmPasswordBy = By.id("confirmation");
    private static final By createAccountFormBy = By.id("form-validate");

    // per‑character delay (in ms) to "slow down" typing
    private static final long TYPING_DELAY_MS = 75;

    public AccountPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void fillFirstName(String firstName) {
        typeSlowly(webDriver.findElement(firstNameBy), firstName, TYPING_DELAY_MS);
    }

    public void fillMiddleName(String middleName) {
        typeSlowly(webDriver.findElement(middleNameBy), middleName, TYPING_DELAY_MS);
    }

    public void fillLastName(String lastName) {
        typeSlowly(webDriver.findElement(lastNameBy), lastName, TYPING_DELAY_MS);
    }

    public void fillEmail(String email) {
        typeSlowly(webDriver.findElement(emailBy), email, TYPING_DELAY_MS);
    }

    public void fillPassword(String password) {
        typeSlowly(webDriver.findElement(passwordBy), password, TYPING_DELAY_MS);
    }

    public void fillConfirmPassword(String password) {
        typeSlowly(webDriver.findElement(confirmPasswordBy), password, TYPING_DELAY_MS);
    }

    public void submit() {
        webDriver.findElement(createAccountFormBy).submit();
    }
}
