package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

public class SetProduct implements Executable {

    private Connection clientConnection;
    private UserService userService;
    private ProductService productService;

    public SetProduct(Connection clientConnection, UserService userService, ProductService productService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Sets store's chosen product
     */
    @Override
    public void execute() {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        productService.setChosenProduct(this.clientConnection.read());
    }
}
