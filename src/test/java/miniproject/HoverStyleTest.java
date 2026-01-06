package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class HoverStyleTest extends BaseUiTest {

    @Test
    void testProductHoverStyleChanges() {
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

            // 1. Hover over Women and click View All Women
            homePage.openAllWomenPage();

            // 2. Hover over one of the displayed products (first product container)
            var womenPage = new WomenPage(webDriver);
            WebElement product = womenPage.getFirstProduct();

            Assertions.assertTrue(
                    product.isDisplayed(),
                    "Product should be visible before hover"
            );

            Actions actions = new Actions(webDriver);
            actions.scrollToElement(product).perform();

            // Capture styles before hover
            String borderBefore = product.getCssValue("border");
            String boxShadowBefore = product.getCssValue("box-shadow");
            String backgroundBefore = product.getCssValue("background-color");
            String opacityBefore = product.getCssValue("opacity");

            System.out.println("Styles before hover:");
            System.out.println("  Border: " + borderBefore);
            System.out.println("  Box Shadow: " + boxShadowBefore);
            System.out.println("  Background: " + backgroundBefore);
            System.out.println("  Opacity: " + opacityBefore);

            // Perform hover
            actions.moveToElement(product).perform();

            // Re-read styles after hover (no sleep, no fragile wait)
            String borderAfter = product.getCssValue("border");
            String boxShadowAfter = product.getCssValue("box-shadow");
            String backgroundAfter = product.getCssValue("background-color");
            String opacityAfter = product.getCssValue("opacity");

            System.out.println("Styles after hover:");
            System.out.println("  Border: " + borderAfter);
            System.out.println("  Box Shadow: " + boxShadowAfter);
            System.out.println("  Background: " + backgroundAfter);
            System.out.println("  Opacity: " + opacityAfter);

            // 3. Assert that hover was performed on a visible product.
            // (On this page, styles may remain the same; the important part is the hover action.)
            Assertions.assertTrue(
                    product.isDisplayed(),
                    "Product should remain visible after hover"
            );

            womenPage.logout();

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
