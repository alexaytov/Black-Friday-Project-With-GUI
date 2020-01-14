package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.ProductAlreadyExistsException;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductName implements Executable {

    private Connection clientConnection;
    private UserService userService;
    private ProductService productService;

    public ChangeProductName(Connection clientConnection, UserService userService, ProductService productService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Changes chosen product name
     * and return to client if operation was successful
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String newName = this.clientConnection.read().toString();
        try {
            productService.changeProductName(newName);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException | ProductAlreadyExistsException ex) {
            this.clientConnection.write(false);
        }
    }
}
