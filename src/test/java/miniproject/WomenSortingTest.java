package miniproject;

import org.junit.jupiter.api.Test;

public class WomenSortingTest extends BaseUiTest {

    @Test
    void testWomenSortingAndWishlist() {
        try {
            WomenFlowHelper.runWomenSortingAndWishlistFlow(webDriver);
        } catch (AssertionError | RuntimeException e) {
            markTestFailed();
            throw e;
        }
    }
}
