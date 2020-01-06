package product;

import commonMessages.ExceptionMessages;
import exceptions.NotEnoughQuantityException;
import product.interfaces.Buyable;
import product.interfaces.Promotional;
import user.interfaces.User;
import validator.Validator;

import java.io.Serializable;
import java.util.Objects;

import static validator.Validator.*;

public class Product implements Buyable, Promotional, Serializable, Cloneable {

    private String name;
    private String description;
    private int quantity;
    private double price;
    private double discountPercent;
    private boolean discounted;
    private double minimumPrice;
    private String size;
    private byte[] imageContent;

    public Product(String name, String description, int quantity, double price, double minimumPrice, double discountPercent, byte[] imageContent, String size) {
        this(name, description, quantity, price, minimumPrice, discountPercent, imageContent);
        this.setSize(size);
    }

    public Product(String name, String description, int quantity, double price, double minimumPrice, double discountPercent, byte[] imageContent) {
        this.setName(name);
        this.setDescription(description);
        this.setQuantity(quantity);
        this.setPrice(price);
        this.setMinimumPrice(minimumPrice);
        this.setSize(null);
        this.setImageContent(imageContent);
        this.setDiscountPercent(discountPercent);

    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(double minimumPrice) {
        Validator.requireNonNegative(minimumPrice, ExceptionMessages.MINIMUM_PRICE_MUST_BE_POSITIVE);
        this.minimumPrice = minimumPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validator.requireNonBlank(name, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.name = name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public String getSize() {
        return this.size;
    }

    //size CAN be null that means the certain product has no size
    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        Validator.requireNonBlank(description, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.description = description;
    }


    @Override
    public boolean isDiscounted() {
        return this.discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    /**
     * @return product price or discounted
     * price based on isDiscounted()
     */
    @Override
    public double getPrice() {
        if (this.isDiscounted()) {
            return this.getDiscountedPrice();
        } else {
            return this.price;
        }
    }

    @Override
    public void setPrice(double price) {
        validatePrice(price, this.minimumPrice);
        this.price = price;
    }

    /**
     * @param user     who is going to purchase the product
     * @param quantity the user is going to purchase
     * @throws NotEnoughQuantityException if quantity is bigger than product quantity
     */
    @Override
    public void buy(User user, int quantity) throws NotEnoughQuantityException {
        if (quantity > this.getQuantity()) {
            throw new NotEnoughQuantityException(String.format(ExceptionMessages.NOT_ENOUGH_QUANTITY, this.getQuantity(), this.getName(), quantity));
        }
        this.setQuantity(this.getQuantity() - quantity);
    }

    /**
     * @return discounted price
     * based on the discount percent
     */
    public double getDiscountedPrice() {
        return this.price * (1 - this.discountPercent / 100);
    }

    public double getPurchasePrice() {
        if (this.isDiscounted()) {
            return this.getDiscountedPrice();
        }
        return this.price;
    }

    @Override
    public double getDiscountPercent() {
        return this.discountPercent;
    }

    /**
     * if the discountPercent is 0 the promotional
     * status of the product becomes false and
     * if the is valid the promotional status becomes true
     */
    @Override
    public void setDiscountPercent(double discountPercent) {
        validatePromotionalPricePercent(discountPercent, this.price, this.minimumPrice);
        this.discountPercent = discountPercent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + this.getName())
                .append(System.lineSeparator())
                .append("Description: ")
                .append(this.getDescription())
                .append(System.lineSeparator())
                .append("Quantity: ")
                .append(this.getQuantity())
                .append(System.lineSeparator());
        if (this.getSize() != null) {
            sb
                    .append("Size: ")
                    .append(this.getSize())
                    .append(System.lineSeparator());
        }
        if (this.isDiscounted()) {
            sb.append(String.format("Old price: %.2f", this.getPrice()))
                    .append(System.lineSeparator())
                    .append(String.format("New price: %.2f", this.getDiscountedPrice()));
        } else {
            sb.append(String.format("Price: %.2f", this.getPrice()));
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        // compares only by name
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return this.getName().equals(product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName());
    }
}
