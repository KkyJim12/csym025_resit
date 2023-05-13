package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import com.csym025_resit.Holder.CustomerHolder;
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

public class EditCustomerController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button updateButton;

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

    public void initialize() throws ClassNotFoundException {
        getCustomer();
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

    public void updateCustomer() throws IOException, ClassNotFoundException {
        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
        CustomerHolder holder = CustomerHolder.getInstance();
        String customerId = holder.getCustomer();

        System.out.println(customerId);

        File f = new File(pathname);
        if (f.exists()) {
            Customer customer = null;
            Customer[] customers = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            customers = (Customer[]) in.readObject();
            in.close();
            fileIn.close();

            int index = 0;

            for (int i = 0; i < customers.length; i++) {
                if ((customers[i].id).equals(customerId)) {
                    customer = customers[i];
                    index = i;
                    break;
                }
            }

            customer.fullName = fullNameInput.getText();
            customer.email = emailInput.getText();
            customer.phone = phoneInput.getText();
            customer.address = addressInput.getText();

            if (genderMaleInput.isSelected()) {
                customer.gender = "Male";
            } else {
                customer.gender = "Female";
            }

            Customer[] newCustomers = Arrays.copyOf(customers, customers.length);
            newCustomers[index] = customer;

            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newCustomers);
            out.close();
            fileOut.close();
        }

        backButton.fire();
    }

    public void getCustomer() {
        try {
            String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
            File f = new File(pathname);

            CustomerHolder holder = CustomerHolder.getInstance();
            String customerId = holder.getCustomer();
            if (f.exists()) {
                Customer customer = null;
                Customer[] customers = null;
                FileInputStream fileIn = new FileInputStream(pathname);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                customers = (Customer[]) in.readObject();
                in.close();
                fileIn.close();

                for (int i = 0; i < customers.length; i++) {
                    if ((customers[i].id).equals(customerId)) {
                        customer = customers[i];
                        break;
                    }
                }

                fullNameInput.setText(customer.fullName);
                emailInput.setText(customer.email);
                phoneInput.setText(customer.phone);
                addressInput.setText(customer.address);

                System.out.println(customer.gender);

                if (customer.gender.equals("Male")) {
                    genderMaleInput.setSelected(true);
                } else {
                    genderFemaleInput.setSelected(true);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
