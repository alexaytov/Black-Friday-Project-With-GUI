package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class GetClientProducts implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private Store store;

    /**
     * Gets all products for user with client permission
     * and sends it trough (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        this.clientConnection.write(this.store.getClientProducts());
    }
}
