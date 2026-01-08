package miniproject;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

        // 1. WOMEN -> View All Women
        homePage.openAllWomenPage();

        WomenPage womenPage = new WomenPage(webDriver);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        // 2. Sort By -> Price
        WebElement sortSelect = womenPage.getSortBySelect();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", sortSelect);
        sortSelect.click();

        WebElement priceOption = womenPage.getSortByPriceOption();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", priceOption);
        priceOption.click();

        // Scroll before checking
        List<WebElement> products = womenPage.getAllProducts();
        if (!products.isEmpty()) {
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});",
                    products.get(0));
        }

        // 3. Verify prices sorted asc (special price used when present)
        List<Double> prices = womenPage.getAllEffectivePrices();
        System.out.println("Effective prices (special where present): " + prices);

        List<Double> sortedPrices = new ArrayList<>(prices);
        sortedPrices.sort(Double::compareTo);

        Assertions.assertEquals(
                sortedPrices,
                prices,
                "Products should be sorted by price ascending when Sort By = Price"
        );

        // 4. Add first product to wishlist
        WebElement firstWishlist = womenPage.getFirstProductWishlistLink();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstWishlist);
        firstWishlist.click();

        // Back to Women page, add second product to wishlist
        homePage = new HomePage(webDriver);
        homePage.openAllWomenPage();
        womenPage = new WomenPage(webDriver);

        WebElement secondWishlist = womenPage.getSecondProductWishlistLink();
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", secondWishlist);
        secondWishlist.click();

        // 5. Open Account dropdown and check "My Wishlist (2 items)" there
        homePage.openAccountDropdown();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
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
    }
}
