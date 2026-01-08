package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WomenSortingTest extends BaseUiTest {

    @Test
    void testWomenSortingAndWishlist() {
        try {
            System.out.println("Starting test: testWomenSortingAndWishlist()");

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

            // 1. WOMEN -> View All Women
            homePage.openAllWomenPage();

            var womenPage = new WomenPage(webDriver);
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
                            org.openqa.selenium.By.cssSelector("#header-account > div > ul > li:nth-child(2) > a")
                    )
            );

            String wishlistText = wishlistMenuLink.getText();
            System.out.println("Account dropdown wishlist text: '" + wishlistText + "'");

            Assertions.assertEquals(
                    "My Wishlist (2 items)",
                    wishlistText,
                    "Account dropdown should display 'My Wishlist (2 items)'"
            );

            Assertions.assertEquals(
                    "My Wishlist (2 items)",
                    wishlistText,
                    "Account dropdown should display 'My Wishlist (2 items)'"
            );



        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
