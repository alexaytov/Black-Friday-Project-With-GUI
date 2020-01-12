package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductDiscountPercent implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        double newDiscountPercentage = this.clientConnection.read();
        try {
            this.store.changeProductDiscountPercent(newDiscountPercentage);
            this.clientConnection.write(true);
        } catch (IllegalArgumentException ex) {
            this.clientConnection.write(false);
        }
    }
}
