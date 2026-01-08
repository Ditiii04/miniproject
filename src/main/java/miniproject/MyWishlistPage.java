package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MyWishlistPage extends BasePage {

    private static final By headingBy = By.cssSelector("div.page-title h1");
    // From wishlist page footer: button with text "Add All to Cart"
    private static final By addAllToCartButtonBy =
            By.cssSelector("button.button.btn-add[title='Add All to Cart']");

    public MyWishlistPage(WebDriver webDriver) {
        super(webDriver);
    }

    public WebElement getHeading() {
        return webDriver.findElement(headingBy);
    }

    public String getHeadingText() {
        return getHeading().getText();
    }

    public void clickAddAllToCart() {
        // small wait so wishlist table is ready
        slowClick(addAllToCartButtonBy, 500);
    }
}
