package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class EarningsDate implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {
        LocalDate date = LocalDate.parse(this.clientConnection.read().toString());
        this.clientConnection.write(store.getEarnings(date));
    }
}
