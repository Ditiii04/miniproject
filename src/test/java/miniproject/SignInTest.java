package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SignInTest extends BaseUiTest {

    @Test
    void testSignInWithAccountFromTest1() {
        try {
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

            // 4. Verify we are logged in as that user:
            //    - My Account title
            //    - URL contains /customer/account/
            Assertions.assertEquals(
                    "My Account",
                    webDriver.getTitle(),
                    "After login, title should be 'My Account'"
            );

            Assertions.assertTrue(
                    webDriver.getCurrentUrl().contains("/customer/account/"),
                    "After login, URL should contain /customer/account/"
            );

            // If you still want to try welcome text later, you can print it:
            // System.out.println("Welcome block HTML: " + webDriver.getPageSource());

            // 5. Click on Account and Log Out
            loginPage.logout();

        } catch (AssertionError e) {
            markTestFailed();
            throw e;
        } catch (RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
