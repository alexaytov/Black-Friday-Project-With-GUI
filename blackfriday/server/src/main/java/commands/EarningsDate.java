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
import java.time.LocalDate;

public class EarningsDate implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private UserService userService;

    @Inject
    private EarningsService earningsService;

    /**
     * Gets earnings for date from store and sends
     * them thought the (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        LocalDate date = LocalDate.parse(this.clientConnection.read().toString());
        this.clientConnection.write(earningsService.getEarnings(date));
    }
}
