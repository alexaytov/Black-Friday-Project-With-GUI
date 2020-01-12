package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductMinimumPrice implements Executable {

    @Inject
    private ServerClientConnection clientConnection;

    @Inject
    private Store store;

    @Override
    public void execute() throws IOException, ClassNotFoundException, SQLException {
        double minimumPrice = this.clientConnection.read();
        try {
            this.store.changeProductMinimumPrice(minimumPrice);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException ex) {
            this.clientConnection.write(false);
        }
    }
}
