package database;

import database.parsers.PurchaseParser;
import store.earnings.Purchase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class PurchaseDatabase extends BaseDatabase<Purchase> {

    private static String tableName = "purchases";
    private static String primaryKey = "name";
    private Statement statement;

    public PurchaseDatabase(Connection DBConnection) throws SQLException {
        super(DBConnection, tableName, primaryKey, new PurchaseParser());
        this.statement = DBConnection.createStatement();
    }


    /**
     * Adds the purchase to the database
     *
     * @param data the data to be added
     * @throws SQLException if SQL error occurs
     */
    @Override
    public synchronized void add(Purchase data) throws SQLException {
        String sql = String.format("INSERT INTO `enaleks`.`purchases` (`username`, `product_name`, `quantity`, `product_price`, `purchase_date`) VALUES ('%s', '%s', '%d', '%f', '%s');",
                data.getUserName(),
                data.getProductName(),
                data.getQuantity(),
                data.getPrice(),
                data.getDate().toString());
        this.statement.execute(sql);
    }
}
