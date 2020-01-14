package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.NotFoundException;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteClientByUsername implements Executable {

    private Connection clientConnection;
    private UserService userService;

    public DeleteClientByUsername(Connection clientConnection, UserService userService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
    }

    /**
     * Delete client by username
     * and return to client if operation was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String usernameOfClientToBeDeleted = this.clientConnection.read().toString();
        try {
            userService.deleteUser(usernameOfClientToBeDeleted);
            this.clientConnection.write(true);
        } catch (NotFoundException e) {
            this.clientConnection.write(false);
        }
    }
}
