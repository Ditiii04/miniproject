package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By emailBy = By.id("email");
    private static final By passwordBy = By.id("pass");
    private static final By loginButtonBy = By.id("send2");

    private static final long TYPING_DELAY_MS = 75;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void fillEmail(String email) {
        typeSlowly(webDriver.findElement(emailBy), email, TYPING_DELAY_MS);
    }

    public void fillPassword(String password) {
        typeSlowly(webDriver.findElement(passwordBy), password, TYPING_DELAY_MS);
    }

    public void submitLogin() {
        slowClick(loginButtonBy, 200);
    }
}
