package miniproject;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage extends BasePage {

    private static final By firstNameBy = By.id("firstname");
    private static final By middleNameBy = By.id("middlename");

    public AccountPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void fillFirstName(String firstName) {
        var firstNameInput = webDriver.findElement(firstNameBy);
        firstNameInput.sendKeys(firstName);
    }

    public void fillMiddleName(String firstName) {
        var firstNameInput = webDriver.findElement(middleNameBy);
        firstNameInput.sendKeys(firstName);
    }

}
