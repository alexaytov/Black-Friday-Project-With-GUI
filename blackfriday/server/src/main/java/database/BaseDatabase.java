package database;

import commonMessages.ExceptionMessages;
import database.interfaces.Database;
import database.parsers.DataParser;
import exceptions.NotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class BaseDatabase<T> implements Database<T> {

    private String tableName;
    private String primaryKey;
    private Statement statement;
    private DataParser<T> parser;

    BaseDatabase(Connection dbConnection, String tableName, String primaryKey, DataParser<T> parser) throws SQLException {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.statement = dbConnection.createStatement();
        this.parser = parser;
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
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", this.tableName, this.primaryKey, name);
        ResultSet resultSet = this.statement.executeQuery(sql);
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
        if (constraints.length != 0) {
            sql = String.format("SELECT * FROM %s WHERE ", this.tableName) + String.join(" && ", constraints);
        } else {
            sql = String.format("SELECT * FROM %s", this.tableName);
        }
        ResultSet resultSet = statement.executeQuery(sql);
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
        String sql = String.format("DELETE FROM %s WHERE %s = '%s';", this.tableName, this.primaryKey, name);
        this.statement.execute(sql);
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
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = '%s'", this.tableName, this.primaryKey, name);
        ResultSet resultSet = this.statement.executeQuery(sql);
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
        String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                this.tableName,
                variableName,
                newValue,
                this.primaryKey,
                primaryKeyValue);
        this.statement.execute(sql);
    }


}
