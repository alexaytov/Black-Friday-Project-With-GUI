package controllers.staff;

import application.App;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import store.earnings.Purchase;
import util.Operations;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ClientPurchases implements Initializable {

    private Map<String, List<Purchase>> purchases;

    @FXML
    private JFXTreeView<String> clientPurchases;

    @FXML
    private JFXTextField clientsSearchTextField;


    @FXML
    void goStaffProducts(ActionEvent event) {
        this.clientPurchases.getScene().getWindow().hide();
        Operations.loadWindow("/view/staff/staffLoggedIn.fxml", 600, 600);
    }

    @FXML
    void searchClients(ActionEvent event) {
        // search client purchases based on client username
        String searchedClient = this.clientsSearchTextField.getText();
        // remove previously loaded client purchases
        this.clientPurchases.setRoot(null);
        Map<String, List<Purchase>> searchedPurchases = new HashMap<>();
        // fill searchedPurchases with purchases
        for (Map.Entry<String, List<Purchase>> purchasesEntry : this.purchases.entrySet()) {
            if (purchasesEntry.getKey().contains(searchedClient)) {
                searchedPurchases.put(purchasesEntry.getKey(), purchasesEntry.getValue());
            }
        }
        // set no purchases found message is there are none
        if (searchedPurchases.size() == 0) {
            TreeItem<String> root = new TreeItem<>("No purchases found!");
            this.clientPurchases.setShowRoot(true);
            this.clientPurchases.setRoot(root);
        } else {
            // fill UI with searched purchases
            fillTreeView(searchedPurchases);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.showAllPurchases();
    }


    @FXML
    void showAllPurchases() {
        // load UI with all purchases
        App.serverConnection.write("get all clients information");
        purchases = App.serverConnection.read();
        fillTreeView(purchases);
    }

    private void fillTreeView(Map<String, List<Purchase>> purchases) {
        TreeItem<String> root = new TreeItem<>();
        this.clientPurchases.setShowRoot(false);
        this.clientPurchases.setRoot(root);
        for (Map.Entry<String, List<Purchase>> purchasesEntry : purchases.entrySet()) {
            TreeItem<String> clientUsername = new TreeItem<>(purchasesEntry.getKey());
            // add all purchases for this client
            for (Purchase purchase : purchasesEntry.getValue()) {
                TreeItem<String> purchaseProductName = new TreeItem<>(purchase.getProductName());
                TreeItem<String> purchaseInformation = new TreeItem<>(purchase.toString());
                purchaseProductName.getChildren().add(purchaseInformation);
                clientUsername.getChildren().add(purchaseProductName);
            }
            root.getChildren().add(clientUsername);
        }
    }
}
