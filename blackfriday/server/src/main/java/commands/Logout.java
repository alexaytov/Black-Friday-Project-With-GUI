package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class Logout implements Executable {

    @Inject
    private Store store;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        this.store.setLoggedInUser(null);
    }
}
