package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.NotFoundException;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteUser implements Executable {

    private Connection clientConnection;
    private UserService userService;

    public DeleteUser(Connection clientConnection, UserService userService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
    }

    /**
     * Delete user and to client
     * if the operation was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        try {
            userService.deleteUser(userService.getLoggedInUser().getUsername());
            this.clientConnection.write(true);
        } catch (NotFoundException ex) {
            this.clientConnection.write(false);
        }
    }
}
