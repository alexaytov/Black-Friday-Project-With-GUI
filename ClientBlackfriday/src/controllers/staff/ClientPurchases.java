package controllers.staff;

import application.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import common.Operations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import store.earnings.Purchase;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClientPurchases implements Initializable {

    private Map<String, List<Purchase>> purchases;

    @FXML
    private JFXTreeView<String> clientPurchases;

    @FXML
    private JFXTextField clientsSearchTextField;


    @FXML
    void goStaffProducts(ActionEvent event) throws IOException {
        this.clientPurchases.getScene().getWindow().hide();
        Operations.loadWindow(this.getClass(), "/FXML/staff/staffLoggedIn.fxml", "Welcome", 600, 600);
    }

    @FXML
    void searchClients(ActionEvent event) {
        String searchedClient = this.clientsSearchTextField.getText();
        this.clientPurchases.setRoot(null);
        Map<String, List<Purchase>> searchedPurchases = new HashMap<>();

        for (Map.Entry<String, List<Purchase>> purchasesEntry : this.purchases.entrySet()) {
            if (purchasesEntry.getKey().contains(searchedClient)) {
                searchedPurchases.put(purchasesEntry.getKey(), purchasesEntry.getValue());
            }
        }

        if (searchedPurchases.size() == 0) {
            TreeItem<String> root = new TreeItem<>("No purchases found!");
            this.clientPurchases.setShowRoot(true);
            this.clientPurchases.setRoot(root);

        } else {
            fillTreeView(searchedPurchases);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.showAllPurchases();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void showAllPurchases() throws IOException, ClassNotFoundException {
        Main.store.getOos().writeObject("get all clients information");
        purchases = (Map<String, List<Purchase>>) Main.store.getOis().readObject();

        fillTreeView(purchases);
    }

    private void fillTreeView(Map<String, List<Purchase>> purchases) {
        TreeItem<String> root = new TreeItem<>();
        this.clientPurchases.setShowRoot(false);
        this.clientPurchases.setRoot(root);

        for (Map.Entry<String, List<Purchase>> purchasesEntry : purchases.entrySet()) {
            TreeItem<String> clientUsername = new TreeItem<>(purchasesEntry.getKey());

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