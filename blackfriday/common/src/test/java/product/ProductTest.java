package product;

import exceptions.NotEnoughQuantityException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductTest {

    private String name = "name";
    private String description = "description";
    private int quantity = 10;
    private double price = 10;
    private double minimumPrice = 1;
    private double discountPercent = 10;
    private byte[] imageContent = new byte[10];
    private String size = "size";

    private Product product;

    @Before
    public void setup() {
        product = new Product(name, description, quantity, price, minimumPrice, discountPercent, imageContent, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNameIsNull() {
        Product product = new Product(null, description, quantity, price, minimumPrice, discountPercent, imageContent, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDescriptionIsNull() {
        Product product = new Product(name, null, quantity, price, minimumPrice, discountPercent, imageContent, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenQuantityIsNegative() {
        Product product = new Product(name, description, -1, price, minimumPrice, discountPercent, imageContent, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenMinimumPriceIsHigherThanPrice() {
        Product product = new Product(name, description, quantity, price, price + 1, discountPercent, imageContent, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDiscountPercentIsNegative() {
        Product product = new Product(name, description, quantity, price, minimumPrice, -1, imageContent, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenImageContentIsNull() {
        Product product = new Product(name, description, quantity, price, minimumPrice, discountPercent, null, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPriceIsBelowMinimumPrice() {
        Product product = new Product(name, description, quantity, minimumPrice - 1, minimumPrice, discountPercent, imageContent, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDiscountPercentIsNumberThatDiscountedPriceIsBelowMinimumPrice() {
        Product product = new Product(name, description, quantity, price, minimumPrice, discountPercent * 100, imageContent, size);
    }

    @Test
    public void getNameShouldReturnRightValue() {
        assertEquals(this.name, this.product.getName());
    }

    @Test
    public void getDescriptionShouldReturnRightValue() {
        assertEquals(this.description, this.product.getDescription());
    }

    @Test
    public void getQuantityShouldReturnRightValue() {
        assertEquals(this.quantity, this.product.getQuantity());
    }

    @Test
    public void getPriceShouldReturnRightValue() {
        assertEquals(this.price, this.product.getPrice(), 0);
    }

    @Test
    public void getDiscountPercentShouldReturnRightValue() {
        assertEquals(this.discountPercent, this.product.getDiscountPercent(), 0);
    }

    @Test
    public void isDiscountedShouldReturnRightValue() {
        this.product.setDiscounted(true);
        assertTrue(this.product.isDiscounted());
    }

    @Test
    public void getMinimumPriceShouldReturnRightValue() {
        assertEquals(this.minimumPrice, this.product.getMinimumPrice(), 0);
    }

    @Test
    public void getSizeShouldReturnRightValue() {
        assertEquals(this.size, this.product.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNameShouldThrowExceptionWhenNameIsNull() {
        this.product.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDescriptionShouldThrowExceptionWhenDescriptionIsNull() {
        this.product.setDescription(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setQuantityShouldThrowExceptionWhenQuantityIsNegative() {
        this.product.setQuantity(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPriceShouldThrowExceptionWhenPriceIsBelowMinimumPrice() {
        this.product.setPrice(this.product.getMinimumPrice() - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDiscountPercentShouldThrowExceptionWhenDiscountPercentIsNegative() {
        this.product.setDiscountPercent(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDiscountPercentShouldThrowExceptionWhenDiscountedPriceIsBelowMinimumPrice() {
        this.product.setDiscountPercent(this.discountPercent * 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinimumPriceShouldThrowExceptionWhenMinimumPriceIsBiggerThanPrice() {
        this.product.setMinimumPrice(this.product.getPrice() + 1);
    }

    @Test
    public void getDiscountedPriceShouldReturnCorrectValue() {
        double correctDiscountedPrice = this.product.getPrice() * (1 - this.product.getDiscountPercent() / 100);
        assertEquals(correctDiscountedPrice, this.product.getDiscountedPrice(), 0);
    }

    @Test
    public void getPurchasePriceShouldReturnCorrectValue(){
        this.product.setDiscounted(true);
        assertEquals(this.product.getDiscountedPrice(), this.product.getPurchasePrice(), 0);
        this.product.setDiscounted(false);
        assertEquals(this.product.getPrice(), this.product.getPurchasePrice(), 0);
    }

    @Test(expected = NotEnoughQuantityException.class)
    public void buyShouldThrowExceptionWhenWantedQuantityIsHigherThenActualQuantity() throws NotEnoughQuantityException {
        this.product.buy(this.quantity + 1);
    }

}
