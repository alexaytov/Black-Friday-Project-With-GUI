package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import store.Store;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class StartBlackFriday implements Executable {

    private Store store;
    private UserService userService;

    public StartBlackFriday(Store store, UserService userService) {
        this.store = store;
        this.userService = userService;
    }

    /**
     * Calls (@code setBlackFriday) in store with argument (@code true)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        this.store.setBlackFriday(true);
    }
}
