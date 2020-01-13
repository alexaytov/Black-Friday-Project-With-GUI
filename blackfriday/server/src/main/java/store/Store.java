package store;

import commonMessages.ExceptionMessages;
import database.ProductDatabase;
import database.PurchaseDatabase;
import database.UserDatabase;
import exceptions.DataAlreadyExistsException;
import exceptions.NotEnoughQuantityException;
import exceptions.NotFoundException;
import exceptions.ProductAlreadyExistsException;
import product.Product;
import store.earnings.Earnings;
import store.earnings.Purchase;
import user.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static validator.Validator.requireNonBlank;
import static validator.Validator.requireNonNull;


public class Store {

    private UserDatabase userDatabase;
    private ProductDatabase productDatabase;
    private PurchaseDatabase purchaseDatabase;
    private Earnings earnings;
    private User loggedInUser;
    private Product chosenProduct;


    public Store(Connection DBConnection) throws IOException, SQLException {
        this.userDatabase = new UserDatabase(DBConnection);
        this.productDatabase = new ProductDatabase(DBConnection);
        this.purchaseDatabase = new PurchaseDatabase(DBConnection);
        this.earnings = new Earnings(this.purchaseDatabase);
        this.loggedInUser = null;
        this.chosenProduct = null;
    }

    public void setChosenProduct(Product chosenProduct) {
        this.chosenProduct = chosenProduct;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getUser(String username) throws IOException, SQLException, NotFoundException {
        return this.userDatabase.getByName(username);
    }

    /**
     * @param user the new user to be created
     * @throws DataAlreadyExistsException if a user with the same username
     *                                    already exists in the database
     */
    public void registerUser(User user) throws DataAlreadyExistsException, SQLException {
        this.userDatabase.add(user);
    }

    /**
     * @param username the username of the user to be deleted
     * @throws NotFoundException if the user is not in the database
     */
    public void deleteUser(String username) throws NotFoundException, SQLException {
        this.userDatabase.delete(username);
    }

    /**
     * @return all of the client purchases
     */
    public Map<String, List<Purchase>> getClientPurchases() throws IOException, SQLException {
        List<Purchase> purchases = this.purchaseDatabase.read("quantity != 0");
        HashMap<String, List<Purchase>> purchaseMap = new HashMap<>();
        for (Purchase purchase : purchases) {
            if (purchaseMap.containsKey(purchase.getUserName())) {
                purchaseMap.get(purchase.getUserName()).add(purchase);
            } else {
                purchaseMap.put(purchase.getUserName(), new ArrayList<>());
                purchaseMap.get(purchase.getUserName()).add(purchase);
            }
        }
        return purchaseMap;
    }

    /**
     * Changes user first name
     *
     * @param username  the username of the user object to be modified
     * @param firstName the new first name to be set to the user
     * @return if the user first name change was successful
     */
    public boolean changeUserFirstName(String username, String firstName) throws SQLException {
        try {
            requireNonBlank(firstName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            this.userDatabase.update(username, "first_name", firstName);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    /**
     * Changes user last name
     *
     * @param username the username of the user object to be modified
     * @param lastName the new last name to be set to the user
     * @return if the new last name was set successfully
     */
    public boolean changeUserLastName(String username, String lastName) throws SQLException {
        try {
            requireNonBlank(lastName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            this.userDatabase.update(username, "last_name", lastName);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Changes user's username
     *
     * @param username    the username of the user object to be modified
     * @param newUsername the new username to be set to the user
     * @return if the new username was set successfully
     */
    public boolean changeUsername(String username, String newUsername) throws DataAlreadyExistsException, SQLException {
        if (this.userDatabase.contains(newUsername)) {
            throw new DataAlreadyExistsException();
        }
        try {
            requireNonBlank(newUsername, ExceptionMessages.NAME_NULL_OR_EMPTY);
            this.userDatabase.update(username, "username", newUsername);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        this.getLoggedInUser().setUsername(newUsername);
        return true;
    }

    /**
     * Changes user password
     *
     * @param username    the username of the user object to be modified
     * @param newPassword the new password to be set to the user
     * @return if the password was successfully changed
     */
    public boolean changePassword(String username, String newPassword) throws SQLException {
        try {
            requireNonBlank(newPassword, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
            this.userDatabase.update(username, "password", newPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Changes age of user
     *
     * @param username the username of the user object to be modified
     * @param newAge   the new age to be set to the user
     * @return if the new age is set successful
     */
    public boolean changeAge(String username, int newAge) throws SQLException {
        try {
            requireNonNull(newAge, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
            this.userDatabase.update(username, "age", String.valueOf(newAge));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
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

    /**
     * Adds the given product to the product database
     *
     * @param product object to be added
     */
    public void addProduct(Product product) throws SQLException, DataAlreadyExistsException {
        this.productDatabase.add(product);
    }

    /**
     * Deleted given product from the product database
     *
     * @param productName name of the product
     * @throws NotFoundException if the product isn't found in the database
     */
    public void deleteProduct(String productName) throws NotFoundException, SQLException {
        this.productDatabase.delete(productName);
    }

    /**
     * @return all promotional product names
     * which have quantity higher than 10
     */
    public List<Product> getClientDiscountedProducts() throws IOException, SQLException {
        return this.productDatabase.read("quantity != 0", "is_discounted = true");
    }

    /**
     * @return all the products which are discounted
     */
    public List<Product> getStaffDiscountedProducts() throws IOException, SQLException {
        return this.productDatabase.read("discounted_percent != 0");
    }

    public Product getProductByName(String name) throws NotFoundException, IOException, SQLException {
        this.chosenProduct = this.productDatabase.getByName(name);
        return this.chosenProduct;
    }

    /**
     * Finds product by name and
     * executes Product buy method
     *
     * @param productName the product's name which is being bought
     * @param user        person who is buying the product
     * @param quantity    the number the product is being bought
     * @return if the purchase was successful
     * @throws NotFoundException if the product is not found
     */
    public boolean buyProduct(String productName, User user, int quantity) throws NotFoundException, NotEnoughQuantityException, IOException, SQLException {
        Product product = this.productDatabase.getByName(productName);
        product.buy(user, quantity);
        this.productDatabase.update(productName, "quantity", String.valueOf(product.getQuantity()));
        Purchase purchase = new Purchase(productName, user.getUsername(), quantity, product.getPrice());
        earnings.logPurchase(purchase);
        return true;
    }

    /**
     * Changes chosen product's name
     *
     * @param newProductName the new product name
     * @throws ProductAlreadyExistsException if a product with the same name already exists
     * @throws SQLException                  if a SQL error occurs
     */
    public void changeProductName(String newProductName) throws ProductAlreadyExistsException, SQLException {
        if (this.productDatabase.contains(newProductName)) {
            throw new ProductAlreadyExistsException(String.format(ExceptionMessages.PRODUCT_ALREADY_EXISTS, newProductName));
        }
        this.productDatabase.update(this.chosenProduct.getName(), "name", newProductName);
    }

    /**
     * Changes chosen product's description
     *
     * @param newDescription the new description
     * @throws SQLException if a SQL error occurs
     */
    public void changeProductDescription(String newDescription) throws SQLException {
        this.productDatabase.update(this.chosenProduct.getName(), "description", newDescription);
    }

    /**
     * Changes chosen product discount percent
     *
     * @param newDiscountPercent the new discount percent
     * @throws SQLException if a SQL exception occurs
     */
    public void changeProductDiscountPercent(double newDiscountPercent) throws SQLException {
        this.chosenProduct.setDiscountPercent(newDiscountPercent);
        this.productDatabase.update(this.chosenProduct.getName(), "discounted_percent", String.valueOf(newDiscountPercent));
    }

    /**
     * Changes chosen product size
     *
     * @param newSize the new size of the product
     * @throws SQLException if a SQL error occurs
     */
    public void changeProductSize(String newSize) throws SQLException {
        this.productDatabase.update(this.chosenProduct.getName(), "size", newSize);
    }

    /**
     * Changes chosen product quantity
     *
     * @param quantity the new quantity of the product
     * @throws SQLException if a SQL error occurs
     */
    public void changeProductQuantity(int quantity) throws SQLException {
        this.productDatabase.update(this.chosenProduct.getName(), "quantity", String.valueOf(quantity));
    }

    /**
     * Return the earnings for a specified year
     *
     * @param year the chosen year for the earnings
     * @return the earnings in a specific year
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public double getEarnings(int year) throws IOException, SQLException {
        return earnings.getEarnings(year);
    }

    /**
     * Return the earnings for a specified month and year
     *
     * @param month the specified month
     * @param year  the specified year
     * @return Earnings from the specified month in the chosen year
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public double getEarnings(int month, int year) throws IOException, SQLException {
        return earnings.getEarnings(month, year);
    }

    /**
     * Return the earnings between the start date and end date
     *
     * @param startDate the start data
     * @param endDate   the end date
     * @return all earnings that happened between the start and end date
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public double getEarnings(LocalDate startDate, LocalDate endDate) throws IOException, SQLException {
        return earnings.getEarnings(startDate, endDate);
    }

    /**
     * Return the earnings for a specific date
     *
     * @param date the date
     * @return all earnings that happened on the specified date
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public double getEarnings(LocalDate date) throws IOException, SQLException {
        return earnings.getEarnings(date);
    }

    /**
     * @return Returns all products in the product database
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public List<Product> getStaffProducts() throws IOException, SQLException {
        return this.productDatabase.read();
    }

    /**
     * @return All product in product database with quantity bigger than zero
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public List<Product> getClientProducts() throws IOException, SQLException {
        return this.productDatabase.read("quantity > 0");
    }

    /**
     * Changes chosen product price
     *
     * @param newPrice the new price
     * @throws SQLException if SQL error occurs
     */
    public void changeProductPrice(double newPrice) throws SQLException {
        this.chosenProduct.setPrice(newPrice);
        this.productDatabase.update(this.chosenProduct.getName(), "price", String.valueOf(newPrice));
    }

    /**
     * Changes the chosen product image
     *
     * @param newImageContent the new image
     */
    public void changeProductImage(byte[] newImageContent) {
        this.productDatabase.updateProductImage(this.chosenProduct.getName(), newImageContent);
    }

    /**
     * Searches all product by name
     *
     * @param searchedAllProductsName the name for the search
     * @return all the products with name that contain the (@code searchedAllProductsName)
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public List<Product> searchAllProducts(String searchedAllProductsName) throws IOException, SQLException {
        return this.productDatabase.read("name LIKE '%" + searchedAllProductsName + "%'");
    }

    /**
     * Searches discounted products by name
     *
     * @param searchedDiscountedProductsName the name for the search
     * @return all discounted products with name that contains (@code searchedDiscountedProductsName)
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public List<Product> searchDiscountedProducts(String searchedDiscountedProductsName) throws IOException, SQLException {
        return this.productDatabase.read("is_discounted = true", "name LIKE '%" + searchedDiscountedProductsName + "%'");
    }

    /**
     * Searches products based on quantity
     *
     * @param maximumQuantity the maximum quantity for the search not inclusive
     * @return return all products below the (@code maximumQuantity)
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    public List<Product> searchQuantityControl(int maximumQuantity) throws IOException, SQLException {
        return this.productDatabase.read("quantity < " + maximumQuantity);
    }

    /**
     * Changes the chosen product minimum price
     *
     * @param minimumPrice the new minimum price
     * @throws SQLException if SQl error occurs
     */
    public void changeProductMinimumPrice(double minimumPrice) throws SQLException {
        this.chosenProduct.setMinimumPrice(minimumPrice);
        this.productDatabase.update(this.chosenProduct.getName(), "minimum_price", String.valueOf(minimumPrice));
    }

    public Object searchDiscountedStaffProducts(String searchedDiscountedProductsName) throws IOException, SQLException {
        return this.productDatabase.read("discounted_percent != 0", "name LIKE '%" + searchedDiscountedProductsName + "%'");
    }
}
