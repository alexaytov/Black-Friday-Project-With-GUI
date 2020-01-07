package server;

import commonMessages.ExceptionMessages;
import connection.Connection;
import exceptions.*;
import product.Product;
import store.Store;
import user.BaseUser;
import user.Client;
import user.Staff;
import user.interfaces.User;
import validator.Validator;

import java.io.IOException;
import java.time.LocalDate;

public class ClientThread implements Runnable {

    private Connection clientConnection;
    private BaseUser user;
    private Store store;
    private Product product;


    public ClientThread(Connection clientConnection, Store store) throws IOException {
        setClientConnection(clientConnection);
        this.store = store;


    }

    public void setClientConnection(Connection clientConnection) {
        Validator.requireNonNull(clientConnection, ExceptionMessages.CONNECTION_NULL);
        this.clientConnection = clientConnection;
    }

    public void run() {
        try {
            while (true) {
                String command = this.clientConnection.read();

                switch (command) {
                    case "login":
                        //tokens
                        //1 -> user type 2 -> username 3 -> password
                        String[] tokens = ((String) this.clientConnection.read()).split("\\s+");
                        try {
                            if (tokens[0].equals("client")) {
                                this.user = this.store.login(tokens[1], tokens[2]);
                            } else {
                                this.user = this.store.login(tokens[1], tokens[2]);
                            }
                            this.clientConnection.write(user.clone());
                        } catch (WrongPasswordException | NotFoundException ex) {
                            this.clientConnection.write(null);
                            this.clientConnection.write(ex.getClass().getSimpleName());
                        }
                        break;
                    case "register client":
                        //TODO make one case register User
                        User client = this.clientConnection.read();
                        try {
                            this.store.registerUser(client);
                            this.clientConnection.write("true");
                        } catch (UserAlreadyExistsException ex) {
                            this.clientConnection.write("false");
                        }
                        break;
                    case "register staff":
                        User staff = this.clientConnection.read();
                        try {
                            this.store.registerUser(staff);
                            this.clientConnection.write(true);
                        } catch (UserAlreadyExistsException e) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "start blackFriday":
                        // starts black friday promotions
                        this.store.setBlackFriday(true);
                        break;
                    case "stop blackFriday":
                        // stops black friday promotions
                        this.store.setBlackFriday(false);
                        break;
                    case "has promotions":
                        this.clientConnection.write(this.store.isBlackFriday());
                        break;
                    case "get client products": {
                        //gets all product names for client
                        this.clientConnection.write(this.store.getClientProducts());

                    }
                    break;
                    case "change product minimum price":
                        double minimumPrice = this.clientConnection.read();
                        try {
                            this.product.setMinimumPrice(minimumPrice);
                            this.clientConnection.write(true);
                        } catch (IllegalArgumentException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "change product price":
                        double newPrice = this.clientConnection.read();
                        try {
                            this.store.changeProductPrice(this.product.getName(), newPrice);
                            this.clientConnection.write(true);
                        } catch (IllegalArgumentException ex) {
                            this.clientConnection.write(false);
                        }

                        break;
                    case "get client discounted products":
                        this.clientConnection.write(this.store.getClientDiscountedProducts());
                        break;
                    case "search staff all products": {
                        String searchedAllProductsName = this.clientConnection.read().toString();
                        this.clientConnection.write(this.store.searchAllProducts(searchedAllProductsName));
                        break;
                    }
                    case "search staff discounted products": {
                        String searchedDiscountedProductsName = this.clientConnection.read().toString();
                        this.clientConnection.write(this.store.searchDiscountedProducts(searchedDiscountedProductsName));
                        break;
                    }
                    case "search client all products":
                        String searchedAllProductsName = this.clientConnection.read().toString();
                        this.clientConnection.write(this.store.searchAllProducts(searchedAllProductsName));
                        break;
                    case "search client discounted products":
                        String searchedDiscountedProductsName = this.clientConnection.read().toString();
                        this.clientConnection.write(this.store.searchDiscountedProducts(searchedDiscountedProductsName));
                        break;
                    case "search quantity control":
                        int maximumQuantity = this.clientConnection.read();
                        this.clientConnection.write(this.store.searchQuantityControl(maximumQuantity));
                        break;
                    case "set product":
                        this.product = this.clientConnection.read();
                        break;
                    case "get product by name":
                        String name = this.clientConnection.read().toString();
                        Product chosenProduct;
                        try {
                            chosenProduct = this.store.getProductByName(name);
                            this.product = chosenProduct;
                            this.clientConnection.write(chosenProduct.clone());
                        } catch (NotFoundException e) {
                            this.product = null;
                            this.clientConnection.write(null);
                        }
                        break;
                    case "product exists":
                        try {
                            store.getProductByName(this.clientConnection.read().toString());
                            this.clientConnection.write(true);
                        } catch (NotFoundException e) {
                            this.clientConnection.write(false);
                        }
                    case "buy product": {
                        String productName = this.clientConnection.read().toString();
                        int quantity = this.clientConnection.read();
                        boolean isBought = false;
                        try {
                            isBought = this.store.buyProduct(productName, this.user, quantity);
                            this.clientConnection.write(isBought);
                        } catch (NotFoundException | NotEnoughQuantityException e) {
                            this.clientConnection.write(false);
                        }
                    }
                    break;
                    case "create product":
                        Product product = this.clientConnection.read();
                        try {
                            this.store.addProduct(product);
                            this.clientConnection.write(true);
                        } catch (ProductAlreadyExistsException e) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "delete client":
                        //delete currently logged in client
                        try {
                            this.store.deleteUser(this.user.getUsername());
                            this.clientConnection.write(true);
                        } catch (NotFoundException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "delete staff":
                        // deletes staff account currently logged in
                        try {
                            this.store.deleteUser(this.user.getUsername());
                            this.clientConnection.write(true);
                        } catch (NotFoundException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "get all clients information":
                        this.clientConnection.write(store.getClientPurchases());
                        break;
                    case "delete client by username":
                        String usernameOfClientToBeDeleted = this.clientConnection.read().toString();
                        try {
                            store.deleteUser(usernameOfClientToBeDeleted);
                            this.clientConnection.write(true);
                        } catch (NotFoundException e) {
                            this.clientConnection.write(false);
                        }
                        this.clientConnection.write(true);

                        break;
                    case "change first name":
                        // changes first name of user currently logged in
                        boolean isChangeFirstNameSuccessful = this.store.changeUserFirstName(this.user, this.clientConnection.read());
                        this.clientConnection.write(isChangeFirstNameSuccessful);
                        break;
                    case "change last name":
                        // changes last name of user currently logged in
                        boolean isLastNameChangeSuccessful = this.store.changeUserLastName(this.user, this.clientConnection.read());
                        this.clientConnection.write(isLastNameChangeSuccessful);
                        break;
                    case "change username":
                        // changes username of user currently logged in
                        boolean isUsernameChangeSuccessful;
                        try {
                            isUsernameChangeSuccessful = this.store.changeUsername(this.user, this.clientConnection.read());
                        } catch (UserAlreadyExistsException e) {
                            isUsernameChangeSuccessful = false;
                        }
                        this.clientConnection.write(isUsernameChangeSuccessful);
                        break;
                    case "change password":
                        // changes password of user currently logged in
                        boolean isPasswordChangeSuccessful = this.store.changePassword(this.user, this.clientConnection.read());
                        this.clientConnection.write(isPasswordChangeSuccessful);
                        break;
                    case "change age":
                        // changes age of user currently logged in
                        boolean isAgeChangeSuccessful = this.store.changeAge(this.user, Integer.parseInt(this.clientConnection.read().toString()));
                        this.clientConnection.write(isAgeChangeSuccessful);
                        break;
                    case "change product name":
                        // changes chosen product name from "get product by name"
                        String newName = this.clientConnection.read().toString();
                        try {
                            this.store.changeProductName(this.product, newName);
                            this.clientConnection.write(true);
                        } catch (IllegalArgumentException | NotFoundException | ProductAlreadyExistsException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "change product image":
                        byte[] newImageContent = this.clientConnection.read();
                        try {
                            this.store.changeProductImage(this.product.getName(), newImageContent);
                            this.clientConnection.write(true);
                        } catch (NullPointerException ex) {
                            this.clientConnection.write(false);
                        }


                        break;
                    case "change product description":
                        // changes chosen product description from "get product by name"
                        String newDescription = this.clientConnection.read().toString();
                        try {
                            this.store.changeProductDescription(this.product.getName(), newDescription);
                            this.clientConnection.write(true);
                        } catch (IllegalArgumentException | NotFoundException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "change product size":
                        String newSize = this.clientConnection.read().toString();
                        try {
                            this.store.changeProductSize(this.product.getName(), newSize);
                            this.clientConnection.write(true);
                        } catch (NotFoundException e) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "change product discount percent":
                        // changes chosen product discount percent from "get product by name"
                        double newDiscountPercentage = this.clientConnection.read();
                        try {
                            this.store.changeProductDiscountPercent(this.product.getName(), newDiscountPercentage);
                            this.clientConnection.write(true);
                        } catch (IllegalArgumentException | NotFoundException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "change product quantity":
                        // changes chosen product quantity from "get product by name"
                        int quantity = this.clientConnection.read();
                        try {
                            this.store.changeProductQuantity(this.product.getName(), quantity);
                            this.clientConnection.write(true);
                        } catch (IllegalArgumentException | NotFoundException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "get staff products":
                        this.clientConnection.write(store.getStaffProducts());
                        break;
                    case "delete product":
                        // deletes product by given name
                        try {
                            store.deleteProduct(this.clientConnection.read().toString());
                            this.clientConnection.write(true);
                        } catch (NotFoundException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "exit product options":
                        // sets currently chosen product to null
                        this.product = null;
                        break;
                    case "get staff discounted products":
                        this.clientConnection.write(store.getStaffDiscountedProducts());
                        break;
                    case "earnings date":
                        LocalDate date = LocalDate.parse(this.clientConnection.read().toString());
                        this.clientConnection.write(store.getEarnings(date));
                        break;
                    case "earnings year": {
                        int year = this.clientConnection.read();
                        this.clientConnection.write(store.getEarnings(year));
                    }
                    break;
                    case "earnings month":
                        int month = this.clientConnection.read();
                        int year = this.clientConnection.read();
                        this.clientConnection.write(store.getEarnings(month, year));
                        break;
                    case "earnings period":
                        LocalDate startDate = this.clientConnection.read();
                        LocalDate endDate = this.clientConnection.read();
                        this.clientConnection.write(store.getEarnings(startDate, endDate));
                        break;
                    case "logout":
                        this.user = null;
                        break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Client thread: " + Thread.currentThread().getName() + " ended!!!");
        } catch (ClassNotFoundException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sens a clone of BaseUser object through ObjectOuputStream defined in class ClientThread
     *
     * @param baseUser BaseUser object sent through the ObjectOutpostStream
     * @throws IOException                if I/O error occuts
     * @throws CloneNotSupportedException if the BaseUser object is not cloneable
     */
    private void sendUserThroughDataStream(BaseUser baseUser) throws IOException, CloneNotSupportedException {
        if (baseUser == null) {
            this.clientConnection.write(null);

        } else {
            this.clientConnection.write(baseUser.clone());
        }
    }
}
