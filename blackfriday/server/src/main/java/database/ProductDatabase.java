package database;

import database.parsers.ProductParser;
import exceptions.DataAlreadyExistsException;
import product.Product;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDatabase extends BaseDatabase<Product> {

    private static String tableName = "products";
    private static String primaryKey = "name";
    private Statement statement;

    public ProductDatabase(Connection DBConnection) throws SQLException {
        super(DBConnection, tableName, primaryKey, new ProductParser());
        this.statement = DBConnection.createStatement();
    }


    public synchronized void updateProductImage(String productName, byte[] imageContent) {
        String updateSQL = "UPDATE products SET image_content = ? WHERE name = ?";
        try (PreparedStatement preparedStatement = super.getDBConnection().prepareStatement(updateSQL)) {
            // create input stream from byte array
            InputStream input = new ByteArrayInputStream(imageContent);
            // set parameters
            preparedStatement.setBinaryStream(1, input);
            preparedStatement.setString(2, productName);
            // store image content in the database
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds new Product to the database
     *
     * @param data the data to be added
     * @throws SQLException               if SQL error occurs
     * @throws DataAlreadyExistsException if a product with the same name already exists
     */
    @Override
    public synchronized void add(Product data) throws SQLException, DataAlreadyExistsException {
        if (super.contains(data.getName())) {
            throw new DataAlreadyExistsException();
        }
        String sql =
                "INSERT INTO products(`name`, `description`, `quantity`, `price`, `minimum_price`, `discounted_percent`, `is_discounted`, `size`) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = super.getDBConnection().prepareStatement(sql);
        preparedStatement.setString(1, data.getName());
        preparedStatement.setString(2, data.getDescription());
        preparedStatement.setInt(3, data.getQuantity());
        preparedStatement.setDouble(4, data.getPrice());
        preparedStatement.setDouble(5, data.getMinimumPrice());
        preparedStatement.setDouble(6, data.getDiscountedPrice());
        preparedStatement.setInt(7, (data.isDiscounted()) ? 1 : 0);
        preparedStatement.setString(8, (data.getSize()));

        preparedStatement.execute();
        updateProductImage(data.getName(), data.getImageContent());
    }

}