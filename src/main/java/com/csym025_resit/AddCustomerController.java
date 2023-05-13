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
    private Button backButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        genderMaleInput.setSelected(true);
    }

    public void switchToCustomerScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStockScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Stock.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRentScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Rent.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addCustomer() throws IOException, ClassNotFoundException {
        Customer customer = new Customer();
        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
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

        File f = new File(pathname);
        if (f.exists()) {
            Customer[] customers = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            customers = (Customer[]) in.readObject();
            in.close();
            fileIn.close();

            Customer[] newCustomers = Arrays.copyOf(customers, customers.length + 1);
            newCustomers[customers.length] = customer;

            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newCustomers);
            out.close();
            fileOut.close();
        } else {
            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new Customer[] { customer });
            out.close();
            fileOut.close();
        }

        backButton.fire();
    }
}