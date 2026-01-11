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

    public AccountPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void fillFirstName(String firstName) {
         var firstNameElement = webDriver.findElement(firstNameBy);
         firstNameElement.sendKeys(firstName);
    }

    public void fillMiddleName(String middleName) {
        var middleNameElement = webDriver.findElement(middleNameBy);
        middleNameElement.sendKeys(middleName);
    }

    public void fillLastName(String lastName) {
        var lastNameElement = webDriver.findElement(lastNameBy);
        lastNameElement.sendKeys(lastName);
    }

    public void fillEmail(String email) {
        var emailElement = webDriver.findElement(emailBy);
        emailElement.sendKeys(email);
    }

    public void fillPassword(String password) {
        var passwordElement = webDriver.findElement(passwordBy);
        passwordElement.sendKeys(password);
    }

    public void fillConfirmPassword(String password) {
        var confirmPasswordElement = webDriver.findElement(confirmPasswordBy);
        confirmPasswordElement.sendKeys(password);
    }

    public void submit() {
        webDriver.findElement(createAccountFormBy).submit();
    }
}
