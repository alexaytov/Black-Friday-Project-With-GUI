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

public class ChangeProductSize implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private UserService userService;

    @Inject
    private ProductService productService;

    /**
     * Changes chosen product size
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String newSize = this.clientConnection.read().toString();
        productService.changeProductSize(newSize);
        this.clientConnection.write(true);
    }
}
