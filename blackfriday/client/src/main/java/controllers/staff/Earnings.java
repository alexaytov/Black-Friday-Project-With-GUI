package controllers.staff;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import openjfx.Main;
import util.Operations;
import validator.Validator;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

public class Earnings {

    @FXML
    private Tab dateTab;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXTextField dateEarningsField;

    @FXML
    private Tab yearTab;

    @FXML
    private JFXTextField yYearField;

    @FXML
    private JFXTextField yearEarningsField;

    @FXML
    private Tab monthTab;

    @FXML
    private JFXTextField mYearField;

    @FXML
    private JFXTextField mMonthField;

    @FXML
    private JFXTextField monthEarningsField;

    @FXML
    private Tab customPerdioTab;

    @FXML
    private JFXDatePicker startDatePicker;

    @FXML
    private JFXDatePicker endDatePicker;

    @FXML
    private JFXTextField customPeriodEarningsField;

    @FXML
    private JFXButton backButton;

    @FXML
    void customPeriodEarnings(ActionEvent event) throws IOException, ClassNotFoundException {
        // get data from GUI
        LocalDate startDate = this.startDatePicker.getValue();
        LocalDate endDate = this.endDatePicker.getValue();

        // validation of entered data
        if (endDate.isBefore(startDate) || endDate.equals(startDate)) {
            //end data is before start data
            ExceptionMessages.showWarningDialog(ExceptionMessages.END_DATE_IS_BEFORE_START_DATE);
        } else {
            // entered data is valid
            // sending data to server
            Main.tcpServer.write("earnings period");
            Main.tcpServer.write(startDate);
            Main.tcpServer.write(endDate);

            // get and show earnings to GUI
            double earnings = Main.tcpServer.read();
            this.customPeriodEarningsField.setText(String.valueOf(earnings));

        }
    }

    @FXML
    void dateEarningsButton(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            // get and validate year
            LocalDate date = this.datePicker.getValue();

            // send command and year to server
            Main.tcpServer.write("earnings date");
            Main.tcpServer.write(date.toString());

            // get earnings from server
            double earnings = Main.tcpServer.read();
            dateEarningsField.setText(String.valueOf(earnings));
        } catch (DateTimeException | IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void monthEarnings(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            // get year and mont from GUI
            int year = Integer.parseInt(this.mYearField.getText());
            int month = Integer.parseInt(this.mMonthField.getText());

            // send data to server
            Main.tcpServer.write("earnings month");
            Main.tcpServer.write(month);
            Main.tcpServer.write(year);

            // show data to GUI
            double earnings = Main.tcpServer.read();
            this.monthEarningsField.setText(String.valueOf(earnings));


        } catch (NumberFormatException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.ENTER_NUMBER);
        }
    }

    @FXML
    void yearEarnings(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            // get and validate year
            int year = Integer.parseInt(yYearField.getText());
            Validator.requireNonNegative(year, ExceptionMessages.YEAR_MUST_BE_POSITIVE);

            // send command and year to server
            Main.tcpServer.write("earnings year");
            Main.tcpServer.write(year);

            // get earnings from server
            double earnings = Main.tcpServer.read();
            yearEarningsField.setText(String.valueOf(earnings));

        } catch (NumberFormatException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.ENTER_NUMBER);
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.INVALID_NUMBER);
        }

    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        Operations.changeWindows(this.datePicker, "staffLoggedIn", "/view/staff/staffLoggedIn.fxml", this.getClass(), 600, 600);
    }

}
