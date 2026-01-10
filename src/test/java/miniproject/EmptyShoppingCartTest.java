package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmptyShoppingCartTest extends BaseUiTest {

    @Test
    void testEmptyShoppingCart() {
        try {
            System.out.println("=== Starting Test 8: Empty Shopping Cart Test ===");

            // ===== Precondition: Run Test 7 =====
            System.out.println("Running Test 7 as precondition...");

            // Run Test 6 first (which Test 7 needs)
            WomenFlowHelper.runWomenSortingAndWishlistFlow(webDriver);
            System.out.println("✓ Test 6 precondition completed");

            // Then complete Test 7 steps (add items to cart)
            System.out.println("Completing Test 7 steps (adding items to cart)...");

            // Navigate to wishlist (should still be in dropdown from Test 6)
            webDriver.findElement(org.openqa.selenium.By.cssSelector("#header-account > div > ul > li:nth-child(2) > a"))
                    .click();

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            MyWishlistPage wishlistPage = new MyWishlistPage(webDriver);

            // Add first product to cart
            System.out.println("Adding first product to cart...");
            wishlistPage.clickEditForItem(0);

            ProductDetailPage productPage = new ProductDetailPage(webDriver);
            productPage.selectFirstAvailableColor();
            productPage.selectFirstAvailableSize();
            productPage.clickAddToCart();

            // Navigate back to wishlist
            HomePage homePage = new HomePage(webDriver);
            homePage.openAccountDropdown();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            webDriver.findElement(org.openqa.selenium.By.cssSelector("#header-account > div > ul > li:nth-child(2) > a"))
                    .click();

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            wishlistPage = new MyWishlistPage(webDriver);

            // Add second product to cart
            System.out.println("Adding second product to cart...");
            wishlistPage.clickEditForItem(0);

            productPage = new ProductDetailPage(webDriver);
            productPage.selectFirstAvailableColor();
            productPage.selectFirstAvailableSize();
            productPage.clickAddToCart();

            // Update quantity to 2 for first item
            CartPage cartPage = new CartPage(webDriver);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            cartPage.setQuantityForRow(0, 2);
            cartPage.clickUpdateForRow(0);

            System.out.println("✓ Test 7 precondition completed - cart has items with updated quantities");

            // Refresh cart page
            cartPage = new CartPage(webDriver);

            // ===== Test 8 Starts Here =====
            int initialCount = cartPage.getCartItemCount();
            System.out.println("Initial cart item count: " + initialCount);
            Assertions.assertTrue(initialCount > 0, "Cart should have items before starting Test 8");

            // Steps 1-3: Delete items one by one and verify count decreases
            System.out.println("\nSteps 1-3: Deleting items one by one and verifying count decreases");

            int currentCount = initialCount;
            while (currentCount > 0) {
                System.out.println("\n--- Iteration " + (initialCount - currentCount + 1) + " ---");
                System.out.println("Current cart item count: " + currentCount);

                // Step 1: Delete the first item
                System.out.println("Step 1: Deleting first item in cart...");
                cartPage.deleteFirstItem();

                // Refresh cart page object after deletion
                cartPage = new CartPage(webDriver);

                // Step 2: Verify count decreased by 1
                int newCount = cartPage.getCartItemCount();
                System.out.println("Cart item count after deletion: " + newCount);

                if (newCount > 0) {
                    // Still have items, verify count decreased
                    Assertions.assertEquals(
                            currentCount - 1,
                            newCount,
                            "Cart item count should decrease by 1 after deletion"
                    );
                    System.out.println("✓ Count decreased by 1 (from " + currentCount + " to " + newCount + ")");
                } else {
                    // Cart is now empty
                    System.out.println("✓ Last item deleted, cart is now empty");
                }

                currentCount = newCount;
            }

            System.out.println("\n✓ All items successfully deleted from cart");

            // Step 4: Verify empty cart message
            System.out.println("\nStep 4: Verifying empty cart message");

            boolean isEmpty = cartPage.isCartEmpty();
            Assertions.assertTrue(isEmpty, "Cart should display empty message");

            String emptyMessage = cartPage.getEmptyCartMessage();
            System.out.println("Empty cart message: '" + emptyMessage + "'");

            Assertions.assertTrue(
                    emptyMessage.contains("You have no items in your shopping cart"),
                    "Empty cart message should contain 'You have no items in your shopping cart'"
            );

            System.out.println("✓ Empty cart message verified");

            // Step 5: Close browser (handled by BaseUiTest tearDown)
            System.out.println("\nStep 5: Browser will be closed by test teardown");
            System.out.println("=== Test 8 completed successfully ===");

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
