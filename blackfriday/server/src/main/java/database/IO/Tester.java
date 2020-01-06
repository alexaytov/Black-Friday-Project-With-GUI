package database.IO;

import store.Store;

import java.io.File;
import java.io.IOException;

public class Tester {

    public Tester() throws IOException {
    }

    public static void main(String[] args) throws Exception {
        final String CLIENT_DATABASE_FILE_NAME = "src/Database/clientsDB.txt";
        final String STAFF_DATABASE_FILE_NAME = "src/Database/staffDB.txt";
        final String PRODUCT_DATABASE_FILE_NAME = "src/Database/productDB.txt";
        final String PURCHASES_DATABASE_FAIL_NAME = "src/Database/purchaseDB.txt";

        Store store = new Store(CLIENT_DATABASE_FILE_NAME, STAFF_DATABASE_FILE_NAME, PRODUCT_DATABASE_FILE_NAME, PURCHASES_DATABASE_FAIL_NAME);



        File file = new File("src/Database/images/shipping-cart.png");

//        byte[] imageContent = Files.readAllBytes(file.toPath());

//        Product product = new Product("name", "descirption", 2, 10, 2, 0, imageContent, "ads");
////        Product product1 = new Product("nam1", "descirption", 2, 10, 2, 0, imageContent, "ads");
////        Product product2 = new Product("nam2", "descirption", 2, 10, 2, 0, imageContent, "ads");
////        Product product3 = new Product("nam3", "descirption", 2, 10, 2, 0, imageContent, "ads");
////        Product product4 = new Product("nam4", "descirption", 2, 10, 2, 0, imageContent, "ads");
////        Product product5 = new Product("nam5", "descirption", 2, 10, 2, 0, imageContent, "ads");
////        Product product6 = new Product("nam6", "descirption", 2, 10, 2, 0, imageContent, "ads");
////        Product product7 = new Product("nam7", "descirption", 2, 10, 2, 0, imageContent, "ads");
////
////        store.addProduct(product);
////        store.addProduct(product1);
////        store.addProduct(product2);
////        store.addProduct(product3);
////        store.addProduct(product4);
////        store.addProduct(product5);
////        store.addProduct(product6);
////        store.addProduct(product7);
//
//        Product product = store.getProductByName("nam1");
//        System.out.println(product.getMinimumPrice());

//        Product product = new Product("productWithoutSize", "descirption", 2, 10, 2, 0, "filePath");

//        System.out.println(store.getProductByName("productWithoutSize").getMinimumPrice());


//        Product p1 = new Product("name", "descr", 10, 2.22, 2, 0);
//        Product p2 = new Product("name1", "descr1", 10, 2.22, 2, 0);
//        Product p3 = new Product("name2", "descr2", 10, 2.22, 2, 0);
//        Map<String, Product> productMap = new HashMap<>();
//        productMap.put(p1.getName(), p1);
//        productMap.put(p2.getName(), p2);
//        productMap.put(p3.getName(), p3);
//        System.out.println();
//        JSONWriter.writeProducts(productMap, "text.txt");

//        Staff staff = new Staff("staf", "staf", "alex", "asd", 1);
//        Map<String, User> userMap = new HashMap<>();
//        userMap.put(staff.getUsername(), staff);
//        JSONWriter.writeUsers(userMap, STAFF_DATABASE_FAIL_NAME);
//        System.out.println();
//        Store store = new Store();
//        System.out.println(store.loginAsStaff("staf", "staf").getPassword());
//        Purchase purchase = new Purchase("asd", "asd", 2, 2);
//        Map<String, List<Purchase>> purchaseMap = new HashMap<>();
//        purchaseMap.put(purchase.getUserName(), new ArrayList<>());
//        purchaseMap.get(purchase.getUserName()).add(purchase);
//        JSONWriter.writePurchase(purchaseMap, PURCHASES_DATABASE_FAIL_NAME);
//
//        HashMap<String, String> pesho = new HashMap<>();
//        pesho.put("alex", "alex");
//        System.out.println();
//
//        System.out.println( JSONReader.readPurchases(PURCHASES_DATABASE_FAIL_NAME).size());

//        JSONWriter.writePurchase(purchaseMap, PURCHASES_DATABASE_FAIL_NAME);
//        Map<String, Purchase> productMap = JSONReader.readPurchases(PURCHASES_DATABASE_FAIL_NAME);
//        System.out.println(productMap.size());


    }


}
