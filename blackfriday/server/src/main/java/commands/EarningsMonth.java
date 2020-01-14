package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.services.EarningsService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

import static validator.Validator.requireNonNegative;
import static validator.Validator.validateMonth;

public class EarningsMonth implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private UserService userService;

    @Inject
    private EarningsService earningsService;

    /**
     * Gets earnings for specific month and
     * sends them through (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        int month = this.clientConnection.read();
        int year = this.clientConnection.read();
        validateMonth(month);
        requireNonNegative(year, ExceptionMessages.YEAR_MUST_BE_POSITIVE);
        this.clientConnection.write(earningsService.getEarnings(month, year));
    }
}
