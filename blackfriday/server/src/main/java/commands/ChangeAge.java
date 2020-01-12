package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeAge implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        boolean isAgeChangeSuccessful = this.store.changeAge(this.store.getLoggedInUser().getUsername(), Integer.parseInt(this.clientConnection.read().toString()));
        this.clientConnection.write(isAgeChangeSuccessful);
    }
}
