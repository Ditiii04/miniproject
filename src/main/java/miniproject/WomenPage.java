package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class WomenPage extends BasePage {

    // Anchor wrapping the image with id 'product-collection-image-428'
    private static final By productAnchorBy =
            By.xpath("//a[contains(@class,'product-image')][img[@id='product-collection-image-428']]");

    public WomenPage(WebDriver webDriver) {
        super(webDriver);
    }

    public WebElement getProductAnchor() {
        return webDriver.findElement(productAnchorBy);
    }

    public void hoverOverElement(WebElement element) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(element).perform();
    }
}
