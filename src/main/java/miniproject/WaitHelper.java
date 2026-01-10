package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utility class for common WebDriver wait operations
 */
public final class WaitHelper {

    private WaitHelper() {
        // Utility class
    }

    /**
     * Wait for an element to be visible
     */
    public static WebElement waitForVisibility(WebDriver driver, By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for an element to be clickable
     */
    public static WebElement waitForClickable(WebDriver driver, By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for an element to be clickable (element already found)
     */
    public static WebElement waitForClickable(WebDriver driver, WebElement element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for element to be invisible
     */
    public static boolean waitForInvisibility(WebDriver driver, By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for page title to contain text
     */
    public static boolean waitForTitleContains(WebDriver driver, String title, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.titleContains(title));
    }

    /**
     * Short pause for UI updates (300ms)
     */
    public static void shortPause(WebDriver driver) {
        pause(driver, Duration.ofMillis(300));
    }

    /**
     * Medium pause for page transitions (500ms)
     */
    public static void mediumPause(WebDriver driver) {
        pause(driver, Duration.ofMillis(500));
    }

    /**
     * Pause for specified duration (use sparingly, prefer explicit waits)
     */
    private static void pause(WebDriver driver, Duration duration) {
        // Use implicit wait as a last resort
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
