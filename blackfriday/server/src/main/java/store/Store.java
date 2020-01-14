package store;

import database.interfaces.Database;
import product.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class Store {

    private Database<Product> productDatabase;


    public Store(Database<Product> productDatabase) {
        this.productDatabase = productDatabase;
    }

    public boolean isBlackFriday() throws IOException, SQLException {
        return !this.productDatabase.read("is_discounted = 1").isEmpty();
    }

    /**
     * Sets black friday status for the store
     * <p>
     * if (@code blackFriday) is true sets all products
     * discounted status to true if their
     * discount percent is higher than 10
     * <p>
     * if (@code blackFriday) is false sets all products
     * discounted status to false
     *
     * @param blackFriday if is true
     */
    public void setBlackFriday(boolean blackFriday) throws IOException, SQLException {
        if (blackFriday) {
            List<Product> productsWithDiscountPercent = this.productDatabase.read("discounted_percent > 0");
            for (Product product : productsWithDiscountPercent) {
                this.productDatabase.update(product.getName(), "is_discounted", "1");
            }
        } else {
            List<Product> discountedProducts = this.productDatabase.read("is_discounted = true");
            for (Product discountedProduct : discountedProducts) {
                this.productDatabase.update(discountedProduct.getName(), "is_discounted", "0");
            }
        }
    }
}
