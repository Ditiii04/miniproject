package miniproject;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    public void afterTestExecution(@NonNull ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            context.getTestInstance().ifPresent(testInstance -> {
                if (testInstance instanceof BaseUiTest baseTest) {
                    ScreenshotUtil.takeScreenshot(baseTest.webDriver, context.getDisplayName());
                }
            });
        }
    }

}