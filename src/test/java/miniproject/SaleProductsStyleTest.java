package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;

import java.util.List;

public class SaleProductsStyleTest extends BaseUiTest {

    @Test
    void testSaleProductsPriceStyles() {
        try {
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

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
