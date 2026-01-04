package miniproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class AccountCreateTest {

    private final WebDriver webDriver = new ChromeDriver();

    @BeforeEach
    void initDriver() {
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @Test
    void testAccountCreation() {
        webDriver.get(PageType.HOME.getUrl());
        var homePage = new HomePage(webDriver);
        homePage.tryAcceptConsent();
        homePage.openRegisterPage();
        Assertions.assertEquals("Create New Customer Account", webDriver.getTitle());

        var accountPage = new AccountPage(webDriver);
        accountPage.fillFirstName("Bomba");
        accountPage.fillMiddleName("Gragos");
    }

}
