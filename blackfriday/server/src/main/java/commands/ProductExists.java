package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.NotFoundException;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ProductExists implements Executable {

    private Connection clientConnection;
    private UserService userService;
    private ProductService productService;

    public ProductExists(Connection clientConnection, UserService userService, ProductService productService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Checks if a product with name read from (@code clientConnection) exists in store
     * and sends result trough (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        try {
            productService.getProductByName(this.clientConnection.read().toString());
            this.clientConnection.write(true);
        } catch (NotFoundException e) {
            this.clientConnection.write(false);
        }
    }
}
