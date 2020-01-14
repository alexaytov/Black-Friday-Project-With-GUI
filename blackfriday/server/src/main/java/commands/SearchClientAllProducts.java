package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class SearchClientAllProducts implements Executable {

    private Connection clientConnection;
    private UserService userService;
    private ProductService productService;

    public SearchClientAllProducts(Connection clientConnection, UserService userService, ProductService productService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Searches all products for users with permission
     * client
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String searchedAllProductsName = this.clientConnection.read().toString();
        this.clientConnection.write(productService.searchAllProducts(searchedAllProductsName));
    }
}
