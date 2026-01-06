package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class WomenPage extends BasePage {

    // First product container on Women page
    private static final By firstProductBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > ul > " +
                    "li:nth-child(1)"
    );

    // First product name link on Women page
    private static final By firstProductNameLinkBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > ul > " +
                    "li:nth-child(1) > div > h2 > a"
    );

    public WomenPage(WebDriver webDriver) {
        super(webDriver);
    }

    public WebElement getFirstProduct() {
        return webDriver.findElement(firstProductBy);
    }

    public WebElement getFirstProductNameLink() {
        return webDriver.findElement(firstProductNameLinkBy);
    }

    public void hoverOverElement(WebElement element) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(element).perform();
    }
}