package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;

public class IsBlackFriday implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    /**
     * Sends result from (code isBlackFriday) method in store
     * trough (@code clientConnection)
     *
     * @throws IOException if IO error occurs
     */
    @Override
    public void execute() throws IOException {
        this.clientConnection.write(this.store.isBlackFriday());
    }
}
