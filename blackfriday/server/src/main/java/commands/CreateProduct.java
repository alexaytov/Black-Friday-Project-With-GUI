package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.DataAlreadyExistsException;
import product.Product;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class CreateProduct implements Executable {

    private Connection clientConnection;
    private UserService userService;
    private ProductService productService;

    public CreateProduct(Connection clientConnection, UserService userService, ProductService productService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Adds product to product database from (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        Product product = this.clientConnection.read();
        try {
            productService.addProduct(product);
            this.clientConnection.write(true);
        } catch (DataAlreadyExistsException e) {
            this.clientConnection.write(false);
        }
    }
}
