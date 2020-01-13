package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.Store;
import validator.Validator;

public class SetProduct implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private Store store;

    /**
     * Sets store's chosen product
     */
    @Override
    public void execute() {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        this.store.setChosenProduct(this.clientConnection.read());
    }
}
