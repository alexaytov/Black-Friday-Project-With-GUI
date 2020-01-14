package store.services;

import database.interfaces.Database;
import store.earnings.Purchase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseService {

    private Database<Purchase> purchaseDatabase;

    public PurchaseService(Database<Purchase> purchaseDatabase) {
        this.purchaseDatabase = purchaseDatabase;
    }

    /**
     * @return all of the client purchases
     */
    public Map<String, List<Purchase>> getClientPurchases() throws IOException, SQLException {
        List<Purchase> purchases = this.purchaseDatabase.read("quantity != 0");
        HashMap<String, List<Purchase>> purchaseMap = new HashMap<>();
        for (Purchase purchase : purchases) {
            if (purchaseMap.containsKey(purchase.getUsername())) {
                purchaseMap.get(purchase.getUsername()).add(purchase);
            } else {
                purchaseMap.put(purchase.getUsername(), new ArrayList<>());
                purchaseMap.get(purchase.getUsername()).add(purchase);
            }
        }
        return purchaseMap;
    }
}
