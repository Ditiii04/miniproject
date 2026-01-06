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

        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
