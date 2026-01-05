package miniproject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public abstract class BaseUiTest {

    protected WebDriver webDriver;
    private boolean testFailed;

    @BeforeEach
    void initDriver(TestInfo testInfo) {
        testFailed = false;
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        webDriver.manage().window().maximize();
        System.out.println("Starting test: " + testInfo.getDisplayName());
    }

    protected void markTestFailed() {
        this.testFailed = true;
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        try {
            if (testFailed && webDriver != null) {
                ScreenshotUtil.takeScreenshot(webDriver, testInfo.getDisplayName());
            }

            // *** keep browser visible for a moment after each test ***
            if (webDriver != null) {
                try {
                    Thread.sleep(2000); // 2 seconds, adjust as you like
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }
}
