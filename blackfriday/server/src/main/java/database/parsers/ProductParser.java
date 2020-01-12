package database.parsers;

import product.Product;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductParser implements DataParser<Product> {

    /**
     * Parses product from result set
     *
     * @param resultSet the result set with information
     * @return product object parsed from the result set data
     * @throws SQLException if SQL error occurs
     * @throws IOException  if IO error occurs
     */
    @Override
    public Product parseData(ResultSet resultSet) throws SQLException, IOException {
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        int quantity = resultSet.getInt("quantity");
        double price = resultSet.getFloat("price");
        double minimumPrice = resultSet.getFloat("minimum_price");
        double discountedPercent = resultSet.getFloat("discounted_percent");
        boolean isDiscounted = resultSet.getBoolean("is_discounted");
        String size = resultSet.getString("size");

        InputStream input = resultSet.getBinaryStream("image_content");
        byte[] imageContent = input.readAllBytes();

        Product product = new Product(name, description, quantity, price, minimumPrice, discountedPercent, imageContent, size);
        product.setDiscounted(isDiscounted);

        return product;
    }
}
