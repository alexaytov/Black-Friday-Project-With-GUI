package database.interfaces;

import exceptions.DataAlreadyExistsException;
import exceptions.NotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Database<T> {

    /**
     * Read data by given name (primary key)
     *
     * @param name the name
     * @return object with name equal to (@code name)
     * @throws SQLException      if SQL error occurs
     * @throws NotFoundException if object with (@code name) doesn't exists
     * @throws IOException       if IO error occurs
     */
    T getByName(String name) throws SQLException, NotFoundException, IOException;

    List<T> read(String... constraints) throws SQLException, IOException;

    /**
     * Adds user to the database
     *
     * @param data the data to be added
     */
    void add(T data) throws SQLException, DataAlreadyExistsException;

    /**
     * Deletes user from database
     *
     * @param name the name of the data being deleted
     * @throws NotFoundException if the user is not found
     */
    void delete(String name) throws NotFoundException, SQLException;

    /**
     * Checks if the (@code data) is in the database
     *
     * @param name the data to be checked
     * @return if the user is contained
     */
    boolean contains(String name) throws SQLException;

    /**
     * Updates database
     *
     * @param primaryKeyValue the primary key name
     * @param variableName    the variable name to be changed
     * @param newValue        the new value for the variable
     * @throws SQLException if SQl error occurs
     */
    void update(String primaryKeyValue, String variableName, String newValue) throws SQLException;
}
