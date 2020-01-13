package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.NotFoundException;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteProduct implements Executable {

    @Inject
    private Store store;

    @Inject
    private Connection clientConnection;

    /**
     * Delete chosen product in store
     * and return if the operation is successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        try {
            store.deleteProduct(this.clientConnection.read().toString());
            this.clientConnection.write(true);
        } catch (NotFoundException ex) {
            this.clientConnection.write(false);
        }
    }
}
