package server;

import exceptions.*;
import product.Product;
import store.Store;
import user.BaseUser;
import user.Client;
import user.Staff;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;

public class ClientThread implements Runnable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private BaseUser user;
    private Store store;
    private Product product;


    public ClientThread(Socket socket, Store store) throws IOException {
        this.store = store;
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(socket.getInputStream());

    }

    public void run() {
        try {
            while (!socket.isClosed()) {
                String command = (String) this.objectInputStream.readObject();

                switch (command) {
                    case "login":
                        //tokens
                        //1 -> user type 2 -> username 3 -> password
                        String[] tokens = ((String) this.objectInputStream.readObject()).split("\\s+");
                        try {
                            if (tokens[0].equals("client")) {
                                this.user = this.store.loginAsClient(tokens[1], tokens[2]);
                            } else {
                                this.user = this.store.loginAsStaff(tokens[1], tokens[2]);
                            }
                            this.objectOutputStream.writeObject(user.clone());
                        } catch (WrongPasswordException | NotFoundException ex) {
                            this.objectOutputStream.writeObject(null);
                            this.objectOutputStream.writeObject(ex.getClass().getSimpleName());
                        }
                        break;
                    case "register client":
                        Client client = (Client) this.objectInputStream.readObject();
                        try {
                            this.store.registerClient(client);
                            this.objectOutputStream.writeObject("true");
                        } catch (UserAlreadyExistsException ex) {
                            this.objectOutputStream.writeObject("false");
                        }
                        break;
                    case "register staff":
                        Staff staff = (Staff) this.objectInputStream.readObject();
                        try {
                            this.store.registerStaff(staff);
                            this.objectOutputStream.writeObject(true);
                        } catch (UserAlreadyExistsException e) {
                            this.objectOutputStream.writeObject(false);
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
                        this.objectOutputStream.writeObject(this.store.isBlackFriday());
                        break;
                    case "get client products": {
                        //gets all product names for client
                        this.objectOutputStream.writeObject(this.store.getClientProducts());

                    }
                    break;
                    case "change product minimum price":
                        double minimumPrice = (double) this.objectInputStream.readObject();
                        try {
                            this.product.setMinimumPrice(minimumPrice);
                            this.objectOutputStream.writeObject(true);
                        } catch (IllegalArgumentException ex) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "change product price":
                        double newPrice = (double) this.objectInputStream.readObject();
                        try {
                            this.store.changeProductPrice(this.product.getName(), newPrice);
                            this.objectOutputStream.writeObject(true);
                        } catch (IllegalArgumentException ex) {
                            this.objectOutputStream.writeObject(false);
                        }

                        break;
                    case "get client discounted products":
                        this.objectOutputStream.writeObject(this.store.getClientDiscountedProducts());
                        break;
                    case "search staff all products": {
                        String searchedAllProductsName = this.objectInputStream.readObject().toString();
                        this.objectOutputStream.writeObject(this.store.searchAllProducts(searchedAllProductsName));
                        break;
                    }
                    case "search staff discounted products": {
                        String searchedDiscountedProductsName = this.objectInputStream.readObject().toString();
                        this.objectOutputStream.writeObject(this.store.searchDiscountedProducts(searchedDiscountedProductsName));
                        break;
                    }
                    case "search client all products":
                        String searchedAllProductsName = this.objectInputStream.readObject().toString();
                        this.objectOutputStream.writeObject(this.store.searchAllProducts(searchedAllProductsName));
                        break;
                    case "search client discounted products":
                        String searchedDiscountedProductsName = this.objectInputStream.readObject().toString();
                        this.objectOutputStream.writeObject(this.store.searchDiscountedProducts(searchedDiscountedProductsName));
                        break;
                    case "search quantity control":
                        int maximumQuantity = (int) this.objectInputStream.readObject();
                        this.objectOutputStream.writeObject(this.store.searchQuantityControl(maximumQuantity));
                        break;
                    case "set product":
                        this.product = (Product) this.objectInputStream.readObject();
                        break;
                    case "get product by name":
                        String name = this.objectInputStream.readObject().toString();
                        Product chosenProduct;
                        try {
                            chosenProduct = this.store.getProductByName(name);
                            this.product = chosenProduct;
                            this.objectOutputStream.writeObject(chosenProduct.clone());
                        } catch (NotFoundException e) {
                            this.product = null;
                            this.objectOutputStream.writeObject(null);
                        }
                        break;
                    case "product exists":
                        try {
                            store.getProductByName(this.objectInputStream.readObject().toString());
                            this.objectOutputStream.writeObject(true);
                        } catch (NotFoundException e) {
                            this.objectOutputStream.writeObject(false);
                        }
                    case "buy product": {
                        String productName = this.objectInputStream.readObject().toString();
                        int quantity = (int) this.objectInputStream.readObject();
                        boolean isBought = false;
                        try {
                            isBought = this.store.buyProduct(productName, this.user, quantity);
                            this.objectOutputStream.writeObject(isBought);
                        } catch (NotFoundException | NotEnoughQuantityException e) {
                            this.objectOutputStream.writeObject(false);
                        }
                    }
                    break;
                    case "create product":
                        Product product = (Product) this.objectInputStream.readObject();
                        try {
                            this.store.addProduct(product);
                            this.objectOutputStream.writeObject(true);
                        } catch (ProductAlreadyExistsException e) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "delete client":
                        //delete currently logged in client
                        try {
                            this.store.deleteClient(this.user.getUsername());
                            this.objectOutputStream.writeObject(true);
                        } catch (NotFoundException ex) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "delete staff":
                        // deletes staff account currently logged in
                        try {
                            this.store.deleteStaff(this.user.getUsername());
                            this.objectOutputStream.writeObject(true);
                        } catch (NotFoundException ex) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "get all clients information":
                        this.objectOutputStream.writeObject(store.getClientPurchases());
                        break;
                    case "delete client by username":
                        String usernameOfClientToBeDeleted = this.objectInputStream.readObject().toString();
                        try {
                            store.deleteClient(usernameOfClientToBeDeleted);
                            this.objectOutputStream.writeObject(true);
                        } catch (NotFoundException e) {
                            this.objectOutputStream.writeObject(false);
                        }
                        this.objectOutputStream.writeObject(true);

                        break;
                    case "change first name":
                        // changes first name of user currently logged in
                        boolean isChangeFirstNameSuccessful = this.store.changeUserFirstName(this.user, (String) this.objectInputStream.readObject());
                        this.objectOutputStream.writeObject(isChangeFirstNameSuccessful);
                        break;
                    case "change last name":
                        // changes last name of user currently logged in
                        boolean isLastNameChangeSuccessful = this.store.changeUserLastName(this.user, (String) this.objectInputStream.readObject());
                        this.objectOutputStream.writeObject(isLastNameChangeSuccessful);
                        break;
                    case "change username":
                        // changes username of user currently logged in
                        boolean isUsernameChangeSuccessful;
                        try {
                            isUsernameChangeSuccessful = this.store.changeUsername(this.user, (String) this.objectInputStream.readObject());
                        } catch (UserAlreadyExistsException e) {
                            isUsernameChangeSuccessful = false;
                        }
                        this.objectOutputStream.writeObject(isUsernameChangeSuccessful);
                        break;
                    case "change password":
                        // changes password of user currently logged in
                        boolean isPasswordChangeSuccessful = this.store.changePassword(this.user, (String) this.objectInputStream.readObject());
                        this.objectOutputStream.writeObject(isPasswordChangeSuccessful);
                        break;
                    case "change age":
                        // changes age of user currently logged in
                        boolean isAgeChangeSuccessful = this.store.changeAge(this.user, Integer.parseInt(this.objectInputStream.readObject().toString()));
                        this.objectOutputStream.writeObject(isAgeChangeSuccessful);
                        break;
                    case "change product name":
                        // changes chosen product name from "get product by name"
                        String newName = this.objectInputStream.readObject().toString();
                        try {
                            this.store.changeProductName(this.product, newName);
                            this.objectOutputStream.writeObject(true);
                        } catch (IllegalArgumentException | NotFoundException | ProductAlreadyExistsException ex) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "change product image":
                        byte[] newImageContent = (byte[]) this.objectInputStream.readObject();
                        try {
                            this.store.changeProductImage(this.product.getName(), newImageContent);
                            this.objectOutputStream.writeObject(true);
                        } catch (NullPointerException ex) {
                            this.objectOutputStream.writeObject(false);
                        }


                        break;
                    case "change product description":
                        // changes chosen product description from "get product by name"
                        String newDescription = this.objectInputStream.readObject().toString();
                        try {
                            this.store.changeProductDescription(this.product.getName(), newDescription);
                            this.objectOutputStream.writeObject(true);
                        } catch (IllegalArgumentException | NotFoundException ex) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "change product size":
                        String newSize = this.objectInputStream.readObject().toString();
                        try {
                            this.store.changeProductSize(this.product.getName(), newSize);
                            this.objectOutputStream.writeObject(true);
                        } catch (NotFoundException e) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "change product discount percent":
                        // changes chosen product discount percent from "get product by name"
                        double newDiscountPercentage = (double) this.objectInputStream.readObject();
                        try {
                            this.store.changeProductDiscountPercent(this.product.getName(), newDiscountPercentage);
                            this.objectOutputStream.writeObject(true);
                        } catch (IllegalArgumentException | NotFoundException ex) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "change product quantity":
                        // changes chosen product quantity from "get product by name"
                        int quantity = (int) this.objectInputStream.readObject();
                        try {
                            this.store.changeProductQuantity(this.product.getName(), quantity);
                            this.objectOutputStream.writeObject(true);
                        } catch (IllegalArgumentException | NotFoundException ex) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "get staff products":
                        this.objectOutputStream.writeObject(store.getStaffProducts());
                        break;
                    case "delete product":
                        // deletes product by given name
                        try {
                            store.deleteProduct(this.objectInputStream.readObject().toString());
                            this.objectOutputStream.writeObject(true);
                        } catch (NotFoundException ex) {
                            this.objectOutputStream.writeObject(false);
                        }
                        break;
                    case "exit product options":
                        // sets currently chosen product to null
                        this.product = null;
                        break;
                    case "get staff discounted products":
                        this.objectOutputStream.writeObject(store.getStaffDiscountedProducts());
                        break;
                    case "earnings date":
                        LocalDate date = LocalDate.parse(this.objectInputStream.readObject().toString());
                        this.objectOutputStream.writeObject(store.getEarnings(date));
                        break;
                    case "earnings year": {
                        int year = (int) this.objectInputStream.readObject();
                        this.objectOutputStream.writeObject(store.getEarnings(year));
                    }
                    break;
                    case "earnings month":
                        int month = (int) this.objectInputStream.readObject();
                        int year = (int) this.objectInputStream.readObject();
                        this.objectOutputStream.writeObject(store.getEarnings(month, year));
                        break;
                    case "earnings period":
                        LocalDate startDate = (LocalDate) this.objectInputStream.readObject();
                        LocalDate endDate = (LocalDate) this.objectInputStream.readObject();
                        this.objectOutputStream.writeObject(store.getEarnings(startDate, endDate));
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
            this.objectOutputStream.writeObject(null);

        } else {
            this.objectOutputStream.writeObject(baseUser.clone());
        }
    }
}
