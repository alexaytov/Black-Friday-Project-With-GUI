package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductPrice implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private UserService userService;

    @Inject
    private ProductService productService;

    /**
     * Changes chosen product price
     * and return to client if operation was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        double newPrice = this.clientConnection.read();
        try {
            productService.changeProductPrice(newPrice);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException ex) {
            this.clientConnection.write(false);
        }

    }
}
