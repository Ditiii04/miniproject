package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;

public class ShoppingCartTest extends BaseUiTest {

    @Test
    void testWomenSortingWishlistAndCartTotals() {
        try {
            System.out.println("=== Starting Test 7: Shopping Cart Test ===");

            // ===== Precondition: run full Test 6 flow =====
            System.out.println("Running Test 6 as precondition...");
            WomenFlowHelper.runWomenSortingAndWishlistFlow(webDriver);
            System.out.println("✓ Test 6 precondition completed");

            // At this point:
            // - user is logged in
            // - 2 items are in wishlist
            // - Account dropdown is open

            // ===== Step 1: Go to My Wishlist =====
            System.out.println("Step 1: Going to My Wishlist");
            webDriver.findElement(By.cssSelector("#header-account > div > ul > li:nth-child(2) > a"))
                    .click();

            // Wait for wishlist page to load
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            MyWishlistPage wishlistPage = new MyWishlistPage(webDriver);
            int wishlistCount = wishlistPage.getWishlistItemCount();
            System.out.println("Wishlist has " + wishlistCount + " items");
            Assertions.assertEquals(2, wishlistCount, "Wishlist should have 2 items from Test 6");

            // ===== Step 2: Add products to shopping cart (Select color and size) =====
            System.out.println("Step 2: Adding wishlist products to cart with color and size");

            // Add FIRST product to cart
            System.out.println("Adding first product to cart...");
            wishlistPage.clickEditForItem(0);

            ProductDetailPage productPage = new ProductDetailPage(webDriver);
            productPage.selectFirstAvailableColor();
            productPage.selectFirstAvailableSize();
            productPage.clickAddToCart();

            System.out.println("✓ First product added to cart");

            // Navigate back to wishlist
            System.out.println("Navigating back to wishlist...");
            HomePage homePage = new HomePage(webDriver);
            homePage.openAccountDropdown();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            webDriver.findElement(By.cssSelector("#header-account > div > ul > li:nth-child(2) > a"))
                    .click();

            // Wait for wishlist page to load
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            wishlistPage = new MyWishlistPage(webDriver);

            // Add SECOND product to cart
            System.out.println("Adding second product to cart...");
            wishlistPage.clickEditForItem(0); // First item (since we might have removed the first)

            productPage = new ProductDetailPage(webDriver);
            productPage.selectFirstAvailableColor();
            productPage.selectFirstAvailableSize();
            productPage.clickAddToCart();

            System.out.println("✓ Second product added to cart");
            System.out.println("✓ Both products added to cart with color and size selected");

            // ===== Step 3: Open Shopping Cart, change quantity to 2, and click Update =====
            System.out.println("Step 3: Changing quantity to 2 for first product");

            // We should already be in cart after adding items, but let's create page object
            CartPage cartPage = new CartPage(webDriver);

            // Wait for cart page to be ready
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            int cartCount = cartPage.getCartItemCount();
            System.out.println("Cart has " + cartCount + " items");
            Assertions.assertEquals(2, cartCount, "Cart should have 2 items");

            // Change quantity of first item to 2
            cartPage.setQuantityForRow(0, 2);
            cartPage.clickUpdateForRow(0);

            System.out.println("✓ Updated quantity to 2 for first product");

            // Refresh page object after update
            cartPage = new CartPage(webDriver);

            // ===== Step 4: Verify prices sum equals Grand Total =====
            System.out.println("Step 4: Verifying that sum of subtotals equals Grand Total");

            List<Double> subtotals = cartPage.getRowSubtotals();
            double grandTotal = cartPage.getGrandTotal();

            System.out.println("Cart subtotals:");
            double sum = 0.0;
            for (int i = 0; i < subtotals.size(); i++) {
                System.out.println("  Row " + i + ": $" + subtotals.get(i));
                sum += subtotals.get(i);
            }
            System.out.println("Sum of subtotals: $" + sum);
            System.out.println("Grand Total: $" + grandTotal);

            Assertions.assertEquals(
                    sum,
                    grandTotal,
                    0.01,
                    "Sum of all item subtotals should equal Grand Total"
            );

            System.out.println("✓ Grand Total matches sum of subtotals");
            System.out.println("=== Test 7 completed successfully ===");

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
