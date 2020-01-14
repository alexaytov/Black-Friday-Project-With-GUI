package store.services;

import commonMessages.ExceptionMessages;
import database.ProductDatabase;
import exceptions.DataAlreadyExistsException;
import exceptions.NotEnoughQuantityException;
import exceptions.NotFoundException;
import exceptions.ProductAlreadyExistsException;
import product.Product;
import store.earnings.Purchase;
import user.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private Product chosenProduct;
    private ProductDatabase productDatabase;

    public ProductService(ProductDatabase productDatabase) {
        this.productDatabase = productDatabase;
    }

    public void setChosenProduct(Product chosenProduct) {
        this.chosenProduct = chosenProduct;
    }


    public Object searchDiscountedStaffProducts(String searchedDiscountedProductsName) throws IOException, SQLException {
        return this.productDatabase.read("discounted_percent != 0", "name LIKE '%" + searchedDiscountedProductsName + "%'");
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
    public Purchase buyProduct(String productName, User user, int quantity) throws NotFoundException, NotEnoughQuantityException, IOException, SQLException {
        Product product = this.productDatabase.getByName(productName);
        product.buy(quantity);
        this.productDatabase.update(productName, "quantity", String.valueOf(product.getQuantity()));
        return new Purchase(productName, user.getUsername(), quantity, product.getPrice());
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
}
