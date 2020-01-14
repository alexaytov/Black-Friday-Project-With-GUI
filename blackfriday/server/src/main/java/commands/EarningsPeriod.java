package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.services.EarningsService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class EarningsPeriod implements Executable {

    private Connection clientConnection;
    private UserService userService;
    private EarningsService earningsService;

    public EarningsPeriod(Connection clientConnection, UserService userService, EarningsService earningsService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
        this.earningsService = earningsService;
    }

    /**
     * Gets earnings for a specific period
     * and sends them trough (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        LocalDate startDate = this.clientConnection.read();
        LocalDate endDate = this.clientConnection.read();
        this.clientConnection.write(earningsService.getEarnings(startDate, endDate));
    }
}
