package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductQuantity implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        int quantity = this.clientConnection.read();
        try {
            this.store.changeProductQuantity(quantity);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException ex) {
            this.clientConnection.write(false);
        }
    }
}
