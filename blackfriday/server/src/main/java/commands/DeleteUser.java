package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.NotFoundException;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteUser implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        try {
            this.store.deleteUser(this.store.getLoggedInUser().getUsername());
            this.clientConnection.write(true);
        } catch (NotFoundException ex) {
            this.clientConnection.write(false);
        }
    }
}
