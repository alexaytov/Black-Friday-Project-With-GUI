package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.NotEnoughQuantityException;
import exceptions.NotFoundException;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class BuyProduct implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        String productName = this.clientConnection.read().toString();
        int quantity = this.clientConnection.read();
        boolean isBought = false;
        try {
            isBought = this.store.buyProduct(productName, this.store.getLoggedInUser(), quantity);
            this.clientConnection.write(isBought);
        } catch (NotFoundException | NotEnoughQuantityException e) {
            this.clientConnection.write(false);
        }
    }
}
