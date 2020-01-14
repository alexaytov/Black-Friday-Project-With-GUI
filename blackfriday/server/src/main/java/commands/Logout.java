package commands;

import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class Logout implements Executable {

    private UserService userService;

    public Logout(UserService userService) {
        this.userService = userService;
    }

    /**
     * Sets loggedInUser in store to null
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        userService.setLoggedInUser(null);
    }
}
