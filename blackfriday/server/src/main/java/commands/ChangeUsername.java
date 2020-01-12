package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.DataAlreadyExistsException;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeUsername implements Executable {

    @Inject
    private ServerClientConnection clientConnection;

    @Inject
    private Store store;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        boolean isUsernameChangeSuccessful;
        try {
            isUsernameChangeSuccessful = this.store.changeUsername(this.store.getLoggedInUser().getUsername(), this.clientConnection.read());
        } catch (DataAlreadyExistsException e) {
            isUsernameChangeSuccessful = false;
        }
        this.clientConnection.write(isUsernameChangeSuccessful);
    }
}
