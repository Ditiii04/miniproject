package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MyWishlistPage extends BasePage {

    private static final By headingBy = By.cssSelector("div.page-title h1");
    // From wishlist page footer: button with text "Add All to Cart"
    private static final By addAllToCartButtonBy =
            By.cssSelector("button.button.btn-add[title='Add All to Cart']");

    // All wishlist item rows in the table
    private static final By wishlistItemRowsBy =
            By.cssSelector("table#wishlist-table tbody tr");

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

    /**
     * Get all wishlist item rows
     */
    public List<WebElement> getWishlistItems() {
        return webDriver.findElements(wishlistItemRowsBy);
    }

    /**
     * Click "Edit" link for a specific wishlist item to go to product detail page
     * @param index 0-based index of the wishlist item
     */
    public void clickEditForItem(int index) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        List<WebElement> items = getWishlistItems();

        if (index >= items.size()) {
            throw new IllegalArgumentException("Wishlist item index " + index + " out of bounds");
        }

        WebElement item = items.get(index);
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", item);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Find the "Edit" link within this row
        WebElement editLink = item.findElement(By.cssSelector("a.link-edit"));
        js.executeScript("arguments[0].click();", editLink);

        System.out.println("Clicked Edit for wishlist item " + index);

        // Wait for product detail page to load
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get the number of items in the wishlist
     */
    public int getWishlistItemCount() {
        return getWishlistItems().size();
    }
}
