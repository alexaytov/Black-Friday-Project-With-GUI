package commonMessages;

import javafx.scene.control.Alert;

public class ExceptionMessages {

    public static final String PRODUCT_NOT_FOUND = "Product with name %s doesn't exist!!!";
    public static final String USER_NOT_FOUND = "User with name %s doesn't exist!!!";
    public static final String WRONG_PASSWORD = "You have entered incorrect password!!!";
    public static final String INVALID_NUMBER = "You have entered an invalid number!!!";
    public static final String FILL_IN_ALL_FIELDS = "Please fill in all the fields above!!!";
    public static final String ENTER_NUMBER = "Please enter a number!!!";
    public static final String END_DATE_IS_BEFORE_START_DATE = "End date can't be before start date!!!";
    public static String AGE_MUST_BE_POSITIVE_NUMBER = "Age must be a positive number!!!";
    public static String STRING_NULL_OR_EMPTY = "Text can't be null or empty!!!";
    public static String PASSWORD_NULL_OR_EMPTY = "Password can't be null or empty!!!";
    public static String DATE_NULL = "Date object can't be null!!!";
    public static String MINIMUM_PRICE_MUST_BE_POSITIVE = "Minimum price has to be a positive number!!!";
    public static String QUANTITY_ZERO_OR_NEGATIVE = "Quantity can't be zero or negative!!!";
    public static String DESCRIPTION_NULL_OR_EMPTY = "Description can't be null or empty string!!!";
    public static String SIZE_NULL_OR_EMPTY = "Size can't be null or empty string!!!";
    public static String PRICE_ZERO_NEGATIVE = "Price can't be zero or negative!!!";
    public static String PRICE_BELOW_MINIMUM_PRICE = "Price can't be below the minimum price!!!";
    public static String DISCOUNTED_PRICE_ZERO_NEGATIVE = "Discounted price can't be zero or negative!!!";
    public static String DISCOUNTED_PRICE_BELOW_MINIMUM_PRICE = "Discounted price can't be below the minimum price!!!";
    public static String NOT_ENOUGH_QUANTITY = "There are only %d from %s. You can't buy %d!!!";
    public static String ENTER_NUMBER_MONTH_DATE_YEAR = "Please enter a number for the date month and year!!!";
    public static String INVALID_DATE = "You have entered an invalid date!!!";
    public static String Product_ALREADY_EXISTS = "Product with the same name already exists!!!";
    public static String NAME_NULL_OR_EMPTY = "Name can't be null or empty!!!";
    public static String FILE_PATH_NULL_OR_EMPTY = "File path can't be null or empty!!!";
    public static String PRODUCT_ALREADY_EXISTS = "Product with the name %s alread exists!!!";
    public static String MONTH_MUST_BE_BETWEEN_0_1 = "Month %d must be between 1 and 12!!!";
    public static String YEAR_MUST_BE_POSITIVE = "Year %d must be a positive number!!!";
    public static String CONNECTION_NULL = "Connection can't be null!!!";
    public static String DATABASE_MAP_NULL = "Database map can't be null";


    public static void showWarningDialog(String warningMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(warningMessage);

        alert.showAndWait();
    }

}
