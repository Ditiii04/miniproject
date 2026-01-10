package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    private static final By cartRowsBy =
            By.cssSelector("table#shopping-cart-table tbody tr");

    // Try multiple selectors for grand total
    private static final By grandTotalBy1 =
            By.cssSelector("#shopping-cart-totals-table tfoot tr.last td.a-right span.price");
    private static final By grandTotalBy2 =
            By.cssSelector("#shopping-cart-totals-table tfoot tr td:nth-child(2) span.price");
    private static final By grandTotalBy3 =
            By.cssSelector("#shopping-cart-totals-table tfoot .price");

    // Empty cart message
    private static final By emptyCartMessageBy =
            By.cssSelector(".cart-empty p");

    public CartPage(WebDriver webDriver) {
        super(webDriver);
    }

    private double parsePrice(String txt) {
        String clean = txt.replace("$", "").replace(",", "").trim();
        return Double.parseDouble(clean);
    }

    /**
     * Get all cart rows
     */
    public List<WebElement> getCartRows() {
        return webDriver.findElements(cartRowsBy);
    }

    /**
     * Get subtotals for all cart rows
     */
    public List<Double> getRowSubtotals() {
        List<Double> subtotals = new ArrayList<>();
        List<WebElement> rows = getCartRows();
        for (WebElement row : rows) {
            WebElement priceEl = row.findElement(By.cssSelector("td.product-cart-total span.price"));
            subtotals.add(parsePrice(priceEl.getText()));
        }
        return subtotals;
    }

    /**
     * Get grand total from cart
     */
    public double getGrandTotal() {
        // Try multiple selectors until one works
        By[] selectors = {grandTotalBy1, grandTotalBy2, grandTotalBy3};

        for (By selector : selectors) {
            try {
                WebElement element = webDriver.findElement(selector);
                if (element.isDisplayed()) {
                    String txt = element.getText();
                    System.out.println("Found Grand Total with selector: " + selector + " = " + txt);
                    return parsePrice(txt);
                }
            } catch (Exception e) {
                // Try next selector
                System.out.println("Selector " + selector + " didn't work, trying next...");
            }
        }

        // If none of the specific selectors work, throw error
        throw new RuntimeException("Could not find Grand Total element with any of the predefined selectors");
    }

    /**
     * Set quantity for a specific cart row
     * @param rowIndex 0-based index of the cart row
     * @param qty new quantity
     */
    public void setQuantityForRow(int rowIndex, int qty) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        List<WebElement> rows = getCartRows();

        if (rowIndex >= rows.size()) {
            throw new IllegalArgumentException("Cart row index " + rowIndex + " out of bounds");
        }

        WebElement row = rows.get(rowIndex);
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", row);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement qtyInput = row.findElement(By.cssSelector("input.qty"));
        qtyInput.clear();
        qtyInput.sendKeys(String.valueOf(qty));

        System.out.println("Set quantity to " + qty + " for cart row " + rowIndex);
    }

    /**
     * Click update button for a specific cart row
     * @param rowIndex 0-based index of the cart row
     */
    public void clickUpdateForRow(int rowIndex) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        List<WebElement> rows = getCartRows();

        if (rowIndex >= rows.size()) {
            throw new IllegalArgumentException("Cart row index " + rowIndex + " out of bounds");
        }

        WebElement row = rows.get(rowIndex);
        WebElement updateButton = row.findElement(By.cssSelector("button[title='Update']"));

        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", updateButton);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        js.executeScript("arguments[0].click();", updateButton);

        System.out.println("Clicked Update for cart row " + rowIndex);

        // Wait for cart to update
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get the number of items in cart
     */
    public int getCartItemCount() {
        return getCartRows().size();
    }

    /**
     * Delete a specific cart row
     * @param rowIndex 0-based index of the cart row to delete
     */
    public void deleteCartRow(int rowIndex) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        List<WebElement> rows = getCartRows();

        if (rowIndex >= rows.size()) {
            throw new IllegalArgumentException("Cart row index " + rowIndex + " out of bounds");
        }

        WebElement row = rows.get(rowIndex);
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", row);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Find the delete button - try multiple selectors
        WebElement deleteButton = null;
        String[] deleteSelectors = {
                "a.btn-remove",
                "a.btn-remove2",
                "a[title='Remove item']",
                "td.product-cart-remove a"
        };

        for (String selector : deleteSelectors) {
            try {
                deleteButton = row.findElement(By.cssSelector(selector));
                if (deleteButton != null && deleteButton.isDisplayed()) {
                    System.out.println("Found delete button with selector: " + selector);
                    break;
                }
            } catch (Exception e) {
                // Try next selector
            }
        }

        if (deleteButton == null) {
            throw new RuntimeException("Could not find delete button for cart row " + rowIndex);
        }

        js.executeScript("arguments[0].click();", deleteButton);

        System.out.println("Clicked delete for cart row " + rowIndex);

        // Wait for cart to update after deletion
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Delete the first item in the cart
     */
    public void deleteFirstItem() {
        deleteCartRow(0);
    }

    /**
     * Check if the cart is empty
     */
    public boolean isCartEmpty() {
        try {
            WebElement emptyMessage = webDriver.findElement(emptyCartMessageBy);
            return emptyMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the empty cart message text
     */
    public String getEmptyCartMessage() {
        try {
            WebElement emptyMessage = webDriver.findElement(emptyCartMessageBy);
            if (emptyMessage.isDisplayed()) {
                return emptyMessage.getText();
            }
        } catch (Exception e) {
            // Message not found
        }
        return "";
    }
}
