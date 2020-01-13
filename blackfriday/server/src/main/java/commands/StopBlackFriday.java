package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class StopBlackFriday implements Executable {

    @Inject
    private Store store;

    /**
     * Calls (@code setBlackFriday) in store with argument (@code false)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        this.store.setBlackFriday(false);
    }
}
