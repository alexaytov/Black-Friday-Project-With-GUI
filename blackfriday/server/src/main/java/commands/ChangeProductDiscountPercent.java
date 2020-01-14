package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductDiscountPercent implements Executable {

    private UserService userService;
    private Connection clientConnection;
    private ProductService productService;

    public ChangeProductDiscountPercent(UserService userService, Connection clientConnection, ProductService productService) {
        this.userService = userService;
        this.clientConnection = clientConnection;
        this.productService = productService;
    }

    /**
     * Changes chosen product colcount percent
     * and return to client if operation was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        double newDiscountPercentage = this.clientConnection.read();
        try {
            productService.changeProductDiscountPercent(newDiscountPercentage);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException ex) {
            this.clientConnection.write(false);
        }
    }
}
