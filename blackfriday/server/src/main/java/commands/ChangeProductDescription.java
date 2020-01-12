package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductDescription implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    /**
     * Changes chosen product's description
     * and return to client if operation was successful
     *
     * @throws IOException            if IO error occurs
     * @throws SQLException           if SQL error occurs
     * @throws ClassNotFoundException if class read by
     *                                (@code clientConnection) is not found
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        String newDescription = this.clientConnection.read().toString();
        try {
            this.store.changeProductDescription(newDescription);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException ex) {
            this.clientConnection.write(false);
        }
    }
}