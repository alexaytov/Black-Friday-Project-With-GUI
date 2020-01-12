package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.NotFoundException;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductSize implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        String newSize = this.clientConnection.read().toString();
        try {
            this.store.changeProductSize(newSize);
            this.clientConnection.write(true);
        } catch (NotFoundException e) {
            this.clientConnection.write(false);
        }
    }
}
