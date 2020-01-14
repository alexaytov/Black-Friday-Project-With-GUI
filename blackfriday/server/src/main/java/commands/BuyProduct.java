package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.NotEnoughQuantityException;
import exceptions.NotFoundException;
import store.earnings.Purchase;
import store.services.EarningsService;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class BuyProduct implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private UserService userService;

    @Inject
    private ProductService productService;

    @Inject
    private EarningsService earningsService;

    /**
     * Executes buy logic in Product class and return
     * to client if operation is successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String productName = this.clientConnection.read().toString();
        int quantity = this.clientConnection.read();
        boolean isBought;
        try {
            Purchase purchase = productService.buyProduct(productName, userService.getLoggedInUser(), quantity);
            this.clientConnection.write(true);
            earningsService.logPurchase(purchase);
        } catch (NotFoundException | NotEnoughQuantityException e) {
            this.clientConnection.write(false);
        }
    }
}
