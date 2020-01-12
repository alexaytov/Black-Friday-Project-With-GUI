package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class StopBlackFriday implements Executable {

    @Inject
    private Store store;

    /**
     * Calls (@code setBlackFriday) in store with argument (@code false)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        this.store.setBlackFriday(false);
    }
}
