package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.Store;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class IsBlackFriday implements Executable {

    private Store store;
    private Connection clientConnection;
    private UserService userService;

    public IsBlackFriday(Store store, Connection clientConnection, UserService userService) {
        this.store = store;
        this.clientConnection = clientConnection;
        this.userService = userService;
    }

    /**
     * Sends result from (code isBlackFriday) method in store
     * trough (@code clientConnection)
     *
     * @throws IOException if IO error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        this.clientConnection.write(this.store.isBlackFriday());
    }
}
