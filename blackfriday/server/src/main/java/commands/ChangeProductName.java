package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.ProductAlreadyExistsException;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductName implements Executable {

    @Inject
    private Store store;

    @Inject
    private Connection clientConnection;

    /**
     * Changes chosen product name
     * and return to client if operation was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String newName = this.clientConnection.read().toString();
        try {
            this.store.changeProductName(newName);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException | ProductAlreadyExistsException ex) {
            this.clientConnection.write(false);
        }
    }
}
