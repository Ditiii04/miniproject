package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

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

            // 2. Hover over the anchor that wraps the selected product image
            var womenPage = new WomenPage(webDriver);
            WebElement anchor = womenPage.getProductAnchor();

            // CSS before hover: link color
            String beforeColor = anchor.getCssValue("color");

            womenPage.hoverOverElement(anchor);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String afterColor = anchor.getCssValue("color");

            boolean colorChanged = !beforeColor.equals(afterColor);

            // 3. Assert that styles have changed to indicate a hover effect
            Assertions.assertTrue(
                    colorChanged,
                    "Product hover should change the link color. " +
                            "Before color: " + beforeColor +
                            " | After color: " + afterColor
            );

            womenPage.logout();

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
