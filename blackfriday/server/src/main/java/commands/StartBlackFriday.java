package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class StartBlackFriday implements Executable {

    @Inject
    private Store store;

    /**
     * Calls (@code setBlackFriday) in store with argument (@code true)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        this.store.setBlackFriday(true);
    }
}