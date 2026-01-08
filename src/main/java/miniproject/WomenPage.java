package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class WomenPage extends BasePage {

    // ===== Locators for products and sorting =====

    // All product <li> items on Women page
    private static final By productContainerBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > ul > li"
    );

    // Sort By <select>
    private static final By sortBySelectBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > div.toolbar > " +
                    "div.sorter > div > select"
    );

    // Option "Price" inside Sort By
    private static final By sortByPriceOptionBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > div.toolbar > " +
                    "div.sorter > div > select > option:nth-child(3)"
    );

    // First product container on Women page (used in earlier tests)
    private static final By firstProductBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > ul > li:nth-child(1)"
    );

    // First product name link on Women page (used in earlier hover test)
    private static final By firstProductNameLinkBy = By.cssSelector(
            "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                    "div.col-wrapper > div.col-main > div.category-products > ul > " +
                    "li:nth-child(1) > div > h2 > a"
    );

    public WomenPage(WebDriver webDriver) {
        super(webDriver);
    }

    // ===== Product access =====

    public WebElement getFirstProduct() {
        return webDriver.findElement(firstProductBy);
    }

    public WebElement getFirstProductNameLink() {
        return webDriver.findElement(firstProductNameLinkBy);
    }

    public List<WebElement> getAllProducts() {
        return webDriver.findElements(productContainerBy);
    }

    // ===== Hover helper (used in previous tests) =====

    public void hoverOverElement(WebElement element) {
        org.openqa.selenium.interactions.Actions actions =
                new org.openqa.selenium.interactions.Actions(webDriver);
        actions.moveToElement(element).perform();
    }

    // ===== Sorting helpers =====

    public WebElement getSortBySelect() {
        return webDriver.findElement(sortBySelectBy);
    }

    public WebElement getSortByPriceOption() {
        return webDriver.findElement(sortByPriceOptionBy);
    }

    // For a given product <li>, get its price-box div
    public WebElement getPriceBox(WebElement product) {
        return product.findElement(By.cssSelector("div.price-box"));
    }

    /**
     * Effective price for a price-box:
     * - special price if present (old + special case)
     * - otherwise regular price
     */
    public double getEffectivePriceFromPriceBox(WebElement priceBox) {
        List<WebElement> special = priceBox.findElements(By.cssSelector("p.special-price span.price"));
        String priceText;
        if (!special.isEmpty()) {
            priceText = special.get(0).getText();
        } else {
            WebElement regular = priceBox.findElement(By.cssSelector("span.regular-price span.price"));
            priceText = regular.getText();
        }
        String numeric = priceText.replace("$", "").replace(",", "").trim();
        return Double.parseDouble(numeric);
    }

    // Collect all effective prices from page into a list
    public List<Double> getAllEffectivePrices() {
        List<Double> prices = new ArrayList<>();
        for (WebElement product : getAllProducts()) {
            WebElement priceBox = getPriceBox(product);
            prices.add(getEffectivePriceFromPriceBox(priceBox));
        }
        return prices;
    }

    // ===== Wishlist links for first and second product =====
    // These are exactly the selectors you specified for Test 6 step 4.

    public WebElement getFirstProductWishlistLink() {
        return webDriver.findElement(By.cssSelector(
                "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                        "div.col-wrapper > div.col-main > div.category-products > ul > " +
                        "li:nth-child(1) > div > div.actions > ul > li:nth-child(1) > a"
        ));
    }

    public WebElement getSecondProductWishlistLink() {
        return webDriver.findElement(By.cssSelector(
                "body > div.wrapper > div > div.main-container.col3-layout > div > " +
                        "div.col-wrapper > div.col-main > div.category-products > ul > " +
                        "li:nth-child(2) > div > div.actions > ul > li:nth-child(1) > a"
        ));
    }
}
