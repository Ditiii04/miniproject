package miniproject;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

final class WomenFlowHelper {

    private WomenFlowHelper() {
    }

    /**
     * Full Test 6 flow on the given driver:
     * - Sign in
     * - Women -> View All Women
     * - Sort by Price and verify ascending
     * - Add first two products to wishlist
     * - Check Account dropdown shows "My Wishlist (2 items)"
     */
    static void runWomenSortingAndWishlistFlow(WebDriver webDriver) {
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

        // Wait for Women page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
    }
}
