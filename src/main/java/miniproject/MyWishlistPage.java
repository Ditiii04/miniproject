package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MyWishlistPage extends BasePage {

    private static final By headingBy = By.cssSelector("div.page-title h1");

    public MyWishlistPage(WebDriver webDriver) {
        super(webDriver);
    }

    public WebElement getHeading() {
        return webDriver.findElement(headingBy);
    }

    public String getHeadingText() {
        return getHeading().getText();
    }
}
