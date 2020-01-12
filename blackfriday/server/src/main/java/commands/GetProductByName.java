package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.NotFoundException;
import product.Product;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class GetProductByName implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        String name = this.clientConnection.read().toString();
        Product chosenProduct;
        try {
            chosenProduct = this.store.getProductByName(name);
            this.clientConnection.write(chosenProduct.clone());
        } catch (NotFoundException e) {
            this.store.setChosenProduct(null);
            this.clientConnection.write(null);
        }
    }
}
