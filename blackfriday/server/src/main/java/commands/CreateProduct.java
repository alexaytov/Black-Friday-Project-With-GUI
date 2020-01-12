package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.DataAlreadyExistsException;
import product.Product;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class CreateProduct implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        Product product = this.clientConnection.read();
        try {
            this.store.addProduct(product);
            this.clientConnection.write(true);
        } catch (DataAlreadyExistsException e) {
            this.clientConnection.write(false);
        }
    }
}
