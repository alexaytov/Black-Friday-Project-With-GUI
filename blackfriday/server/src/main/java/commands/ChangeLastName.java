package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeLastName implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        boolean isLastNameChangeSuccessful = this.store.changeUserLastName(this.store.getLoggedInUser().getUsername(), this.clientConnection.read());
        this.clientConnection.write(isLastNameChangeSuccessful);
    }
}
