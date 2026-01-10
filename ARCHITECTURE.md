# Project Architecture

## Overview

This test automation project follows industry best practices and design patterns to ensure maintainability, scalability, and reliability.

## Design Patterns

### 1. Page Object Model (POM)

**Purpose:** Separate test logic from page-specific code

**Implementation:**
- Each page has its own class (e.g., `HomePage`, `LoginPage`, `CartPage`)
- Page classes contain locators and methods specific to that page
- Tests interact with pages through methods, never directly with elements

**Benefits:**
- Easy maintenance when UI changes
- Reusable page interactions
- Clear separation of concerns
- Reduced code duplication

**Example:**
```java
// Instead of this in test:
webDriver.findElement(By.id("email")).sendKeys("test@mail.com");

// We do this:
loginPage.fillEmail("test@mail.com");
```

### 2. Base Page Pattern

**Purpose:** Centralize common page functionality

**Implementation:**
- `BasePage` contains methods used across multiple pages
- All page classes extend `BasePage`
- Includes navigation, wait utilities, and common operations

**Benefits:**
- Single place for common functionality
- Consistent behavior across pages
- Easier to update shared logic

### 3. Base Test Pattern

**Purpose:** Standardize test setup and teardown

**Implementation:**
- `BaseUiTest` handles WebDriver lifecycle
- Sets up driver before each test
- Handles screenshot capture on failure
- Cleans up after each test

**Benefits:**
- Consistent test environment
- Automatic resource cleanup
- Centralized failure handling

## Project Structure

```
┌─────────────────────────────────────────────┐
│           Test Classes                       │
│  (AccountCreateTest, SignInTest, etc.)      │
└────────────────┬────────────────────────────┘
                 │ extends
                 ▼
┌─────────────────────────────────────────────┐
│           BaseUiTest                         │
│  - WebDriver setup/teardown                 │
│  - Screenshot on failure                    │
└─────────────────────────────────────────────┘
                 │ uses
                 ▼
┌─────────────────────────────────────────────┐
│           Page Objects                       │
│  (HomePage, LoginPage, CartPage, etc.)      │
└────────────────┬────────────────────────────┘
                 │ extends
                 ▼
┌─────────────────────────────────────────────┐
│           BasePage                           │
│  - Common navigation                         │
│  - Wait utilities                            │
│  - Shared operations                         │
└─────────────────────────────────────────────┘
                 │ uses
                 ▼
┌─────────────────────────────────────────────┐
│           Helper Classes                     │
│  - WaitHelper                                │
│  - CredentialStore                           │
│  - ScreenshotUtil                            │
└─────────────────────────────────────────────┘
```

## Wait Strategy

### Explicit Waits (Preferred)
Used for dynamic elements that may take time to appear:
```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
```

### Implicit Waits
Set globally to 2 seconds in BaseUiTest:
```java
webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
```

### When Thread.sleep is Used
Only in specific cases:
- **Character-by-character typing simulation** - Mimics human typing
- **UI animation delays** - Where explicit waits cannot detect changes

## Error Handling Strategy

### 1. Screenshot on Failure
```java
@AfterEach
void tearDown(TestInfo testInfo) {
    if (testFailed) {
        ScreenshotUtil.takeScreenshot(webDriver, testInfo.getDisplayName());
    }
    webDriver.quit();
}
```

### 2. Try-Catch in Tests
All tests wrap logic in try-catch to mark failures:
```java
try {
    // Test logic
} catch (AssertionError | RuntimeException e) {
    markTestFailed();
    throw e;
}
```

### 3. Graceful Fallbacks
Multiple selector attempts for resilient element finding:
```java
By[] selectors = {selector1, selector2, selector3};
for (By selector : selectors) {
    try {
        return webDriver.findElement(selector);
    } catch (Exception e) {
        // Try next selector
    }
}
```

## Data Management

### Credential Persistence
```java
// After Test 1 creates account:
CredentialStore.saveEmail(email);

// Test 2 retrieves it:
String email = CredentialStore.loadEmail();
```

**Location:** `target/last-account.txt`

## Test Dependencies

Some tests depend on previous tests:

```
Test 1 (Create Account)
    ↓
Test 2 (Sign In) ← Uses credentials from Test 1
    ↓
Test 3-5 (Various features) ← Need login
    ↓
Test 6 (Sorting & Wishlist) ← Adds items to wishlist
    ↓
Test 7 (Shopping Cart) ← Uses wishlist from Test 6
    ↓
Test 8 (Empty Cart) ← Empties cart from Test 7
```

## Key Design Decisions

### 1. Dynamic Element Selection
**Decision:** Avoid hardcoded IDs and indices
**Reason:** Tests break when product order changes
**Implementation:** Use relative positioning and iteration

### 2. Helper Classes
**Decision:** Extract reusable flows to helper classes
**Reason:** Test 7 and 8 both need Test 6 flow
**Implementation:** `WomenFlowHelper.runWomenSortingAndWishlistFlow()`

### 3. JavaScript Execution
**Decision:** Use JavaScript for some clicks
**Reason:** Avoids element interception issues
**Implementation:**
```java
((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
```

### 4. Multiple Selector Strategy
**Decision:** Try multiple selectors for same element
**Reason:** Website structure may vary or update
**Implementation:** Fallback selector arrays

### 5. Scroll Before Interaction
**Decision:** Scroll elements into view before interaction
**Reason:** Prevents "element not interactable" errors
**Implementation:**
```java
js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
```

## Scalability Considerations

### Adding New Tests
1. Create test class extending `BaseUiTest`
2. Reuse existing page objects where possible
3. Create new page objects as needed
4. Follow naming conventions

### Adding New Pages
1. Extend `BasePage`
2. Define locators as private static final
3. Create public methods for page interactions
4. Use WebDriverWait for dynamic elements

### Modifying Existing Tests
1. Check if page object needs update first
2. Update locators in page object, not in test
3. Maintain backward compatibility when possible
4. Update documentation

## Performance Optimization

### 1. Parallel Execution (Future)
Currently sequential, but can be parallelized:
```xml
<configuration>
    <parallel>classes</parallel>
    <threadCount>4</threadCount>
</configuration>
```

### 2. Test Data Management
- Unique emails using timestamp
- No database cleanup needed
- Fast test execution

### 3. Smart Waits
- Short timeouts for fast elements (2s)
- Longer timeouts for slow operations (10s)
- No unnecessary waits

## Testing Best Practices Applied

1. ✅ **Arrange-Act-Assert** pattern
2. ✅ **One assertion concept per test**
3. ✅ **Independent tests** (where possible)
4. ✅ **Descriptive test names**
5. ✅ **Setup/Teardown in base class**
6. ✅ **Page Object Model**
7. ✅ **Explicit waits over implicit**
8. ✅ **Screenshots on failure**
9. ✅ **Meaningful assertions messages**
10. ✅ **DRY principle**

## Technology Choices

### Why Selenium?
- Industry standard for web automation
- Excellent browser support
- Large community and resources

### Why JUnit 5?
- Modern testing framework
- Excellent IDE integration
- Powerful assertions and annotations

### Why Chrome?
- Most popular browser
- Good developer tools
- Reliable WebDriver support

### Why WebDriverManager?
- Automatic driver management
- No manual driver downloads
- Always up-to-date

## Future Enhancements

1. **Parallel Test Execution** - Speed up test suite
2. **Data-Driven Testing** - Test with multiple data sets
3. **Cross-Browser Testing** - Chrome, Firefox, Edge
4. **CI/CD Integration** - GitHub Actions / Jenkins
5. **Allure Reporting** - Rich test reports
6. **API Testing** - Complement UI tests
7. **Mobile Testing** - Appium integration
8. **Docker Support** - Containerized test execution

---

**Document Version:** 1.0
**Last Updated:** 2026-01-10
