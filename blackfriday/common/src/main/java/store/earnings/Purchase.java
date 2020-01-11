package store.earnings;

import commonMessages.ExceptionMessages;

import java.io.Serializable;
import java.time.LocalDateTime;

import static validator.Validator.*;

/**
 * The Purchase class represents a given purchase
 * made from the at moment it was made
 * <p>
 * Provides the following information about a purchase
 * name of the product being bought
 * name of the user who bought the product
 * the quantity bough
 * the peirce at which it was bought
 * the date the purchase happened
 */
public class Purchase implements Serializable {

    private String productName;
    private String userName;
    private int quantity;
    private double price;
    private LocalDateTime date;

    public Purchase(String productName, String userName, int quantity, double price, LocalDateTime date) {
        this.setProductName(productName);
        this.setQuantity(quantity);
        this.setPrice(price);
        this.setUserName(userName);
        this.date = date;
    }

    public Purchase(String productName, String userName, int quantity, double price) {
        this.setProductName(productName);
        this.setQuantity(quantity);
        this.setPrice(price);
        this.setUserName(userName);
        this.date = LocalDateTime.now();
    }

    public String getProductName() {
        return this.productName;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }

    private void setProductName(String productName) {
        requireNonBlank(productName, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.productName = productName;
    }

    private void setUserName(String userName) {
        requireNonBlank(userName, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.userName = userName;
    }

    private void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void setPrice(double price) {
        validatePrice(price, 0);
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    /**
     * @return the total amount of money from the purchase
     * cost = bought quantity * the price at which the product was sold
     */
    double getCost() {
        return this.price * this.quantity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product name: ").append(this.getProductName())
                .append(System.lineSeparator())
                .append("Bought quantity: ")
                .append(this.getQuantity())
                .append(System.lineSeparator())
                .append("Price: ")
                .append(this.getPrice())
                .append(System.lineSeparator())
                .append("Total cost: ")
                .append(this.getCost())
                .append(System.lineSeparator())
                .append("Date: ")
                .append(this.getDate().toLocalDate().toString());
        return sb.toString();
    }
}