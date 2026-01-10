package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductDetailPage extends BasePage {

    private static final By addToCartButtonBy = By.cssSelector(
            "#product_addtocart_form button[title='Add to Cart']"
    );

    // Color swatches container
    private static final By colorSwatchesBy = By.cssSelector(
            "ul.configurable-swatch-list[id*='configurable_swatch_color'] li"
    );

    // Size swatches container
    private static final By sizeSwatchesBy = By.cssSelector(
            "ul.configurable-swatch-list[id*='configurable_swatch_size'] li"
    );

    public ProductDetailPage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * Select the first available color swatch
     */
    public void selectFirstAvailableColor() {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        List<WebElement> colorSwatches = webDriver.findElements(colorSwatchesBy);

        for (WebElement swatch : colorSwatches) {
            try {
                // Check if swatch is not disabled
                String classes = swatch.getAttribute("class");
                if (!classes.contains("not-available")) {
                    WebElement swatchLabel = swatch.findElement(By.cssSelector("a span.swatch-label"));
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", swatchLabel);

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    js.executeScript("arguments[0].click();", swatchLabel);
                    System.out.println("Selected color swatch");
                    return;
                }
            } catch (Exception e) {
                // Try next swatch
            }
        }
    }

    /**
     * Select the first available size swatch
     */
    public void selectFirstAvailableSize() {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        List<WebElement> sizeSwatches = webDriver.findElements(sizeSwatchesBy);

        for (WebElement swatch : sizeSwatches) {
            try {
                // Check if swatch is not disabled
                String classes = swatch.getAttribute("class");
                if (!classes.contains("not-available")) {
                    WebElement swatchLabel = swatch.findElement(By.cssSelector("a span.swatch-label"));
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", swatchLabel);

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    js.executeScript("arguments[0].click();", swatchLabel);
                    System.out.println("Selected size swatch");
                    return;
                }
            } catch (Exception e) {
                // Try next swatch
            }
        }
    }

    /**
     * Click Add to Cart button
     */
    public void clickAddToCart() {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        WebElement addButton = webDriver.findElement(addToCartButtonBy);

        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", addButton);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        js.executeScript("arguments[0].click();", addButton);
        System.out.println("Clicked Add to Cart");

        // Wait for cart to update
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
