package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SalePage extends BasePage {

    // All product <li> items on the Sale page
    private static final By productContainerBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > ul > li"
    );

    // Relative to each product <li>
    private static final By priceBoxBy = By.cssSelector("div.price-box");
    private static final By oldPriceBy = By.cssSelector("p.old-price .price");
    private static final By specialPriceBy = By.cssSelector("p.special-price .price");

    public SalePage(WebDriver webDriver) {
        super(webDriver);
    }

    public List<WebElement> getAllProducts() {
        return webDriver.findElements(productContainerBy);
    }

    public WebElement getPriceBox(WebElement product) {
        return product.findElement(priceBoxBy);
    }

    public WebElement getOldPrice(WebElement product) {
        return product.findElement(oldPriceBy);
    }

    public WebElement getSpecialPrice(WebElement product) {
        return product.findElement(specialPriceBy);
    }
}
