package database.interfaces;

import exceptions.DataAlreadyExistsException;
import exceptions.NotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Database<T> {


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

    void update(String primaryKeyValue, String variableName, String newValue) throws SQLException;

//    T read(String name);
//
//    List<T> readAll();

}
