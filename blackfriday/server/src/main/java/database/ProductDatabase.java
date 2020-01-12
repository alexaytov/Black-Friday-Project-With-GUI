package database;

import database.parsers.ProductParser;
import exceptions.DataAlreadyExistsException;
import product.Product;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDatabase extends BaseDatabase<Product> {

    private static String tableName = "products";
    private static String primaryKey = "name";
    private Statement statement;
    private Connection DBConnection;

    public ProductDatabase(Connection DBConnection) throws IOException, SQLException {
        super(DBConnection, tableName, primaryKey, new ProductParser());
        this.DBConnection = DBConnection;
        this.statement = DBConnection.createStatement();
    }


    public void updateProductImage(String productName, byte[] imageContent) {
        String updateSQL = "UPDATE products SET image_content = ? WHERE name = ?";
        try (PreparedStatement pstmt = this.DBConnection.prepareStatement(updateSQL)) {

            // create input stream from byte array
            InputStream input = new ByteArrayInputStream(imageContent);

            // set parameters
            pstmt.setBinaryStream(1, input);
            pstmt.setString(2, productName);

            // store image content in the database
            pstmt.executeUpdate();

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
    public void add(Product data) throws SQLException, DataAlreadyExistsException {
        if (super.contains(data.getName())) {
            throw new DataAlreadyExistsException();
        }
        String sql = String.format(
                "INSERT INTO products(`name`, `description`, `quantity`, `price`, `minimum_price`, `discounted_percent`, `is_discounted`, `size`) " +
                        "VALUES('%s', '%s', '%d', %f, %f, %f, %s, '%s');",
                data.getName(),
                data.getDescription(),
                data.getQuantity(),
                data.getPrice(),
                data.getMinimumPrice(),
                data.getDiscountPercent(),
                String.valueOf(data.isDiscounted()),
                data.getSize()
        );
        this.statement.execute(sql);
        updateProductImage(data.getName(), data.getImageContent());
    }

}