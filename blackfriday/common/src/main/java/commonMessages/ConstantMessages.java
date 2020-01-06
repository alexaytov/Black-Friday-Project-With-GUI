package commonMessages;

import javafx.scene.control.Alert;

public class ConstantMessages {
    public static final String WELCOME_MESSAGE = "Welcome %s %s";
    public static final String ENTER_QUANTITY = "Please enter quantity: ";
    public static final String SUCCESSFUL_PURCHASE = "You just successfully purchased %d %s!";
    public static final String UNSUCCESSFUL_PURCHASE = "Sorry there was a problem with your purchase please try again!";
    public static final String GO_BACK = "Go back";
    public static final String EARNINGS_FOR_DATE = "Your earnings for year: %d, month: %d and date: %d are %.2f";
    public static final String INPUT_YEAR = "Please enter year: ";
    public static final String INPUT_MONTH = "Please enter month as a number: ";
    public static final String INPUT_DAY = "Please enter day of the month: ";
    public static final String INPUT_START_PERIOD = "Please enter start of the period!";
    public static final String INPUT_END_PERIOD = "Please enter end of the period!";
    public static final String EARNINGS_PERIOD = "Your earnings are %.2f";
    public static final String EARNINGS_YEAR = "Your earnings for %d year are %.2f";
    public static final String EARNINGS_MONTH = "Your earnings for %d month are %.2f";
    public static final String STAFF_REGISTERED = "You registered staff successfully!";
    public static final String STAFF_ALREADY_EXISTS = "Staff with the same username already exists!";
    public static final String REGISTERED_SUCCESSFULLY = "You were registered successfully";
    public static final String CLIENT_ALREADY_EXISTS = "There is already a user with the same username please try registering again";
    public static final String INPUT_PRODUCT_NAME = "Enter product name: ";
    public static final String NO_DISCOUNTED_PRODUCTS = "There are no discounted products at the moment!";
    public static final String PRODUCT_CREATED = "The product was created successfully!";
    public static final String INPUT_PRODUCT_DESCRIPTION = "Enter product new description: ";
    public static final String INPUT_PRODUCT_QUANTITY = "Enter product new quantity: ";
    public static final String INPUT_PRODUCT_MINIMUM_PRICE = "Enter minimum price: ";
    public static final String INPUT_PRODUCT_DISCOUNT_PERCENT = "Enter product new discount percent: ";
    public static final String INPUT_PRODUCT_AGE = "Enter your new age: ";
    public static final String AGE_CHANGE_SUCCESSFUL = "Your age was successfully changed";
    public static final String AGE_CHANGE_UNSUCCESSFUL = "There was a problem changing your age, please try again";
    public static final String INPUT_FIRST_NAME = "Please enter new first name: ";
    public static final String FIRST_NAME_CHANGE_SUCCESSFUL = "First name successfully changed";
    public static final String FIRST_NAME_CHANGE_UNSUCCESSFUL = "There was a problem changing your first name, please try again";
    public static final String INPUT_LAST_NAME = "Please enter your new last name: ";
    public static final String LAST_NAME_CHANGE_SUCCESSFUL = "Last name successfully changed";
    public static final String LAST_NAME_CHANGE_UNSUCCESSFUL = "There was a problem changing your last name, please try again";
    public static final String INPUT_USERNAME = "Please enter your new username: ";
    public static final String USERNAME_CHANGE_SUCCESSFUL = "Username successfully changed";
    public static final String USERNAME_CHANGE_UNSUCCESSFUL = "There was a problem changing your username, please try again";
    public static final String INPUT_PASSWORD = "Please enter your new password: ";
    public static final String PASSWORD_CHANGE_SUCCESSFUL = "Password successfully changed";
    public static final String PASSWORD_CHANGE_UNSUCCESSFUL = "There was a problem changing your password, please try again";
    public static final String CLIENT_DELETED_UNSUCCESSFUL = "There was a problem deleting this account please try again";
    public static final String CLIENT_DELETED_SUCCESSFUL = "Client deleted successfully!";
    public static final String PRODUCT_NAME_CHANGED_SUCCESSFUL = "The product name was successfully changed!";
    public static final String PRODUCT_NAME_CHANGED_UNSUCCESSFUL = "There is a product with the same name please try again!";
    public static final String PRODUCT_DESCRIPTION_CHANGED_SUCCESSFUL = "The product description was successfully changed!";
    public static final String PRODUCT_DESCRIPTION_CHANGED_UNSUCCESSFUL = "There was a problem with changing the product's description";
    public static final String PRODUCT_QUANTITY_CHANGED_SUCCESSFUL = "The product quantity was successfully changed!";
    public static final String PRODUCT_QUANTITY_CHANGED_UNSUCCESSFUL = "There was a problem with changing the product's quantity";
    public static final String PRODUCT_MINIMUM_PRICE_CHANGED_SUCCESSFUL = "The product minimum price was successfully changed!";
    public static final String PRODUCT_MINIMUM_PRICE_CHANGED_UNSUCCESSFUL = "There was a problem with changing the product's minimum price";
    public static final String PRODUCT_DISCOUNT_PERCENT_CHANGED_SUCCESSFUL = "The product discount percent was successfully changed!";
    public static final String PRODUCT_DISCOUNT_PERCENT_CHANGED_UNSUCCESSFUL = "There was a problem with changing the product's discount percent";
    public static final String CLIENT_NOT_EXIST = "Client with username %s doesn't exist!!!";
    public static final String STAFF_DELETED_SUCCESSFUL = "Staff deleted successfully!!!";
    public static final String STAFF_DELETED_UNSUCCESSFUL = "There was a problem deleting this staff account!!!";
    public static final String NO_PRODUCTS = "There are not products at the moment!";
    public static final String ENTER_ZERO_BACK = "Enter 0 to go back";
    public static final String PRODUCT_SIZE_CHANGED_SUCCESSFUL = "The product size was successfully changed!";
    public static final String PRODUCT_SIZE_CHANGED_UNSUCCESSFUL = "There was problem changing the size of the product please try again!";



    public static final String START_BLACK_FRIDAY = "Black friday started!";
    public static final String STOP_BLACK_FRIDAY = "Black friday stopped!";

    public static final String TEXT_FIELD_UNFOCUSED_COLOR = "#4d4d4d";
    public static final String PRODUCT_PRICE_CHANGED_SUCCESSFUL = "Product price was changed successfully!";
    public static final String PRODUCT_PRICE_CHANGED_UNCCESSFUL = "There was a problem changing the price please try again";
    public static final String PRODUCT_IMAGE_CHANGE_SUCCESSFUL = "The image was changed successfully!";
    public static final String PRODUCT_IMAGE_CHANGE_UNSUCCESSFUL = "There was a problem changing the image please try again!";
    public static final String NO_RESULTS = "No Results Found";
    public static final String PRODUCT_DELETED_SUCCESSFULLY = "Product was deleted successfully!";
    public static final String PRODUCT_DELETED_UNSUCCESSFULLY = "There was a problem deleting the product please try again!";

    public static void confirmationPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


}
