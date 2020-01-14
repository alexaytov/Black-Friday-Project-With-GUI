package controllers.staff;

import application.App;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import commonMessages.CommandNames;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import util.Operations;
import util.Windows;
import validator.Validator;

import java.time.DateTimeException;
import java.time.LocalDate;

import static util.Operations.showWarningDialog;
import static validator.Validator.requireNonNegative;
import static validator.Validator.validateMonth;

public class Earnings {

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXTextField dateEarningsField;

    @FXML
    private JFXTextField yYearField;

    @FXML
    private JFXTextField yearEarningsField;

    @FXML
    private JFXTextField mYearField;

    @FXML
    private JFXTextField mMonthField;

    @FXML
    private JFXTextField monthEarningsField;

    @FXML
    private JFXDatePicker startDatePicker;

    @FXML
    private JFXDatePicker endDatePicker;

    @FXML
    private JFXTextField customPeriodEarningsField;

    @FXML
    void customPeriodEarnings(ActionEvent event) {
        // get data from GUI
        LocalDate startDate = this.startDatePicker.getValue();
        LocalDate endDate = this.endDatePicker.getValue();
        if (startDate == null || endDate == null) {
            showWarningDialog("Start date or End date can't be null!");
        } else {
            // validation of entered data
            if (endDate.isBefore(startDate) || endDate.equals(startDate)) {
                //end data is before start data
                showWarningDialog(ExceptionMessages.END_DATE_IS_BEFORE_START_DATE);
            } else {
                // entered data is valid
                // sending data to server
                App.serverConnection.write(CommandNames.EARNINGS_FOR_PERIOD);
                App.serverConnection.write(startDate);
                App.serverConnection.write(endDate);
                // get and show earnings to GUI
                double earnings = App.serverConnection.read();
                this.customPeriodEarningsField.setText(String.valueOf(earnings));
            }
        }
    }

    @FXML
    void dateEarningsButton(ActionEvent event) {
        try {
            // get and validate year
            LocalDate date = this.datePicker.getValue();
            if (date == null) {
                showWarningDialog(ExceptionMessages.DATE_NULL);
            } else {
                // send command and year to server
                App.serverConnection.write(CommandNames.EARNINGS_FOR_DATE);
                App.serverConnection.write(date.toString());
                // get earnings from server
                double earnings = App.serverConnection.read();
                dateEarningsField.setText(String.valueOf(earnings));
            }
        } catch (DateTimeException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void monthEarnings(ActionEvent event) {
        try {
            // get year and mont from GUI
            int year = Integer.parseInt(this.mYearField.getText());
            int month = Integer.parseInt(this.mMonthField.getText());
            // validate data
            validateMonth(month);
            requireNonNegative(year, ExceptionMessages.YEAR_MUST_BE_POSITIVE);
            // send data to server
            App.serverConnection.write(CommandNames.EARNINGS_FOR_MONTH);
            App.serverConnection.write(month);
            App.serverConnection.write(year);
            // show data to GUI
            double earnings = App.serverConnection.read();
            this.monthEarningsField.setText(String.valueOf(earnings));
        } catch (NumberFormatException ex) {
            showWarningDialog(ExceptionMessages.ENTER_NUMBER);
        } catch (IllegalArgumentException ex) {
            showWarningDialog(ex.getMessage());
        }
    }

    @FXML
    void yearEarnings(ActionEvent event) {
        try {
            // get and validate year
            int year = Integer.parseInt(yYearField.getText());
            Validator.requireNonNegative(year, ExceptionMessages.YEAR_MUST_BE_POSITIVE);
            // send command and year to server
            App.serverConnection.write(CommandNames.EARNINGS_FOR_YEAR);
            App.serverConnection.write(year);
            // get earnings from server
            double earnings = App.serverConnection.read();
            yearEarningsField.setText(String.valueOf(earnings));
        } catch (NumberFormatException ex) {
            showWarningDialog(ExceptionMessages.ENTER_NUMBER);
        } catch (IllegalArgumentException ex) {
            showWarningDialog(ExceptionMessages.INVALID_NUMBER);
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        // go to staff logged in window
        this.datePicker.getScene().getWindow().hide();
        Operations.loadWindow(Windows.STAFF_LOGGED_IN_PATH, Windows.STAFF_LOGGED_IN_WIDTH, Windows.STAFF_LOGGED_IN_HEIGHT);
    }
}
