package store.earnings;

import database.PurchaseDatabase;
import exceptions.DataAlreadyExistsException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Earnings {

    private PurchaseDatabase purchaseDatabase;

    public Earnings(PurchaseDatabase purchaseDatabase) {
        this.purchaseDatabase = purchaseDatabase;
    }

    /**
     * Adds a purchase to the purchase database
     *
     * @param purchase the purchase added to the purchaseDatabase
     */
    public void logPurchase(Purchase purchase) throws SQLException{
        this.purchaseDatabase.add(purchase);
    }

    /**
     * @param year the year which we want to get all earnings from
     * @return all the earnings which happened in that year
     */
    public double getEarnings(int year) throws IOException, SQLException {
        List<Purchase> purchases = this.purchaseDatabase.read("year == " + year);
        return purchases.stream()
                .mapToDouble(Purchase::getCost)
                .sum();

    }

    /**
     * @param month the month in which the earnings were made
     * @param year  the year in which the earnings were made
     * @return total earnings for the specified month and year
     */
    public double getEarnings(int month, int year) throws IOException, SQLException {
        // TODO FIX purchase date
        List<Purchase> purchases = this.purchaseDatabase.read("pruchase_date == " + month, "year == " + year);
        return purchases.stream()
                .mapToDouble(Purchase::getCost)
                .sum();
    }

    /**
     * @param startDate the beginning of the period in which the earnings were made
     * @param endDate   the end of the period in which the earnings were made
     * @return all earnings made between the startDate and endDate exclusive
     */
    public double getEarnings(LocalDate startDate, LocalDate endDate) throws IOException, SQLException {
        List<Purchase> purchases = this.purchaseDatabase.read("purchase_date > " + startDate, "purchase_date <" + endDate);
        return purchases.stream()
                .mapToDouble(Purchase::getCost)
                .sum();
    }

    /**
     * @param date the date in which the earnings were made
     * @return all earnings made on the given date
     */
    public double getEarnings(LocalDate date) throws IOException, SQLException {
        List<Purchase> purchases = this.purchaseDatabase.read("purchase_date == " + date);
        return purchases.stream()
                .mapToDouble(Purchase::getCost)
                .sum();

    }
}
