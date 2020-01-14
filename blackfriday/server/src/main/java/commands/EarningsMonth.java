package commands;

import command.enterpreter.interfaces.Executable;
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

    private Connection clientConnection;
    private UserService userService;
    private EarningsService earningsService;

    public EarningsMonth(Connection clientConnection, UserService userService, EarningsService earningsService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.earningsService = earningsService;
    }

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
