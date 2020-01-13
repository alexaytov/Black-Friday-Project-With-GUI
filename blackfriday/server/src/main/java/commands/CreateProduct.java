package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.DataAlreadyExistsException;
import product.Product;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class CreateProduct implements Executable {

    @Inject
    private Store store;

    @Inject
    private Connection clientConnection;

    /**
     * Adds product to product database from (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        Product product = this.clientConnection.read();
        try {
            this.store.addProduct(product);
            this.clientConnection.write(true);
        } catch (DataAlreadyExistsException e) {
            this.clientConnection.write(false);
        }
    }
}
