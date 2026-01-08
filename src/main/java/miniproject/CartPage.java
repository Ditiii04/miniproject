package miniproject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    private static final By cartRowsBy =
            By.cssSelector("table#shopping-cart-table tbody tr");
    private static final By grandTotalBy =
            By.cssSelector("#shopping-cart-totals-table tfoot tr.last .price");

    public CartPage(WebDriver webDriver) {
        super(webDriver);
    }

    private double parsePrice(String txt) {
        String clean = txt.replace("$", "").trim();
        return Double.parseDouble(clean);
    }

    public List<Double> getRowSubtotals() {
        List<Double> subtotals = new ArrayList<>();
        List<WebElement> rows = webDriver.findElements(cartRowsBy);
        for (WebElement row : rows) {
            WebElement priceEl = row.findElement(By.cssSelector("td.a-right.last .price"));
            subtotals.add(parsePrice(priceEl.getText()));
        }
        return subtotals;
    }

    public double getGrandTotal() {
        String txt = webDriver.findElement(grandTotalBy).getText();
        return parsePrice(txt);
    }

    public void setQuantityOfFirstRow(int qty) {
        WebElement firstRow = webDriver.findElements(cartRowsBy).get(0);
        WebElement qtyInput = firstRow.findElement(By.cssSelector("input.qty"));
        qtyInput.clear();
        qtyInput.sendKeys(String.valueOf(qty));
    }

    public void clickUpdateCart() {
        slowClick(By.cssSelector("button[title='Update Shopping Cart']"), 500);
    }
}
