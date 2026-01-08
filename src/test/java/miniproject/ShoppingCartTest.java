package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ShoppingCartTest extends BaseUiTest {

    @Test
    void testWomenSortingWishlistAndCartTotals() {
        try {
            System.out.println("Starting test: testWomenSortingWishlistAndCartTotals()");

            // ===== Precondition: run full Test 6 flow on this driver =====
            WomenFlowHelper.runWomenSortingAndWishlistFlow(webDriver);

            // At this point:
            // - user is logged in
            // - 2 items are in wishlist
            // - Account dropdown is open (from step 5 of Test 6)

            // ===== 1. Go to My Wishlist =====
            webDriver.findElement(By.cssSelector("#header-account > div > ul > li:nth-child(2) > a"))
                    .click();

// ===== 2. Add the products to the shopping cart (Select color and size) =====

// First wishlist product: use actual row id 3276
            webDriver.findElement(By.cssSelector(
                    "#item_3276 td.wishlist-cell4.customer-wishlist-item-cart p > a.link-edit"
            )).click();

// choose color + size  (adjust swatch ids if needed)
            webDriver.findElement(By.cssSelector("#swatch20 > span.swatch-label")).click();
            webDriver.findElement(By.cssSelector("#swatch79 > span.swatch-label")).click();

// add to cart
            webDriver.findElement(By.cssSelector(
                    "#product_addtocart_form > div.product-shop > div.product-options-bottom > " +
                            "div.add-to-cart > div.add-to-cart-buttons > button"
            )).click();

// Back to wishlist via Account dropdown
            HomePage homePage = new HomePage(webDriver);
            homePage.openAccountDropdown();
            webDriver.findElement(By.cssSelector("#header-account > div > ul > li:nth-child(2) > a"))
                    .click();

// Second wishlist product: row id 3277
            webDriver.findElement(By.cssSelector(
                    "#item_3277 td.wishlist-cell4.customer-wishlist-item-cart p > a.link-edit"
            )).click();

            webDriver.findElement(By.cssSelector("#swatch22 > span.swatch-label")).click();
            webDriver.findElement(By.cssSelector("#swatch79 > span.swatch-label")).click();

            webDriver.findElement(By.cssSelector(
                    "#product_addtocart_form > div.product-shop > div.product-options-bottom > " +
                            "div.add-to-cart > div.add-to-cart-buttons > button"
            )).click();

            // ===== 3. Open the Shopping Cart, change quantity to 2 for one product and click Update =====

            // If you want to follow your steps 100%, click directly in the cart table:
            WebElement qtyInput = webDriver.findElement(By.cssSelector(
                    "#shopping-cart-table > tbody > tr.first.odd > td.product-cart-actions > input"
            ));
            qtyInput.clear();
            qtyInput.sendKeys("2");

            WebElement updateButton = webDriver.findElement(By.cssSelector(
                    "#shopping-cart-table > tbody > tr.first.odd > td.product-cart-actions > button"
            ));
            updateButton.click();

            // ===== 4. Verify that the prices sum for all items is equal to Grand Total price =====

            // First row subtotal
            String firstSubtotalText = webDriver.findElement(By.cssSelector(
                    "#shopping-cart-table > tbody > tr.first.odd > td.product-cart-total > span"
            )).getText();

            // Second row subtotal
            String secondSubtotalText = webDriver.findElement(By.cssSelector(
                    "#shopping-cart-table > tbody > tr.last.even > td.product-cart-total > span"
            )).getText();

            // Grand total
            String grandTotalText = webDriver.findElement(By.cssSelector(
                    "#shopping-cart-totals-table > tfoot > tr > td:nth-child(2)"
            )).getText();

            // Helper to parse "$150.00" -> 150.00
            double firstSubtotal = parsePrice(firstSubtotalText);
            double secondSubtotal = parsePrice(secondSubtotalText);
            double grandTotal = parsePrice(grandTotalText);

            double sum = firstSubtotal + secondSubtotal;

            System.out.println("First subtotal: " + firstSubtotalText + " -> " + firstSubtotal);
            System.out.println("Second subtotal: " + secondSubtotalText + " -> " + secondSubtotal);
            System.out.println("Sum of subtotals: " + sum);
            System.out.println("Grand total: " + grandTotalText + " -> " + grandTotal);

            Assertions.assertEquals(
                    sum,
                    grandTotal,
                    0.01,
                    "Sum of all item subtotals should equal Grand Total"
            );

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }

    private double parsePrice(String txt) {
        String numeric = txt.replace("$", "").replace(",", "").trim();
        return Double.parseDouble(numeric);
    }
}
