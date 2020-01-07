package store;

import commonMessages.ExceptionMessages;
import database.ProductDatabase;
import database.PurchaseDatabase;
import database.UserDatabase;
import exceptions.*;
import product.Product;
import store.earnings.Earnings;
import store.earnings.Purchase;
import user.interfaces.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static validator.Validator.validateQuantity;


public class Store {

    private UserDatabase userDatabase;
    private ProductDatabase productDatabase;
    private PurchaseDatabase purchaseDatabase;
    private Earnings earnings;
    private boolean blackFriday;


    public Store(String userDatabaseFileName, String productDatabaseFileName, String purchasesDatabaseFailName) throws IOException {
        this.userDatabase = new UserDatabase(userDatabaseFileName);
        this.productDatabase = new ProductDatabase(productDatabaseFileName);
        this.purchaseDatabase = new PurchaseDatabase(purchasesDatabaseFailName);
        this.earnings = new Earnings(this.purchaseDatabase);
        this.blackFriday = false;

    }

    /**
     * @param username the username of the client
     * @param password the password of the client
     * @return user object if the user is found and the password is correct
     * @throws NotFoundException      if the user is not in the database
     * @throws WrongPasswordException if the user exists but the passwords do not match
     */
    public User login(String username, String password) throws NotFoundException, WrongPasswordException {
        User user = this.userDatabase.getByName(username);
        if (user.getPassword().equals(password)) {
            return user;
        }
        throw new WrongPasswordException(ExceptionMessages.WRONG_PASSWORD);
    }

    /**
     * @param user the new user to be created
     * @throws UserAlreadyExistsException if a user with the same username
     *                                    already exists in the database
     */
    public void registerUser(User user) throws UserAlreadyExistsException {
        if (this.userDatabase.contains(user)) {
            throw new UserAlreadyExistsException();
        }
        this.userDatabase.write(user);
    }

    /**
     * @param username the username of the user to be deleted
     * @throws NotFoundException if the user is not in the database
     */
    public void deleteUser(String username) throws NotFoundException {
        this.userDatabase.delete(username);
    }

    /**
     * @return all of the client purchases
     */
    public Map<String, List<Purchase>> getClientPurchases() {
        return this.purchaseDatabase.getAllPurchases();
    }

    /**
     * Changes user first name
     *
     * @param user      user object to be modified
     * @param firstName the new first name to be set to the user
     * @return if the new first name was set successfully
     */
    public boolean changeUserFirstName(User user, String firstName) {
        try {
            user.setFirstName(firstName);
            this.userDatabase.saveAllChanges();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Changes user last name
     *
     * @param user     user object to be modified
     * @param lastName the new last name to be set to the user
     * @return if the new last name was set successfully
     */
    public boolean changeUserLastName(User user, String lastName) {
        try {
            user.setLastName(lastName);
            this.userDatabase.saveAllChanges();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;

    }

    /**
     * Changes user's username
     *
     * @param user        user object to be modified
     * @param newUsername the new username to be set to the user
     * @return if the new username was set successfully
     */
    public boolean changeUsername(User user, String newUsername) throws UserAlreadyExistsException {

        if (this.userDatabase.contains(newUsername)) {
            throw new UserAlreadyExistsException();
        }
        try{
            user.setUsername(newUsername);
        }catch (IllegalArgumentException ex){
            return false;
        }
        this.userDatabase.write(user);
        return true;
    }

    /**
     * Changes user password
     *
     * @param user        user object to be modified
     * @param newPassword the new password to be set to the user
     * @return if the password was successfully changed
     */
    public boolean changePassword(User user, String newPassword) {
        try {
            user.setPassword(newPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Changes age of user
     *
     * @param user   user object to be modified
     * @param newAge the new age to be set to the user
     * @return if the new age is set successful
     */
    public boolean changeAge(User user, int newAge) {
        try {
            user.setAge(newAge);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean isBlackFriday() {
        return blackFriday;
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
    public void setBlackFriday(boolean blackFriday) {
        if (blackFriday) {
            for (Map.Entry<String, Product> productEntry : this.productDatabase.getData().entrySet()) {
                if (productEntry.getValue().getDiscountPercent() != 0) {
                    productEntry.getValue().setDiscounted(true);
                }
            }
        } else {
            for (Map.Entry<String, Product> productEntry : this.productDatabase.getData().entrySet()) {
                productEntry.getValue().setDiscounted(false);
            }

        }
        this.blackFriday = blackFriday;
    }

    public ProductDatabase getProductDatabase() {
        return this.productDatabase;
    }

    /**
     * Adds the given product to the product database
     *
     * @param product object to be added
     * @throws ProductAlreadyExistsException if the product already exists
     */
    public void addProduct(Product product) throws ProductAlreadyExistsException {
        if (this.productDatabase.getData().containsKey(product.getName())) {
            throw new ProductAlreadyExistsException(String.format(ExceptionMessages.PRODUCT_ALREADY_EXISTS, product.getName()));
        }
        this.productDatabase.write(product);
    }

    /**
     * Deleted given product from the product database
     *
     * @param productName name of the product
     * @throws NotFoundException if the product isn't found in the database
     */
    public void deleteProduct(String productName) throws NotFoundException {
        this.productDatabase.delete(productName);
    }

    /**
     * @return all promotional product names
     * which have quantity higher than 10
     */
    public List<Product> getClientDiscountedProducts() {
        return this.productDatabase
                .getData().values().stream()
                .filter(product -> product.isDiscounted() && product.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    /**
     * @return all the products which are discounted
     */
    public List<Product> getStaffDiscountedProducts() {
        return this.productDatabase
                .getData().values().stream()
                .filter(product -> product.getDiscountPercent() != 0)
                .collect(Collectors.toList());
    }

    /**
     * @return all the product names from the
     * database which have quantity higher than 10
     */
    public List<String> getProductNamesForClient() {
        return this.getProductDatabase()
                .getData().values().stream()
                .filter(product -> product.getQuantity() > 0)
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    /**
     * @return all product names in the database
     */
    public List<String> getProductNamesForStaff() {
        return this.getProductDatabase()
                .getData().values().stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    public Product getProductByName(String name) throws NotFoundException {
        return this.productDatabase.getByName(name);
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
    public boolean buyProduct(String productName, User user, int quantity) throws NotFoundException, NotEnoughQuantityException {
        try {
            Product product = this.productDatabase.getByName(productName);
            product.buy(user, quantity);
            this.productDatabase.saveAllChanges();
            Purchase purchase = new Purchase(productName, user.getUsername(), quantity, product.getPrice());
            earnings.logPurchase(purchase);

        } catch (NotFoundException ex) {
            return false;
        }
        return true;
    }

    /**
     * @param quantity maximum quantity
     * @return all product names with quantity lower than (@code quantity)
     */
    public List<String> getProductNamesBelowQuantity(int quantity) {
        validateQuantity(quantity);
        return this.productDatabase.getData().values()
                .stream()
                .filter(product -> product.getQuantity() < quantity)
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    public void changeProductName(Product product, String newProductName) throws NotFoundException, ProductAlreadyExistsException {
        if (this.productDatabase.getData().containsKey(newProductName)) {
            throw new ProductAlreadyExistsException();
        }
        this.productDatabase.delete(product.getName());
        product.setName(newProductName);
        this.productDatabase.write(product);
        this.productDatabase.saveAllChanges();
    }

    public void changeProductDescription(String productName, String newDescription) throws NotFoundException {
        this.productDatabase.getData().get(productName).setDescription(newDescription);
        this.productDatabase.saveAllChanges();
    }

    public void changeProductDiscountPercent(String productName, double newDiscountPercent) throws NotFoundException {
        this.productDatabase.getData().get(productName).setDiscountPercent(newDiscountPercent);
        this.productDatabase.saveAllChanges();
    }

    public void changeProductSize(String productName, String newSize) throws NotFoundException {
        this.productDatabase.getData().get(productName).setSize(newSize);
        this.productDatabase.saveAllChanges();
    }

    public void changeProductQuantity(String productName, int quantity) throws NotFoundException {
        this.productDatabase.getData().get(productName).setQuantity(quantity);
        this.productDatabase.saveAllChanges();
    }

    public double getEarnings(int year) {
        return earnings.getEarnings(year);
    }

    public double getEarnings(int month, int year) {
        return earnings.getEarnings(month, year);
    }

    public double getEarnings(LocalDate startDate, LocalDate endDate) {
        return earnings.getEarnings(startDate, endDate);
    }

    public double getEarnings(LocalDate date) {
        return earnings.getEarnings(date);
    }


    public List<Product> getStaffProducts() {
        return new ArrayList<>(this.productDatabase.getData().values());
    }


    public List<Product> getClientProducts() {

        return this.productDatabase.getData().values()
                .stream()
                .filter(product -> product.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    public void changeProductPrice(String name, double newPrice) {
        this.productDatabase.getData().get(name).setPrice(newPrice);
    }

    public void changeProductImage(String productName, byte[] newImageContent) {
        this.productDatabase.getData().get(productName)
                .setImageContent(newImageContent);

    }

    public List<Product> searchAllProducts(String searchedAllProductsName) {
        return this.productDatabase.getData().values()
                .stream()
                .filter(product -> product.getName().toLowerCase().contains(searchedAllProductsName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Product> searchDiscountedProducts(String searchedDiscountedProductsName) {
        return this.productDatabase.getData().values()
                .stream()
                .filter(product -> product.isDiscounted() && product.getName().toLowerCase().contains(searchedDiscountedProductsName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Product> searchQuantityControl(int maximumQuantity) {
        return this.productDatabase.getData().values()
                .stream()
                .filter(product -> product.getQuantity() < maximumQuantity)
                .collect(Collectors.toList());
    }
}
