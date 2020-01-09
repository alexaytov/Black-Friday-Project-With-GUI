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


    @Override
    public void add(Purchase data) throws SQLException {
        String sql = String.format("INSERT INTO `enaleks`.`purchases` (`username`, `product_name`, `quantity`, `product_price`) VALUES ('%s', '%s', '%d', '%f');",
                data.getUserName(),
                data.getProductName(),
                data.getQuantity(),
                data.getPrice());
        this.statement.execute(sql);
    }

    @Override
    public void update( String primaryKeyValue, String variableName, String newValue) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                tableName,
                variableName,
                newValue,
                primaryKey,
                primaryKeyValue);
        this.statement.execute(sql);
    }


//    /**
//     * Adds purchase to the (@code data)
//     *
//     * @param purchase purchase to be added
//     */
//    public synchronized void write(Purchase purchase) {
//        String userName = purchase.getUserName();
//        if (this.data.containsKey(userName)) {
//            this.data.get(userName).add(purchase);
//        } else {
//            this.data.put(userName, new ArrayList<>());
//            this.data.get(userName).add(purchase);
//        }
//        this.saveAllChanges();
//    }
//
//    /**
//     * Deleted purchase from (@code data)
//     *
//     * @param purchase the purchase to be deleted
//     * @return if the purchase was removed
//     * @throws NotFoundException if the purchase wasn't found
//     */
//    public synchronized boolean delete(Purchase purchase) throws NotFoundException {
//        try {
//            boolean isRemoved = this.data.get(purchase.getUserName()).remove(purchase);
//            this.saveAllChanges();
//            return isRemoved;
//        } catch (NullPointerException ex) {
//            throw new NotFoundException();
//        }
//    }
//
//    /**
//     * @param startDate the beginning of the period
//     * @param endDate   the end of the period
//     * @return all the purchases between the (@code startDate) and (@code endDate)
//     */
//    public List<Purchase> getPurchases(LocalDate startDate, LocalDate endDate) {
//        // > start date < endDate
//        List<Purchase> validPurchases = new ArrayList<>();
//        for (List<Purchase> listOfPurchases : this.data.values()) {
//            for (Purchase purchase : listOfPurchases) {
//                LocalDate purchaseDate = purchase.getDate().toLocalDate();
//                if (purchaseDate.isAfter(startDate) && purchaseDate.isBefore(endDate)) {
//                    validPurchases.add(purchase);
//                }
//            }
//
//        }
//        return validPurchases;
//    }
//
//    /**
//     * @param year the year purchases happened
//     * @return all purchasers happened in that (@code year)
//     */
//    public List<Purchase> getPurchases(int year) {
//        requireNonNegative(year, ExceptionMessages.YEAR_MUST_BE_POSITIVE);
//        List<Purchase> validPurchases = new ArrayList<>();
//        for (List<Purchase> purchaseList : this.data.values()) {
//            for (Purchase purchase : purchaseList) {
//                if (purchase.getDate().toLocalDate().getYear() == year) {
//                    validPurchases.add(purchase);
//                }
//            }
//        }
//        return validPurchases;
//    }
//
//    public Map<String, List<Purchase>> getAllPurchases() {
//        return Collections.unmodifiableMap(this.data);
//    }
//
//    /**
//     * @param month the month in which the purchases happened
//     * @param year  the year in which the purchases happened
//     * @return all purchases that happened on (@code month)
//     */
//    public List<Purchase> getPurchases(int month, int year) {
//        validateMonth(month);
//        List<Purchase> validPurchases = new ArrayList<>();
//        for (List<Purchase> purchaseList : this.data.values()) {
//            for (Purchase purchase : purchaseList) {
//                LocalDate purchaseDate = purchase.getDate().toLocalDate();
//                if (purchaseDate.getYear() == year && purchaseDate.getMonth().getValue() == month) {
//                    validPurchases.add(purchase);
//                }
//            }
//        }
//        return validPurchases;
//    }
//
//    /**
//     * @param date the date in which the purchase happened
//     * @return purchases that happened on (@code date)
//     */
//    public List<Purchase> getPurchases(LocalDate date) {
//        List<Purchase> validPurchases = new ArrayList<>();
//        for (Map.Entry<String, List<Purchase>> listEntry : this.data.entrySet()) {
//            for (Purchase purchase : listEntry.getValue()) {
//                if (purchase.getDate().toLocalDate().equals(date)) {
//                    validPurchases.add(purchase);
//                }
//            }
//        }
//        return validPurchases;
//    }
//
//    @Override
//    public Purchase getByName(String name) throws SQLException, NotFoundException {
//        String sql = String.format("SELECT * FROM %s WHERE name = '%s'", this.tableName, username);
//        ResultSet resultSet = this.statement.executeQuery(sql);
//        if (resultSet.getFetchSize() == 0) {
//            throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, username));
//        }
//        return parseUser(resultSet);
//    }
//
//    @Override
//    public List<User> read(String... constraints) throws SQLException {
//        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE ");
//        sql.append(String.join(" && ", constraints));
//        ResultSet resultSet = statement.executeQuery(sql.toString());
//        List<User> users = new ArrayList<>();
//        while(resultSet.next()){
//            users.add(parseUser(resultSet));
//        }
//        return users;
//    }
//
//    @Override
//    public synchronized void add(User user) throws SQLException, DataAlreadyExistsException {
//        if (this.contains(user.getUsername())) {
//            throw new DataAlreadyExistsException();
//        }
//        String sql = String.format(
//                "INSERT INTO `enaleks`.`users` (`username`, `password`, `permission`, `first_name`, `last_name`, `age`, `date_of_registration`) VALUES ('%s', '%s', '%s', '%s', '%s', '%d', '%s');",
//                user.getUsername(),
//                user.getPassword(),
//                user.getPermission().toString().toLowerCase(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getAge(),
//                user.getDateOfCreation().toString());
//        this.statement.execute(sql);
//    }
//
//    @Override
//    public synchronized void delete(String username) throws NotFoundException, SQLException {
//        String sql = String.format("DELETE FROM users WHERE username = '%s';", username);
//        if (!this.statement.execute(sql)) {
//            throw new NotFoundException();
//        }
//    }
//
//    @Override
//    public boolean contains(String username) throws SQLException {
//        String sql = String.format("SELECT COUNT(*) FROM users WHERE username = '%s'", username);
//        ResultSet resultSet = this.statement.executeQuery(sql);
//        resultSet.next();
//        return resultSet.getInt(1) == 1;
//    }
//
}
