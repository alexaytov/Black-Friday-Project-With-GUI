package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.ServerClientConnection;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

import static validator.Validator.requireNonNegative;
import static validator.Validator.validateMonth;

public class EarningsMonth implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    /**
     * Gets earnings for specific month and
     * sends them through (@code clientConnection)
     *
     * @throws IOException            if IO error occurs
     * @throws SQLException           if SQL error occurs
     * @throws ClassNotFoundException if read class by (@code clientConnection) is not found
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        int month = this.clientConnection.read();
        int year = this.clientConnection.read();
        validateMonth(month);
        requireNonNegative(year, ExceptionMessages.YEAR_MUST_BE_POSITIVE);
        this.clientConnection.write(store.getEarnings(month, year));
    }
}
