package database;

import database.parsers.UserParser;
import exceptions.DataAlreadyExistsException;
import user.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDatabase extends BaseDatabase<User> {

    private static String tableName = "users";
    private static String primaryKey = "username";
    private Statement statement;

    public UserDatabase(Connection DBConnection) throws IOException, SQLException {
        super(DBConnection, tableName, primaryKey, new UserParser());
        statement = DBConnection.createStatement();
    }

    @Override
    public synchronized void add(User user) throws SQLException, DataAlreadyExistsException {
        if (this.contains(user.getUsername())) {
            throw new DataAlreadyExistsException();
        }
        String sql = String.format(
                "INSERT INTO `enaleks`.`users` (`username`, `password`, `permission`, `first_name`, `last_name`, `age`, `date_of_registration`) VALUES ('%s', '%s', '%s', '%s', '%s', '%d', '%s');",
                user.getUsername(),
                user.getPassword(),
                user.getPermission().toString().toLowerCase(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getDateOfCreation().toString());
        this.statement.execute(sql);
    }
}
