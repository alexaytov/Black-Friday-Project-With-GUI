package database.parsers;

import store.earnings.Purchase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PurchaseParser implements DataParser<Purchase> {

    /**
     * Parses purchase from result set
     *
     * @param resultSet the result set with information
     * @return purchase object parsed from the result set
     * @throws SQLException if SQL error occurs
     */
    @Override
    public Purchase parseData(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString("username");
        String productName = resultSet.getString("product_name");
        int quantity = resultSet.getInt("quantity");
        double productPrice = resultSet.getFloat("product_price");
        String purchaseDateAsString = resultSet.getString("purchase_date");
        purchaseDateAsString = purchaseDateAsString.replace(' ', 'T');
        LocalDateTime purchaseDate = LocalDateTime.parse(purchaseDateAsString);

        return new Purchase(productName, username, quantity, productPrice, purchaseDate);
    }
}
