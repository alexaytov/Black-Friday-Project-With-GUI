package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class SetProduct implements Executable {

    @Inject
    private ServerClientConnection clientConnection;

    @Inject
    private Store store;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        this.store.setChosenProduct(this.clientConnection.read());
    }
}
