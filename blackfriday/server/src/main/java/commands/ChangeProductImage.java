package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductImage implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    /**
     * Changes chosen product image
     * and return to client if operation was successful
     *
     * @throws IOException            if IO error occurs
     * @throws SQLException           if SQL error occurs
     * @throws ClassNotFoundException if read class by (@code clientConnection) is not found
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        byte[] newImageContent = this.clientConnection.read();
        try {
            this.store.changeProductImage(newImageContent);
            this.clientConnection.write(true);
        } catch (NullPointerException ex) {
            this.clientConnection.write(false);
        }
    }
}