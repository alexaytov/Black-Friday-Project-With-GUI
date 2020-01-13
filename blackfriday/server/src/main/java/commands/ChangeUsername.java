package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.DataAlreadyExistsException;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeUsername implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private Store store;

    /**
     * Changes logged in user's username
     * and sends thorough client connection if
     * change was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        boolean isUsernameChangeSuccessful;
        try {
            isUsernameChangeSuccessful = this.store.changeUsername(this.store.getLoggedInUser().getUsername(), this.clientConnection.read());
        } catch (DataAlreadyExistsException e) {
            isUsernameChangeSuccessful = false;
        }
        this.clientConnection.write(isUsernameChangeSuccessful);
    }
}
