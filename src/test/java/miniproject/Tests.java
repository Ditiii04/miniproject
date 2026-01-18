package miniproject;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Tests extends BaseUiTest {

    @Order(1)
    @Test
    void testAccountCreation() {
        webDriver.get(PageType.HOME.getUrl());

        var homePage = new HomePage(webDriver);
        homePage.tryAcceptConsent();
        homePage.openRegisterPage();

        Assertions.assertEquals(
                PageType.CREATE_ACCOUNT.getTitle(),
                webDriver.getTitle(),
                "Create Account page title did not match"
        );

        var accountPage = new AccountPage(webDriver);

        accountPage.fillFirstName("Test");
        accountPage.fillMiddleName("Selenium");
        accountPage.fillLastName("Automation");

        String email = "test" + System.currentTimeMillis() + "@mail.com";
        accountPage.fillEmail(email);

        // save email for Test 2
        CredentialStore.saveEmail(email);

        accountPage.fillPassword("Password123!");
        accountPage.fillConfirmPassword("Password123!");

        accountPage.submit();

        Assertions.assertTrue(
                accountPage.isSuccessMessageDisplayed(),
                "Success message should be displayed after account creation"
        );

        Assertions.assertEquals("My Account", webDriver.getTitle());
        Assertions.assertTrue(
                webDriver.getCurrentUrl().contains("/customer/account/"),
                "URL should contain /customer/account/ after successful registration"
        );

        accountPage.logout();
    }

    @Order(2)
    @Test
    void testSignInWithAccountFromTest1() {
        // 1. Navigate to home page
        webDriver.get(PageType.HOME.getUrl());

        var homePage = new HomePage(webDriver);
        homePage.tryAcceptConsent();

        // 2. Click on Account then Sign in
        homePage.openLoginPage();

        // 3. Login with the credentials created from Test 1
        String email = CredentialStore.loadEmail();
        String password = "Password123!"; // same as in Test 1

        Assertions.assertFalse(
                email.isEmpty(),
                "Email from Test 1 should not be empty. Make sure Test 1 ran first."
        );

        var loginPage = new LoginPage(webDriver);
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.submitLogin();

        // 4a. Verify we are on My Account page
        Assertions.assertEquals(
                "My Account",
                webDriver.getTitle(),
                "After login, title should be 'My Account'"
        );

        String currentUrl = webDriver.getCurrentUrl();
        Assertions.assertNotNull(currentUrl, "Current URL should not be null after login");
        Assertions.assertTrue(
                currentUrl.contains("/customer/account/"),
                "After login, URL should contain /customer/account/"
        );

        // 4b. Check your username is displayed on right corner of the page
        String welcomeText = loginPage.getWelcomeMessageText();
        System.out.println("Welcome text: " + welcomeText);

        String expectedFirstName = "Test";
        String expectedMiddleName = "Selenium";
        String expectedLastName = "Automation";

        String upperWelcome = welcomeText.toUpperCase();

        Assertions.assertTrue(
                upperWelcome.contains(expectedFirstName.toUpperCase()) &&
                        upperWelcome.contains(expectedMiddleName.toUpperCase()) &&
                        upperWelcome.contains(expectedLastName.toUpperCase()),
                "Header welcome text should contain first, middle and last name. Actual: " + welcomeText
        );

        // 5. Click on Account and Log Out
        loginPage.logout();
    }

    @Order(3)
    @Test
    void testProductNameColorChangesOnHover() {
        // Precondition: Sign in
        webDriver.get(PageType.HOME.getUrl());

        var homePage = new HomePage(webDriver);
        homePage.tryAcceptConsent();
        homePage.openLoginPage();

        String email = CredentialStore.loadEmail();
        String password = "Password123!";

        Assertions.assertFalse(
                email.isEmpty(),
                "Email from Test 1 should not be empty. Make sure Test 1 ran first."
        );

        var loginPage = new LoginPage(webDriver);
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.submitLogin();

        Assertions.assertEquals("My Account", webDriver.getTitle());

        // Go to Women page
        homePage.openAllWomenPage();

        var womenPage = new WomenPage(webDriver);
        WebElement nameLink = womenPage.getFirstProductNameLink();

        Assertions.assertTrue(
                nameLink.isDisplayed(),
                "Product name link should be visible before hover"
        );

        // 1) Scroll with JS so we are 100% in viewport
        ((JavascriptExecutor) webDriver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", nameLink);

        // 2) Read color before hover
        String colorBefore = nameLink.getCssValue("color");
        String hexBefore = Color.fromString(colorBefore).asHex();
        System.out.println("Name link color BEFORE hover: " + colorBefore + " | hex: " + hexBefore);

        // 3) Hover the text
        Actions actions = new Actions(webDriver);
        actions.moveToElement(nameLink).perform();

        // 4) Read color after hover
        String colorAfter = nameLink.getCssValue("color");
        String hexAfter = Color.fromString(colorAfter).asHex();
        System.out.println("Name link color AFTER hover: " + colorAfter + " | hex: " + hexAfter);

        // 5) Assert the text color changed
        Assertions.assertNotEquals(
                hexBefore,
                hexAfter,
                "Product name text color should change on hover"
        );

        Assertions.assertTrue(
                nameLink.isDisplayed(),
                "Product name link should remain visible after hover"
        );

        womenPage.logout();
    }

    @Order(4)
    @Test
    void testSaleProductsPriceStyles() {
        // Precondition: Sign in
        webDriver.get(PageType.HOME.getUrl());

        var homePage = new HomePage(webDriver);
        homePage.tryAcceptConsent();
        homePage.openLoginPage();

        String email = CredentialStore.loadEmail();
        String password = "Password123!";

        Assertions.assertFalse(
                email.isEmpty(),
                "Email from Test 1 should not be empty. Make sure Test 1 ran first."
        );

        var loginPage = new LoginPage(webDriver);
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.submitLogin();

        Assertions.assertEquals("My Account", webDriver.getTitle());

        // 1. Hover over Sale and click "View All Sale"
        homePage.openAllSalePage();

        var salePage = new SalePage(webDriver);
        List<WebElement> products = salePage.getAllProducts();

        Assertions.assertFalse(
                products.isEmpty(),
                "There should be at least one product on the Sale page"
        );

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        Actions actions = new Actions(webDriver);

        String expectedGreyHex = "#a0a0a0";  // from .price-box .old-price .price
        String expectedBlueHex = "#3399cc";  // from .price-box .price

        // 2. For each product: scroll into view, then verify prices
        for (WebElement product : products) {
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", product);

            WebElement oldPrice = salePage.getOldPrice(product);
            WebElement specialPrice = salePage.getSpecialPrice(product);

            // Optional: hover product, in case UI changes on hover
            actions.moveToElement(product).perform();

            // 2) Multiple prices shown
            Assertions.assertTrue(
                    oldPrice.isDisplayed(),
                    "Original (old) price should be visible for sale product"
            );
            Assertions.assertTrue(
                    specialPrice.isDisplayed(),
                    "Discounted (special) price should be visible for sale product"
            );

            // 3) Original price: grey + strikethrough
            String oldColor = oldPrice.getCssValue("color");
            String oldHex = Color.fromString(oldColor).asHex();
            String oldDecoration = oldPrice.getCssValue("text-decoration");

            System.out.println("Old price color: " + oldColor + " | hex: " + oldHex);
            System.out.println("Old price text-decoration: " + oldDecoration);

            Assertions.assertEquals(
                    expectedGreyHex,
                    oldHex,
                    "Original price should have grey color"
            );

            Assertions.assertTrue(
                    oldDecoration.contains("line-through"),
                    "Original price should be strikethrough"
            ); // strikethrough check[web:354][web:370]

            // 4) Final price: blue + no strikethrough
            String specialColor = specialPrice.getCssValue("color");
            String specialHex = Color.fromString(specialColor).asHex();
            String specialDecoration = specialPrice.getCssValue("text-decoration");

            System.out.println("Special price color: " + specialColor + " | hex: " + specialHex);
            System.out.println("Special price text-decoration: " + specialDecoration);

            Assertions.assertEquals(
                    expectedBlueHex,
                    specialHex,
                    "Final price should have blue color"
            ); // color verification[web:266][web:273]

            Assertions.assertFalse(
                    specialDecoration.contains("line-through"),
                    "Final price should NOT be strikethrough"
            );
        }

        salePage.logout();
    }

    @Order(5)
    @Test
    void testMenPageFilters() {
        System.out.println("Starting test: testMenPageFilters()");

        // Precondition: Sign in
        webDriver.get(PageType.HOME.getUrl());

        var homePage = new HomePage(webDriver);
        homePage.tryAcceptConsent();
        homePage.openLoginPage();

        String email = CredentialStore.loadEmail();
        String password = "Password123!";

        Assertions.assertFalse(
                email.isEmpty(),
                "Email from Test 1 should not be empty. Make sure Test 1 ran first."
        );

        var loginPage = new LoginPage(webDriver);
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.submitLogin();

        Assertions.assertEquals("My Account", webDriver.getTitle());

        // 1. Hover over MEN and click "View All Men"
        homePage.openAllMenPage();

        var menPage = new MenPage(webDriver);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        // 2. From Shopping Options panel click on black color
        WebElement blackFilter = menPage.getBlackColorFilter();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", blackFilter);
        blackFilter.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 3. Check that all displayed products have the selected color bordered in blue
        List<WebElement> productsWithBlack = menPage.getAllProducts();
        Assertions.assertFalse(
                productsWithBlack.isEmpty(),
                "There should be products after applying black color filter"
        );

        String expectedBlueHex = "#3399cc";

        for (WebElement product : productsWithBlack) {
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", product);
            new Actions(webDriver).moveToElement(product).perform();

            WebElement blackSwatchImg = menPage.getBlackSwatchForProduct(product);

            WebElement swatchLink = blackSwatchImg.findElement(
                    org.openqa.selenium.By.xpath("./ancestor::a[1]")
            );

            String borderColor = swatchLink.getCssValue("border-color");
            String borderHex = Color.fromString(borderColor).asHex();

            System.out.println("Black swatch border color: " + borderColor + " | hex: " + borderHex);

            Assertions.assertEquals(
                    expectedBlueHex,
                    borderHex,
                    "Selected black color swatch should have blue border"
            );
        }

        // 4. Reset filters by going back to MEN -> View All Men (instead of clicking X)
        js.executeScript("window.scrollTo(0, 0);");
        webDriver.get(PageType.HOME.getUrl());
        homePage = new HomePage(webDriver);
        homePage.tryAcceptConsent();
        homePage.openAllMenPage();
        menPage = new MenPage(webDriver);
        js = (JavascriptExecutor) webDriver;

        // Apply price filter $0.00 - $99.99
        WebElement priceFilter = menPage.getFirstPriceFilter();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", priceFilter);
        priceFilter.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Confirm text "3 Item(s)" and that only 3 products are displayed
        WebElement itemsText = menPage.getItemsCountText();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", itemsText);

        String itemsTextValue = itemsText.getText();
        System.out.println("Items count text: " + itemsTextValue);

        Assertions.assertTrue(
                itemsTextValue.contains("3 Item"),
                "Pager text should indicate 3 items"
        );

        List<WebElement> pricedProducts = menPage.getAllProducts();
        Assertions.assertEquals(
                3,
                pricedProducts.size(),
                "Exactly 3 products should be displayed for the selected price range"
        );

        // 5. For each product displayed, check that the price matches the defined criteria
        for (WebElement product : pricedProducts) {
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", product);

            WebElement priceElement = menPage.getPriceElement(product);
            String priceText = priceElement.getText(); // e.g. "$75.00"
            System.out.println("Product price: " + priceText);

            String numeric = priceText.replace("$", "").replace(",", "").trim();
            double price = Double.parseDouble(numeric);

            Assertions.assertTrue(
                    price >= 0.0 && price <= 99.99,
                    "Product price " + price + " should be within $0.00 - $99.99"
            );
        }

        menPage.logout();
    }

    @Order(6)
    @Test
    void womanFullFlowTest() {
        // ------------------------------ Test 6 -----------------------------------
        System.out.println("Running Women sorting + wishlist flow (Test 6)");

        webDriver.get(PageType.HOME.getUrl());

        HomePage homePage = new HomePage(webDriver);
        homePage.tryAcceptConsent();
        homePage.openLoginPage();

        String email = CredentialStore.loadEmail();
        String password = "Password123!";

        Assertions.assertFalse(
                email.isEmpty(),
                "Email from Test 1 should not be empty. Make sure Test 1 ran first."
        );

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.submitLogin();

        Assertions.assertEquals("My Account", webDriver.getTitle());
        System.out.println("✓ Logged in successfully");

        // 1. WOMEN -> View All Women
        System.out.println("Step 1: Hover over WOMEN and click View All Women");
        homePage.openAllWomenPage();

        WomenPage womenPage = new WomenPage(webDriver);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        System.out.println("✓ Women page opened successfully");

        // 2. Sort By -> Price
        System.out.println("Step 2: Selecting Price from Sort By dropdown");
        WebElement sortSelectElement = womenPage.getSortBySelect();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", sortSelectElement);

        // Small delay before interaction
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Use Select class for proper dropdown handling
        Select sortDropdown = new Select(sortSelectElement);
        System.out.println("Available options in Sort By dropdown:");
        for (WebElement option : sortDropdown.getOptions()) {
            System.out.println("  - " + option.getText());
        }

        // Select by visible text "Price"
        sortDropdown.selectByVisibleText("Price");
        System.out.println("Selected 'Price' from dropdown");

        // Wait for page to reload/resort after selecting price
        System.out.println("Waiting for page to resort after selecting Price...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Refresh page objects after sort
        womenPage = new WomenPage(webDriver);

        // 3. Verify prices sorted asc (special price used when present)
        System.out.println("Step 3: Verifying products are sorted by price ascending");

        // Scroll to first product
        List<WebElement> products = womenPage.getAllProducts();
        Assertions.assertFalse(products.isEmpty(), "Products should be displayed after sorting");

        if (!products.isEmpty()) {
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", products.get(0));
        }

        List<Double> prices = womenPage.getAllEffectivePrices();
        System.out.println("Effective prices (special where present): " + prices);
        Assertions.assertFalse(prices.isEmpty(), "Should have at least one product price");

        List<Double> sortedPrices = new ArrayList<>(prices);
        sortedPrices.sort(Double::compareTo);

        Assertions.assertEquals(
                sortedPrices,
                prices,
                "Products should be sorted by price ascending when Sort By = Price"
        );
        System.out.println("✓ Products are correctly sorted by price ascending");

        // 4. Add first two products to wishlist
        System.out.println("Step 4: Adding first two products to wishlist");

        // 4a. Add FIRST product to wishlist
        System.out.println("Adding first product to wishlist...");
        WebElement firstProduct = womenPage.getFirstProduct();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstProduct);

        // Wait before hover
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        womenPage.hoverOverElement(firstProduct);

        // Wait for wishlist link to appear
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement firstWishlist = womenPage.getFirstProductWishlistLink();
        js.executeScript("arguments[0].click();", firstWishlist);

        // Wait for wishlist addition to complete
        System.out.println("Waiting for first product to be added to wishlist...");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 4b. Navigate back to Women page for second product
        System.out.println("Navigating back to Women page for second product...");
        homePage = new HomePage(webDriver);
        homePage.openAllWomenPage();
        womenPage = new WomenPage(webDriver);

        // Wait for page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Add SECOND product to wishlist
        System.out.println("Adding second product to wishlist...");
        List<WebElement> productsForSecond = womenPage.getAllProducts();
        Assertions.assertTrue(productsForSecond.size() >= 2, "Should have at least 2 products");

        WebElement secondProduct = productsForSecond.get(1);
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", secondProduct);

        // Wait before hover
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        womenPage.hoverOverElement(secondProduct);

        // Wait for wishlist link to appear
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement secondWishlist = womenPage.getSecondProductWishlistLink();
        js.executeScript("arguments[0].click();", secondWishlist);

        // Wait for wishlist addition to complete
        System.out.println("Waiting for second product to be added to wishlist...");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("✓ Two products added to wishlist");

        // 5. Open Account dropdown and check "My Wishlist (2 items)" there
        System.out.println("Step 5: Checking Account dropdown for 'My Wishlist (2 items)'");

        // Scroll to top to ensure account dropdown is visible
        js.executeScript("window.scrollTo(0, 0);");
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Opening Account dropdown...");
        homePage.openAccountDropdown();

        // Wait for dropdown to be visible
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement wishlistMenuLink = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#header-account > div > ul > li:nth-child(2) > a")
                )
        );

        String wishlistText = wishlistMenuLink.getText();
        System.out.println("Account dropdown wishlist text: '" + wishlistText + "'");

        Assertions.assertEquals(
                "My Wishlist (2 items)",
                wishlistText,
                "Account dropdown should display 'My Wishlist (2 items)'"
        );

        System.out.println("✓ Account dropdown correctly shows 'My Wishlist (2 items)'");
        System.out.println("=== Test 6 completed successfully ===");


        // ------------------------------ Test 7 -----------------------------------
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

        // ------------------------------ Test 8 -----------------------------------
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
    }

}
