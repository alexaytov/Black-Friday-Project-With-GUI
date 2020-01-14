package store.services;

import commonMessages.ExceptionMessages;
import database.interfaces.Database;
import exceptions.DataAlreadyExistsException;
import exceptions.NotFoundException;
import user.User;

import java.io.IOException;
import java.sql.SQLException;

import static validator.Validator.requireNonBlank;
import static validator.Validator.requireNonNull;

public class UserService {

    private Database<User> userDatabase;
    private User loggedInUser;

    public UserService(Database<User> userDatabase) {
        this.userDatabase = userDatabase;
        this.loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getUser(String username) throws IOException, SQLException, NotFoundException {
        return this.userDatabase.getByName(username);
    }

    /**
     * @param user the new user to be created
     * @throws DataAlreadyExistsException if a user with the same username
     *                                    already exists in the database
     */
    public void registerUser(User user) throws DataAlreadyExistsException, SQLException {
        this.userDatabase.add(user);
    }

    /**
     * @param username the username of the user to be deleted
     * @throws NotFoundException if the user is not in the database
     */
    public void deleteUser(String username) throws NotFoundException, SQLException {
        this.userDatabase.delete(username);
    }

    /**
     * Changes user first name
     *
     * @param username  the username of the user object to be modified
     * @param firstName the new first name to be set to the user
     * @return if the user first name change was successful
     */
    public boolean changeUserFirstName(String username, String firstName) throws SQLException {
        try {
            requireNonBlank(firstName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            this.userDatabase.update(username, "first_name", firstName);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    /**
     * Changes user last name
     *
     * @param username the username of the user object to be modified
     * @param lastName the new last name to be set to the user
     * @return if the new last name was set successfully
     */
    public boolean changeUserLastName(String username, String lastName) throws SQLException {
        try {
            requireNonBlank(lastName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            this.userDatabase.update(username, "last_name", lastName);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Changes user's username
     *
     * @param username    the username of the user object to be modified
     * @param newUsername the new username to be set to the user
     * @return if the new username was set successfully
     */
    public boolean changeUsername(String username, String newUsername) throws DataAlreadyExistsException, SQLException {
        if (this.userDatabase.contains(newUsername)) {
            throw new DataAlreadyExistsException();
        }
        try {
            requireNonBlank(newUsername, ExceptionMessages.NAME_NULL_OR_EMPTY);
            this.userDatabase.update(username, "username", newUsername);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        this.getLoggedInUser().setUsername(newUsername);
        return true;
    }

    /**
     * Changes user password
     *
     * @param username    the username of the user object to be modified
     * @param newPassword the new password to be set to the user
     * @return if the password was successfully changed
     */
    public boolean changePassword(String username, String newPassword) throws SQLException {
        try {
            requireNonBlank(newPassword, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
            this.userDatabase.update(username, "password", newPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Changes age of user
     *
     * @param username the username of the user object to be modified
     * @param newAge   the new age to be set to the user
     * @return if the new age is set successful
     */
    public boolean changeAge(String username, int newAge) throws SQLException {
        try {
            requireNonNull(newAge, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
            this.userDatabase.update(username, "age", String.valueOf(newAge));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
