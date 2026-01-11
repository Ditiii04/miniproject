package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    private static final By emailBy = By.id("email");
    private static final By passwordBy = By.id("pass");
    private static final By loginButtonBy = By.id("send2");

    // your header welcome element (right corner)
    private static final By welcomeMessageBy =
            By.cssSelector("body > div.wrapper > div > div.header-language-background > div > p");

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void fillEmail(String email) {
        var emailElement = webDriver.findElement(emailBy);
        emailElement.sendKeys(email);
    }

    public void fillPassword(String password) {
        var passwordElement = webDriver.findElement(passwordBy);
        passwordElement.sendKeys(password);
    }

    public void submitLogin() {
        // Wait for login button to be clickable
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        var loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginButtonBy));

        // Scroll into view to ensure it's visible
        ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", loginButton);

        // Click using JavaScript to avoid interception issues
        ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("arguments[0].click();", loginButton);
    }

    public String getWelcomeMessageText() {
        try {
            return webDriver.findElement(welcomeMessageBy).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
