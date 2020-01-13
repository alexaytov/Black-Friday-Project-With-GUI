package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductPrice implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private Store store;

    /**
     * Changes chosen product price
     * and return to client if operation was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        double newPrice = this.clientConnection.read();
        try {
            this.store.changeProductPrice(newPrice);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException ex) {
            this.clientConnection.write(false);
        }

    }
}
