package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MenPage extends BasePage {

    // All product <li> items on Men page
    private static final By productContainerBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > ul > li"
    );

    // Black color swatch in Shopping Options
    private static final By blackColorFilterBy = By.cssSelector(
            "#narrow-by-list > dd:nth-child(6) > ol > li:nth-child(1) > a > span.swatch-label > img"
    );

    // “X” to clear filter (current filters block) – not used anymore, but can stay
    private static final By clearFilterBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.col-left.sidebar.col-left-first > div > " +
                    "div.block-content.toggle-content > div.currently > ol > li > a"
    );

    // Price range first option ($0.00 - $99.99)
    private static final By firstPriceFilterBy = By.cssSelector(
            "#narrow-by-list > dd:nth-child(4) > ol > li:nth-child(1) > a"
    );

    // “3 Item(s)” text in pager
    private static final By itemsCountTextBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > div.toolbar > " +
                    "div.pager > div > p"
    );

    public MenPage(WebDriver webDriver) {
        super(webDriver);
    }

    public List<WebElement> getAllProducts() {
        return webDriver.findElements(productContainerBy);
    }

    public WebElement getBlackColorFilter() {
        return webDriver.findElement(blackColorFilterBy);
    }

    public WebElement getClearFilterLink() {
        return webDriver.findElement(clearFilterBy);
    }

    public WebElement getFirstPriceFilter() {
        return webDriver.findElement(firstPriceFilterBy);
    }

    public WebElement getItemsCountText() {
        return webDriver.findElement(itemsCountTextBy);
    }

    /**
     * Black swatch img for a given product
     * li.option-black.is-media.filter-match.selected > a > span > img
     */
    public WebElement getBlackSwatchForProduct(WebElement product) {
        By blackSwatchImgBy = By.cssSelector(
                "div > ul > li.option-black.is-media.filter-match.selected > a > span > img"
        );
        return product.findElement(blackSwatchImgBy);
    }

    /**
     * Price span inside a product
     */
    public WebElement getPriceElement(WebElement product) {
        By priceSpanBy = By.cssSelector("span.price");
        return product.findElement(priceSpanBy);
    }
}
