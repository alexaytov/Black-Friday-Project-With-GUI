package database;

import commonMessages.ExceptionMessages;
import database.interfaces.Database;
import database.parsers.DataParser;
import exceptions.NotFoundException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class BaseDatabase<T> implements Database<T> {

    private String tableName;
    private String primaryKey;
    private Statement statement;
    private DataParser<T> parser;
    private final Connection DBConnection;

    BaseDatabase(Connection DBConnection, String tableName, String primaryKey, DataParser<T> parser) throws SQLException {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.statement = DBConnection.createStatement();
        this.parser = parser;
        this.DBConnection = DBConnection;
    }

    protected Connection getDBConnection() {
        return DBConnection;
    }

    /**
     * Searches and return object by name in the database
     *
     * @param name the name of the serached object
     * @return the object with the given name
     * @throws SQLException      if SQL error occurs
     * @throws NotFoundException if an object with the name isn't found
     * @throws IOException       if IO error occurs
     */
    @Override
    public synchronized T getByName(String name) throws SQLException, NotFoundException, IOException {
        PreparedStatement preparedStatement = DBConnection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", this.tableName, this.primaryKey));
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return parser.parseData(resultSet);
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * Reads all records from database if no constrains are set, otherwise filters results based on constrains
     *
     * @param constraints the constrains for the search in SQL language
     * @return all records if no constraints or filtered records based on constrains
     * @throws SQLException if SQL error occurs
     * @throws IOException  if IO error occurs
     */
    @Override
    public synchronized List<T> read(String... constraints) throws SQLException, IOException {
        requireNonNull(constraints);
        List<T> data = new ArrayList<>();
        String sql;
        PreparedStatement preparedStatement = null;
        if (constraints.length != 0) {
            sql = String.format("SELECT * FROM %s WHERE", this.tableName);
            if (constraints[0].contains("BETWEEN")) {
                //parse for sql with between with dates statement
                String[] tokens = constraints[0].split("\\s+");
                sql += String.format(" %s %s ? %s ?", tokens[0], tokens[1], tokens[3]);
                preparedStatement = DBConnection.prepareStatement(sql);
                preparedStatement.setString(1, tokens[2]);
                preparedStatement.setString(2, tokens[4]);
            } else {
                // setup sql command for prepared statement
                for (int i = 0; i < constraints.length; i++) {
                    String[] tokens = constraints[i].split("\\s+");
                    sql += String.format(" %s %s ?", tokens[0], tokens[1]);
                    if (i != constraints.length - 1) {
                        sql += " AND ";
                    }
                    preparedStatement = DBConnection.prepareStatement(sql);
                }
                // set prepared statement parameters
                for (int i = 1; i <= constraints.length; i++) {
                    String[] tokens = constraints[i - 1].split("\\s+");
                    String wantedValue = tokens[2].replaceAll("\'", "");
                    if(wantedValue.equals("true")){
                        preparedStatement.setBoolean(i, true);
                    }else if(wantedValue.equals("false")){
                        preparedStatement.setBoolean(i, false);
                    }else{
                        preparedStatement.setString(i, wantedValue);
                    }
                }
            }
        } else {
            sql = String.format("SELECT * FROM %s", this.tableName);
            preparedStatement = DBConnection.prepareStatement(sql);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            data.add(parser.parseData(resultSet));
        }
        return data;
    }

    /**
     * Deletes record in database based on given name
     *
     * @param name the name of the data being deleted
     * @throws NotFoundException if the record is not found
     * @throws SQLException      if SQL error occurs
     */
    @Override
    public synchronized void delete(String name) throws NotFoundException, SQLException {
        if (!this.contains(name)) {
            throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, name));
        }
        PreparedStatement preparedStatement = DBConnection.prepareStatement(String.format("DELETE FROM %s WHERE %s = ?;", this.tableName, this.primaryKey));
        preparedStatement.setString(1, name);
        preparedStatement.execute();
    }

    /**
     * Checks if a record with the given name exists in the database
     *
     * @param name the data to be checked
     * @return whether the record exists in the database
     * @throws SQLException if SQL error occurs
     */
    @Override
    public boolean contains(String name) throws SQLException {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", this.tableName, this.primaryKey);
        PreparedStatement preparedStatement = DBConnection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) >= 1;
    }

    /**
     * Updates the database
     *
     * @param primaryKeyValue the primary key based on the database
     * @param variableName    the variable name to be changed
     * @param newValue        the new value for the variable
     * @throws SQLException if SQl error occurs
     */
    @Override
    public synchronized void update(String primaryKeyValue, String variableName, String newValue) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?",
                this.tableName,
                variableName,
                this.primaryKey);
        PreparedStatement preparedStatement = DBConnection.prepareStatement(sql);
        preparedStatement.setString(1, newValue);
        preparedStatement.setString(2, primaryKeyValue);
        preparedStatement.execute();
    }


}
