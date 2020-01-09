package database;

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

    public BaseDatabase(Connection dbConnection, String tableName, String primaryKey, DataParser<T> parser) throws SQLException {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.statement = dbConnection.createStatement();
        this.parser = parser;
    }


    @Override
    public T getByName(String name) throws SQLException, NotFoundException, IOException {
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", this.tableName, this.primaryKey, name);
        ResultSet resultSet = this.statement.executeQuery(sql);
        if (resultSet.next()) {
            return parser.parseData(resultSet);
        }else{
            throw new NotFoundException();
        }
    }

    @Override
    public List<T> read(String... constraints) throws SQLException, IOException {
        requireNonNull(constraints);
        List<T> data = new ArrayList<>();
        String sql;
        if (constraints.length != 0) {
            sql = String.format("SELECT * FROM %s WHERE ", this.tableName)
                    + String.join(" && ", constraints);
        } else {
            sql = String.format("SELECT * FROM %s", this.tableName);
        }
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            data.add(parser.parseData(resultSet));
        }
        return data;

    }

    @Override
    public void delete(String name) throws NotFoundException, SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = '%s';", this.tableName, this.primaryKey, name);
        if (!this.statement.execute(sql)) {
            throw new NotFoundException();
        }
    }

    @Override
    public boolean contains(String name) throws SQLException {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = '%s'", this.tableName, this.primaryKey, name);
        ResultSet resultSet = this.statement.executeQuery(sql);
        resultSet.next();
        return resultSet.getInt(1) >= 1;
    }

    @Override
    public void update(String primaryKeyValue, String variableName, String newValue) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                this.tableName,
                variableName,
                newValue,
                this.primaryKey,
                primaryKeyValue);
        this.statement.execute(sql);
    }
}
