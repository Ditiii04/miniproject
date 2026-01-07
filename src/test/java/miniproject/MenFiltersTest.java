package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;

import java.util.List;

public class MenFiltersTest extends BaseUiTest {

    @Test
    void testMenPageFilters() {
        try {
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

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
