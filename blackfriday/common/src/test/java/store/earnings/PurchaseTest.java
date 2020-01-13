package store.earnings;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class PurchaseTest {

    Purchase purchase;
    private String productName = "productName";
    private int quantity = 10;
    private double price = 2;
    private String username = "username";
    private LocalDateTime date = LocalDateTime.now();

    @Before
    public void setup() {
        this.purchase = new Purchase(productName, username, quantity, price, date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenProductNameIsNull() {
        Purchase purchase = new Purchase(null, this.username, this.quantity, this.price, this.date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUsernameIsNull() {
        Purchase purchase = new Purchase(this.productName, null, this.quantity, this.price, this.date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenQuantityIsNegative() {
        Purchase purchase = new Purchase(this.productName, this.username, -1, this.price, this.date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenQuantityIsZero() {
        Purchase purchase = new Purchase(this.productName, this.username, 0, this.price, this.date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPriceIsNegative() {
        Purchase purchase = new Purchase(this.productName, this.username, this.quantity, -1, this.date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPriceIsZero() {
        Purchase purchase = new Purchase(this.productName, this.username, this.quantity, 0, this.date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDateIsNull() {
        Purchase purchase = new Purchase(this.productName, this.username, this.quantity, this.price, null);
    }

    @Test
    public void getProductNameShouldReturnCorrectValue() {
        assertEquals(this.productName, this.purchase.getProductName());
    }

    @Test
    public void getUsernameShouldReturnCorrectValue() {
        assertEquals(this.username, this.purchase.getUsername());
    }

    @Test
    public void getQuantityShouldReturnCorrectValue() {
        assertEquals(this.quantity, this.purchase.getQuantity());
    }

    @Test
    public void getPriceShouldReturnCorrectValue() {
        assertEquals(this.price, this.purchase.getPrice(), 0);
    }

    @Test
    public void getDateShouldReturnCorrectValue() {
        assertEquals(this.date, this.purchase.getDate());
    }

    @Test
    public void getCostShouldReturnCorrectValue() {
        double totalCost = this.price * this.quantity;
        assertEquals(totalCost, this.purchase.getCost(), 0);
    }
}
