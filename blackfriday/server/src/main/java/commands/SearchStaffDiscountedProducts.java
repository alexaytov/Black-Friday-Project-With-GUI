package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class SearchStaffDiscountedProducts implements Executable {

    private Connection clientConnection;
    private UserService userService;
    private ProductService productService;

    public SearchStaffDiscountedProducts(Connection clientConnection, UserService userService, ProductService productService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Searches products for users with permission admin
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String searchedDiscountedProductsName = this.clientConnection.read().toString();
        this.clientConnection.write(productService.searchDiscountedStaffProducts(searchedDiscountedProductsName));
    }
}
