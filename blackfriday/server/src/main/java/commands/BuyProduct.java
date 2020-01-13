package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.NotEnoughQuantityException;
import exceptions.NotFoundException;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class BuyProduct implements Executable {

    @Inject
    private Store store;

    @Inject
    private Connection clientConnection;

    /**
     * Executes buy logic in Product class and return
     * to client if operation is successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String productName = this.clientConnection.read().toString();
        int quantity = this.clientConnection.read();
        boolean isBought;
        try {
            isBought = this.store.buyProduct(productName, this.store.getLoggedInUser(), quantity);
            this.clientConnection.write(isBought);
        } catch (NotFoundException | NotEnoughQuantityException e) {
            this.clientConnection.write(false);
        }
    }
}
