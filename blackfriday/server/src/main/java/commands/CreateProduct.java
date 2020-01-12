package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.ServerClientConnection;
import exceptions.DataAlreadyExistsException;
import product.Product;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class CreateProduct implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    /**
     * Adds product to product database from (@code clientConnection)
     *
     * @throws IOException            if IO error occurs
     * @throws SQLException           if SQL error occurs
     * @throws ClassNotFoundException if read class by (@code clientConnection) is not found
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        Product product = this.clientConnection.read();
        try {
            this.store.addProduct(product);
            this.clientConnection.write(true);
        } catch (DataAlreadyExistsException e) {
            this.clientConnection.write(false);
        }
    }
}
