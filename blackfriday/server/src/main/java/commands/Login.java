package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import connection.Connection;
import exceptions.NotFoundException;
import exceptions.WrongPasswordException;
import passwordHasher.BCryptHasher;
import store.services.UserService;
import user.User;

import java.io.IOException;
import java.sql.SQLException;

public class Login implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private UserService userService;

    /**
     * Sends used salt for hashed password for this specific user trough (@code clientConnection)
     * Reads hashed password from (@code clientConnection) and compares it with stored password.
     * If user is logged in sets (@code loggedInUser) field in store and sends it trough (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        String username = this.clientConnection.read();
        User userToBeLoggedIn;
        try {
            userToBeLoggedIn = userService.getUser(username);
            String salt = BCryptHasher.getSalt(userToBeLoggedIn.getPassword());
            this.clientConnection.write(salt);
            String enteredHashedPassword = this.clientConnection.read();
            if (enteredHashedPassword.equals(userToBeLoggedIn.getPassword())) {
                userService.setLoggedInUser(userToBeLoggedIn);
                this.clientConnection.write(userToBeLoggedIn.clone());
            } else {
                throw new WrongPasswordException();
            }
        } catch (NotFoundException | WrongPasswordException e) {
            this.clientConnection.write(null);
            this.clientConnection.write(e.getClass().getSimpleName());
        }catch (CloneNotSupportedException ex){
            ex.printStackTrace();
        }
    }
}
