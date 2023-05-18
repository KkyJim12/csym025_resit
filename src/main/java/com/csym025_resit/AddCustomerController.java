package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.UUID;

import com.csym025_resit.Model.Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCustomerController {

    @FXML
    private Button customerLinkButton;

    @FXML
    private TextField fullNameInput;

    @FXML
    private TextField emailInput;

    @FXML
    private TextField phoneInput;

    @FXML
    private TextArea addressInput;

    @FXML
    private RadioButton genderMaleInput;

    @FXML
    private RadioButton genderFemaleInput;

    @FXML
    private Label fullNameInputError;

    @FXML
    private Label emailInputError;

    @FXML
    private Label phoneInputError;

    @FXML
    private Label addressInputError;

    @FXML
    private Button backButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Initial function
    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        // Set choicebox default
        genderMaleInput.setSelected(true);
    }

    // Switch to customer scene
    public void switchToCustomerScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Switch to stock scene
    public void switchToStockScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Stock.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Switch to rent scene
    public void switchToRentScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Rent.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Check if string char is number
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    // Reset all error labels
    public void resetValidate() throws IOException, ClassNotFoundException {
        fullNameInputError.setText("");
        emailInputError.setText("");
        phoneInputError.setText("");
        addressInputError.setText("");
    }

    // Validate add customer function
    public Boolean validateAddCustomer() throws IOException, ClassNotFoundException {

        // Init result to true
        Boolean result = true;
        // Check customer name
        if (fullNameInput.getText().isEmpty()) {
            fullNameInputError.setText("Please inform customer name.");
            result = false;
        }
        // Check email
        if (emailInput.getText().isEmpty()) {
            emailInputError.setText("Please inform email.");
            result = false;
        }
        // Check phone
        if (!isNumeric(phoneInput.getText())) {
            phoneInputError.setText("Please inform phone as number");
            result = false;
        }
        if (phoneInput.getText().isEmpty()) {
            phoneInputError.setText("Please inform phone.");
            result = false;
        }

        // Check address
        if (addressInput.getText().isEmpty()) {
            addressInputError.setText("Please inform address.");
            result = false;
        }

        // Return result
        return result;
    }

    // Add customer function
    public void addCustomer() throws IOException, ClassNotFoundException {
        // Reset validation first every run this functions
        resetValidate();
        // If validation exist stop continue
        if (validateAddCustomer() == false) {
            return;
        }

        // Create customer from model class and assign value from inputs
        Customer customer = new Customer();
        customer.id = UUID.randomUUID().toString();
        customer.fullName = fullNameInput.getText();
        customer.email = emailInput.getText();
        customer.phone = phoneInput.getText();
        customer.address = addressInput.getText();
        if (genderMaleInput.isSelected()) {
            customer.gender = "Male";
        } else {
            customer.gender = "Female";
        }

        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
        File f = new File(pathname);
        // Check if serialization file exist
        if (f.exists()) {
            // Get data from serialization data
            Customer[] customers = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            customers = (Customer[]) in.readObject();
            in.close();
            fileIn.close();

            // Add new data to serialization
            Customer[] newCustomers = Arrays.copyOf(customers, customers.length + 1);
            newCustomers[customers.length] = customer;

            // Store a new data
            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newCustomers);
            out.close();
            fileOut.close();
        } else {
            // If not create a new serialization file and store a new data
            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new Customer[] { customer });
            out.close();
            fileOut.close();
        }

        // Click on back button to return back to main scene 
        backButton.fire();
    }
}