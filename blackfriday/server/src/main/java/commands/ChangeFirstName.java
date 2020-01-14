package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeFirstName implements Executable {

    private Connection clientConnection;
    private UserService userService;

    public ChangeFirstName(Connection clientConnection, UserService userService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
    }

    /**
     * Changes logged in user first name
     * and return to client if operation was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        boolean isChangeFirstNameSuccessful = userService.changeUserFirstName(userService.getLoggedInUser().getUsername(), this.clientConnection.read());
        this.clientConnection.write(isChangeFirstNameSuccessful);
    }
}
