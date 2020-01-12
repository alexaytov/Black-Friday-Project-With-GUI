package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.ProductAlreadyExistsException;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductName implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        String newName = this.clientConnection.read().toString();
        try {
            this.store.changeProductName(newName);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException | ProductAlreadyExistsException ex) {
            this.clientConnection.write(false);
        }
    }
}
