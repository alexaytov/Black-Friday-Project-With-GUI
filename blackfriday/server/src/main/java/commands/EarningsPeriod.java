package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class EarningsPeriod implements Executable {

    @Inject
    private Store store;

    @Inject
    private Connection clientConnection;

    /**
     * Gets earnings for a specific period
     * and sends them trough (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        LocalDate startDate = this.clientConnection.read();
        LocalDate endDate = this.clientConnection.read();
        this.clientConnection.write(store.getEarnings(startDate, endDate));
    }
}
