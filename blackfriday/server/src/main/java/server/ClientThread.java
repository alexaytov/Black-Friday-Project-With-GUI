package server;

import commonMessages.ExceptionMessages;
import connection.ServerClientConnection;
import exceptions.*;
import passwordHasher.interfaces.Hasher;
import product.Product;
import store.Store;
import user.User;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ClientThread implements Runnable {

    private ServerClientConnection clientConnection;
    private User user;
    private Store store;
    private Product product;


    public ClientThread(ServerClientConnection clientConnection, Store store) throws IOException {
        setClientConnection(clientConnection);
        this.store = store;


    }

    public void setClientConnection(ServerClientConnection clientConnection) {
        Validator.requireNonNull(clientConnection, ExceptionMessages.CONNECTION_NULL);
        this.clientConnection = clientConnection;
    }

    public void run() {
        try {
            while (true) {
                String command = this.clientConnection.read();
                switch (command) {
                    case "login":
                        // tokens
                        // type 2 -> username 3 -> password

                        // get password
                        // send salt
                        // comapre password


                        String username = this.clientConnection.read();
                        User userToBeLoggedIn = null;
                        try {
                            userToBeLoggedIn = this.store.getUser(username);
                            String salt = Hasher.getSalt(userToBeLoggedIn.getPassword());
                            this.clientConnection.write(salt);
                            String enteredHashedPassword = this.clientConnection.read();
                            if(enteredHashedPassword.equals(userToBeLoggedIn.getPassword())){
                                this.user = userToBeLoggedIn;
                                this.clientConnection.write(user.clone());
                            }else{
                                throw new WrongPasswordException();
                            }
                        } catch (NotFoundException | WrongPasswordException e) {
                            this.clientConnection.write(null);
                            this.clientConnection.write(e.getClass().getSimpleName());
                        }


//                        this.store.getUser(tokens[0]);
//                        try {
//                            this.user = this.store.login(tokens[0], tokens[1]);
//
//                            this.clientConnection.write(user.clone());
//                        } catch (WrongPasswordException | NotFoundException ex) {
//                            this.clientConnection.write(null);
//                            this.clientConnection.write(ex.getClass().getSimpleName());
//                        }
                        break;
                    case "register user":
                        User user = this.clientConnection.read();
                        try {
                            this.store.registerUser(user);
                            this.clientConnection.write(true);
                        } catch (DataAlreadyExistsException ex) {
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
                        } catch (IllegalArgumentException | NotFoundException ex) {
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
                        } catch (DataAlreadyExistsException e) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "delete user":
                        //deletes currently logged in user
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
                        boolean isChangeFirstNameSuccessful = this.store.changeUserFirstName(this.user.getUsername(), this.clientConnection.read());
                        this.clientConnection.write(isChangeFirstNameSuccessful);
                        break;
                    case "change last name":
                        // changes last name of user currently logged in
                        boolean isLastNameChangeSuccessful = this.store.changeUserLastName(this.user.getUsername(), this.clientConnection.read());
                        this.clientConnection.write(isLastNameChangeSuccessful);
                        break;
                    case "change username":
                        // changes username of user currently logged in
                        boolean isUsernameChangeSuccessful;
                        try {
                            isUsernameChangeSuccessful = this.store.changeUsername(this.user.getUsername(), this.clientConnection.read());
                        } catch (DataAlreadyExistsException e) {
                            isUsernameChangeSuccessful = false;
                        }
                        this.clientConnection.write(isUsernameChangeSuccessful);
                        break;
                    case "change password":
                        // changes password of user currently logged in
                        boolean isPasswordChangeSuccessful = this.store.changePassword(this.user.getUsername(), this.clientConnection.read());
                        this.clientConnection.write(isPasswordChangeSuccessful);
                        break;
                    case "change age":
                        // changes age of user currently logged in
                        boolean isAgeChangeSuccessful = this.store.changeAge(this.user.getUsername(), Integer.parseInt(this.clientConnection.read().toString()));
                        this.clientConnection.write(isAgeChangeSuccessful);
                        break;
                    case "change product name":
                        // changes chosen product name from "get product by name"
                        String newName = this.clientConnection.read().toString();
                        try {
                            this.store.changeProductName(this.product.getName(), newName);
                            this.clientConnection.write(true);
                        } catch (IllegalArgumentException | ProductAlreadyExistsException ex) {
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
                        } catch (IllegalArgumentException ex) {
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
                        } catch (IllegalArgumentException ex) {
                            this.clientConnection.write(false);
                        }
                        break;
                    case "change product quantity":
                        // changes chosen product quantity from "get product by name"
                        int quantity = this.clientConnection.read();
                        try {
                            this.store.changeProductQuantity(this.product.getName(), quantity);
                            this.clientConnection.write(true);
                        } catch (IllegalArgumentException ex) {
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
        } catch (ClassNotFoundException | CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sens a clone of User object through ObjectOuputStream defined in class ClientThread
     *
     * @param baseUser User object sent through the ObjectOutpostStream
     * @throws IOException                if I/O error occuts
     * @throws CloneNotSupportedException if the User object is not cloneable
     */
    private void sendUserThroughDataStream(User baseUser) throws IOException, CloneNotSupportedException {
        if (baseUser == null) {
            this.clientConnection.write(null);

        } else {
            this.clientConnection.write(baseUser.clone());
        }
    }
}
