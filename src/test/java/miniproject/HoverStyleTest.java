package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;

public class HoverStyleTest extends BaseUiTest {

    @Test
    void testProductNameColorChangesOnHover() {
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

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
