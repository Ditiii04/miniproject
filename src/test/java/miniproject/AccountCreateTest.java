package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountCreateTest extends BaseUiTest {

    @Test
    void testAccountCreation() {
        try {
            webDriver.get(PageType.HOME.getUrl());

            var homePage = new HomePage(webDriver);
            homePage.tryAcceptConsent();
            homePage.openRegisterPage();

            Assertions.assertEquals(
                    PageType.CREATE_ACCOUNT.getTitle(),
                    webDriver.getTitle(),
                    "Create Account page title did not match"
            );

            var accountPage = new AccountPage(webDriver);

            accountPage.fillFirstName("Test");
            accountPage.fillMiddleName("Selenium");
            accountPage.fillLastName("Automation");

            String email = "test" + System.currentTimeMillis() + "@mail.com";
            accountPage.fillEmail(email);

            // save email for Test 2
            CredentialStore.saveEmail(email);

            accountPage.fillPassword("Password123!");
            accountPage.fillConfirmPassword("Password123!");

            accountPage.submit();

            Assertions.assertTrue(
                    accountPage.isSuccessMessageDisplayed(),
                    "Success message should be displayed after account creation"
            );

            Assertions.assertEquals("My Account", webDriver.getTitle());
            Assertions.assertTrue(
                    webDriver.getCurrentUrl().contains("/customer/account/"),
                    "URL should contain /customer/account/ after successful registration"
            );

            accountPage.logout();

        } catch (AssertionError e) {
            markTestFailed();
            throw e;
        } catch (RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
