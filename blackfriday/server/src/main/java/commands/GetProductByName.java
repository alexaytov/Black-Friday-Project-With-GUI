package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.NotFoundException;
import product.Product;
import store.services.ProductService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class GetProductByName implements Executable {

    private Connection clientConnection;
    private UserService userService;
    private ProductService productService;

    public GetProductByName(Connection clientConnection, UserService userService, ProductService productService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Gets product by name and sends them
     * trough (@code clientConnection)
     *
     * @throws IOException  is IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String name = this.clientConnection.read().toString();
        Product chosenProduct;
        try {
            chosenProduct = productService.getProductByName(name);
            this.clientConnection.write(chosenProduct.clone());
        } catch (NotFoundException e) {
            productService.setChosenProduct(null);
            this.clientConnection.write(null);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
