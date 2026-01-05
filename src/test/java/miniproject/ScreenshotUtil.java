package miniproject;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final Path SCREENSHOT_DIR =
            Path.of("target", "screenshots");

    private ScreenshotUtil() {
        // utility class
    }

    public static void takeScreenshot(WebDriver driver, String testName) {
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }

        try {
            // Make sure directory exists
            Files.createDirectories(SCREENSHOT_DIR);

            var ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName.replace(" ", "_") + "_" + timestamp + ".png";

            Path dest = SCREENSHOT_DIR.resolve(fileName);
            Files.copy(srcFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Saved screenshot: " + dest.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }
}
