package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class Logout implements Executable {

    @Inject
    private Store store;

    /**
     * Sets loggedInUser in store to null
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        this.store.setLoggedInUser(null);
    }
}
