# Selenium + Java - E-commerce Test Automation Project

A comprehensive test automation framework for the Tealium E-commerce Demo website using Selenium WebDriver, Java, and JUnit 5.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Test Scenarios](#test-scenarios)
- [Setup Instructions](#setup-instructions)
- [Running Tests](#running-tests)
- [Project Features](#project-features)
- [Page Object Model](#page-object-model)
- [Reporting](#reporting)

## 🎯 Project Overview

This project automates 8 comprehensive test scenarios for an e-commerce website, covering:
- User account management
- Authentication
- Product browsing and filtering
- Shopping cart operations
- Wishlist functionality

**Website Under Test:** https://ecommerce.tealiumdemo.com/

## 🛠 Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Programming Language |
| Selenium WebDriver | 4.39.0 | Browser Automation |
| JUnit Jupiter | 6.0.1 | Testing Framework |
| WebDriverManager | 6.3.3 | Driver Management |
| Maven | - | Build Tool |
| Apache Commons Text | 1.15.0 | Utilities |
| Apache Commons Validator | 1.10.1 | Validation |

## 📁 Project Structure

```
miniproject/
├── src/
│   ├── main/java/miniproject/
│   │   ├── BasePage.java              # Base page with common functionality
│   │   ├── HomePage.java              # Home page object
│   │   ├── LoginPage.java             # Login page object
│   │   ├── AccountPage.java           # Account creation page
│   │   ├── WomenPage.java             # Women products page
│   │   ├── MenPage.java               # Men products page
│   │   ├── SalePage.java              # Sale products page
│   │   ├── MyWishlistPage.java        # Wishlist page
│   │   ├── ProductDetailPage.java     # Product detail page
│   │   ├── CartPage.java              # Shopping cart page
│   │   ├── PageType.java              # Page URL/title enum
│   │   └── WaitHelper.java            # Wait utilities
│   │
│   └── test/java/miniproject/
│       ├── BaseUiTest.java            # Base test setup/teardown
│       ├── AccountCreateTest.java     # Test 1: Account creation
│       ├── SignInTest.java            # Test 2: Sign in
│       ├── HoverStyleTest.java        # Test 3: Hover effects
│       ├── SaleProductsStyleTest.java # Test 4: Sale product styles
│       ├── MenFiltersTest.java        # Test 5: Filtering
│       ├── WomenSortingTest.java      # Test 6: Sorting & Wishlist
│       ├── ShoppingCartTest.java      # Test 7: Cart operations
│       ├── EmptyShoppingCartTest.java # Test 8: Empty cart
│       ├── WomenFlowHelper.java       # Reusable flow helper
│       ├── CredentialStore.java       # Credential persistence
│       └── ScreenshotUtil.java        # Screenshot on failure
│
├── pom.xml                            # Maven configuration
└── README.md                          # This file
```

## 🧪 Test Scenarios

### Test 1: Create an Account ✅
- Navigate to home page
- Open registration page
- Fill in account details with unique email
- Verify successful account creation
- Verify success message displayed
- Logout

### Test 2: Sign In ✅
- Navigate to home page
- Open login page
- Login with credentials from Test 1
- Verify user is logged in
- Verify username displayed in header
- Logout

### Test 3: Check Hover Style ✅
- **Precondition:** User logged in
- Navigate to Women page
- Hover over product
- Verify product name color changes on hover
- Logout

### Test 4: Check Sale Products Style ✅
- **Precondition:** User logged in
- Navigate to Sale page
- For each product, verify:
  - Multiple prices shown (original + discounted)
  - Original price is grey with strikethrough
  - Final price is blue without strikethrough
- Logout

### Test 5: Check Page Filters ✅
- **Precondition:** User logged in
- Navigate to Men page
- Apply black color filter
- Verify all products have black swatch with blue border
- Apply price filter ($0.00 - $99.99)
- Verify only 3 products displayed
- Verify all prices within range
- Logout

### Test 6: Check Sorting ✅
- **Precondition:** User logged in
- Navigate to Women page
- Sort products by Price (ascending)
- Verify products sorted correctly
- Add first 2 products to wishlist
- Verify "My Wishlist (2 items)" in account dropdown

### Test 7: Shopping Cart Test ✅
- **Precondition:** Test 6 completed
- Go to My Wishlist
- Add both wishlist items to cart (select color & size)
- Change quantity to 2 for first product
- Verify sum of subtotals equals Grand Total

### Test 8: Empty Shopping Cart Test ✅
- **Precondition:** Test 7 completed
- Delete items from cart one by one
- Verify item count decreases after each deletion
- Verify empty cart message displayed
- Close browser

## 🚀 Setup Instructions

### Prerequisites
- Java 25 or higher installed
- Maven installed
- Chrome browser installed

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd miniproject
```

2. Install dependencies:
```bash
mvn clean install
```

## ▶️ Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test
```bash
# Run single test
mvn test -Dtest=AccountCreateTest

# Run multiple tests
mvn test -Dtest=AccountCreateTest,SignInTest
```

### Run Tests in Sequence
Tests 1-8 should be run in order as some tests depend on previous ones:
```bash
# Run tests in order
mvn test -Dtest=AccountCreateTest,SignInTest,HoverStyleTest,SaleProductsStyleTest,MenFiltersTest,WomenSortingTest,ShoppingCartTest,EmptyShoppingCartTest
```

## ✨ Project Features

### 1. Page Object Model (POM)
- Clean separation between test logic and page interactions
- Reusable page objects
- Easy maintenance and scalability

### 2. Screenshot on Failure
- Automatically captures screenshots when tests fail
- Screenshots saved to `target/screenshots/`
- Includes timestamp and test name

### 3. Credential Management
- Saves created account credentials to file
- Reuses credentials across dependent tests
- Located in `target/last-account.txt`

### 4. Explicit Waits
- Uses WebDriverWait for reliable synchronization
- WaitHelper utility for common wait patterns
- Minimal use of Thread.sleep (only for UI simulation)

### 5. Dynamic Element Handling
- No hardcoded IDs or indices where possible
- Robust selectors with fallback options
- Handles stale elements gracefully

### 6. Detailed Logging
- Console output for each test step
- Clear progress indicators
- Helpful debugging information

### 7. Assertion-Based Verification
- JUnit assertions for all validations
- Clear failure messages
- Comprehensive test coverage

## 📦 Page Object Model

### BasePage
Central base class providing:
- Navigation methods (Women, Men, Sale pages)
- Account dropdown operations
- Privacy consent handling
- Success message verification
- Logout functionality
- Common utilities (typing, clicking with waits)

### Test-Specific Pages

#### HomePage
- Extends BasePage
- Entry point for navigation

#### LoginPage
- Email and password input
- Submit login with wait
- Welcome message verification

#### AccountPage
- Account registration form
- Field input methods
- Form submission

#### WomenPage / MenPage / SalePage
- Product listing access
- Sorting functionality
- Filtering options
- Price extraction
- Wishlist operations

#### MyWishlistPage
- View wishlist items
- Edit wishlist items
- Navigate to product details
- Item count tracking

#### ProductDetailPage
- Color selection
- Size selection
- Add to cart
- Dynamic swatch handling

#### CartPage
- View cart items
- Update quantities
- Delete items
- Calculate totals
- Empty cart verification

## 📊 Reporting

### Console Output
- Real-time test execution status
- Step-by-step progress
- Assertion results
- Timing information

### Screenshots
Failed tests automatically capture screenshots:
- Location: `target/screenshots/`
- Format: `TestName_YYYYMMdd_HHmmss.png`
- Includes full page context

### Maven Surefire Reports
After running tests:
```bash
# View reports
open target/surefire-reports/index.html
```

## 🔧 Configuration

### Browser Configuration
- Default: Chrome
- Driver: Managed automatically by WebDriverManager
- Window: Maximized
- Implicit Wait: 2 seconds

### Timeouts
- Page Load: Default
- Element Wait: 2-10 seconds (context-dependent)
- Script Timeout: Default

## 📝 Best Practices Implemented

1. **Page Object Model** - Clean separation of concerns
2. **Explicit Waits** - Reliable synchronization
3. **DRY Principle** - Reusable helper methods
4. **Clear Naming** - Self-documenting code
5. **Error Handling** - Graceful failure management
6. **Screenshot Capture** - Visual failure debugging
7. **Modular Design** - Easy to extend and maintain
8. **Assertions** - Comprehensive validations

## 🐛 Troubleshooting

### Common Issues

**Issue:** ChromeDriver version mismatch
**Solution:** WebDriverManager handles this automatically

**Issue:** Element not found
**Solution:** Check if page fully loaded, increase wait time

**Issue:** Stale element reference
**Solution:** BasePage handles stale elements with retry logic

**Issue:** Tests failing in sequence
**Solution:** Ensure Test 1 runs first to create account


## 📄 License

This project is created for educational and testing purposes.

---

**Generated with Selenium + Java + JUnit 5**
