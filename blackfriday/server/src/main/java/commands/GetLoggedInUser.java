package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class GetLoggedInUser implements Executable {

    @Inject
    private ServerClientConnection clientConnection;

    @Inject
    private Store store;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        this.clientConnection.write(store.getLoggedInUser());
    }
}
