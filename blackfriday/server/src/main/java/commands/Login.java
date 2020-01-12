package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.NotFoundException;
import exceptions.WrongPasswordException;
import passwordHasher.interfaces.Hasher;
import store.Store;
import user.User;

import java.io.IOException;
import java.sql.SQLException;

public class Login implements Executable {

    @Inject
    private ServerClientConnection clientConnection;

    @Inject
    private Store store;

    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException {

        String username = this.clientConnection.read();
        User userToBeLoggedIn = null;
        try {
            userToBeLoggedIn = this.store.getUser(username);
            String salt = Hasher.getSalt(userToBeLoggedIn.getPassword());
            this.clientConnection.write(salt);
            String enteredHashedPassword = this.clientConnection.read();
            if (enteredHashedPassword.equals(userToBeLoggedIn.getPassword())) {
                this.store.setLoggedInUser(userToBeLoggedIn);
                this.clientConnection.write(userToBeLoggedIn.clone());
            } else {
                throw new WrongPasswordException();
            }
        } catch (NotFoundException | WrongPasswordException e) {
            this.clientConnection.write(null);
            this.clientConnection.write(e.getClass().getSimpleName());
        }
    }
}
