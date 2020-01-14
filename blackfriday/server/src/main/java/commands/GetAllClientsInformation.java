package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.earnings.Purchase;
import store.services.PurchaseService;
import store.services.UserService;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GetAllClientsInformation implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private UserService userService;

    @Inject
    private PurchaseService purchaseService;

    /**
     * Gets client information and sends
     * it through (@code clientConnection)
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(userService.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        Map<String, List<Purchase>> clientPurchases = purchaseService.getClientPurchases();
        System.out.println(clientPurchases);
        this.clientConnection.write(clientPurchases);
    }
}
