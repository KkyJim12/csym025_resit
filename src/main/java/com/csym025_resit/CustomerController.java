package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.csym025_resit.Holder.CustomerHolder;
import com.csym025_resit.Model.Customer;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CustomerController {
    @FXML
    private Button customerLinkButton;

    @FXML
    private ScrollPane showArea;

    @FXML
    private TextField searchInput;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Initiate function when open scene
    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        getAllCustomers();
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

    // Switch to add customer scene
    public void switchToAddCustomerScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Switch to edit customer scene
    private class switchToEditCustomerScreen implements EventHandler<Event> {
        @Override
        public void handle(Event evt) {
            try {
                // Get id from button
                String elementId = ((Button) evt.getSource()).getId();

                // Store id in holder class
                CustomerHolder holder = CustomerHolder.getInstance();
                holder.setCustomer(elementId);

                root = FXMLLoader.load(getClass().getResource("EditCustomer.fxml"));
                stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // Search function
    public void search() throws IOException, ClassNotFoundException, FileNotFoundException {
        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
        File f = new File(pathname);

        // Check if file exist
        if (f.exists()) {
            Customer[] customers = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            customers = (Customer[]) in.readObject();
            in.close();
            fileIn.close();

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(5));
            grid.setVgap(10);

            // Get only data that contain search value
            for (int i = 0; i < customers.length; i++) {
                if (customers[i].fullName.contains(searchInput.getText())) {
                    Label fullName = new Label(customers[i].fullName);
                    fullName.setStyle("-fx-font-size:32; -fx-font-weight:bold; -fx-font-family: Segoe UI");
                    fullName.setTextFill(Color.color(1, 1, 1));
                    Label email = new Label("Email: " + customers[i].email);
                    email.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    email.setTextFill(Color.color(1, 1, 1));
                    Label phone = new Label("Phone: " + customers[i].phone);
                    phone.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    phone.setTextFill(Color.color(1, 1, 1));
                    Label address = new Label("Address: " + customers[i].address);
                    address.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    address.setTextFill(Color.color(1, 1, 1));
                    Label gender = new Label("Gender: " + customers[i].gender);
                    gender.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    gender.setTextFill(Color.color(1, 1, 1));
                    Button editButton = new Button("Edit");
                    editButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#eab308");
                    editButton.setTextFill(Color.color(1, 1, 1));
                    editButton.setId(customers[i].id);
                    editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new switchToEditCustomerScreen());
                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#f97316");
                    deleteButton.setTextFill(Color.color(1, 1, 1));
                    deleteButton.setId(customers[i].id);
                    deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new deleteCustomer());
                    HBox manageSection = new HBox(editButton, deleteButton);
                    manageSection.setSpacing(10);

                    VBox customerCard = new VBox();
                    customerCard.getChildren().add(fullName);
                    customerCard.getChildren().add(email);
                    customerCard.getChildren().add(phone);
                    customerCard.getChildren().add(address);
                    customerCard.getChildren().add(gender);
                    customerCard.getChildren().add(manageSection);

                    customerCard.setPrefSize(425, 150);
                    customerCard.setPadding(new Insets(10, 20, 10, 20));
                    customerCard.setStyle("-fx-background-color:#ef4444");
                    customerCard.setId("customerCard");

                    grid.add(customerCard, 1, i);
                }

                showArea.setContent(grid);
                showArea.setPannable(true);
            }

        }
    }

    // Get all customers
    public void getAllCustomers() throws IOException, ClassNotFoundException, FileNotFoundException {

        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
        File f = new File(pathname);

        // Check if file exist
        if (f.exists()) {
            Customer[] customers = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            customers = (Customer[]) in.readObject();
            in.close();
            fileIn.close();

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(5));
            grid.setVgap(10);

            // Create card from looping data
            for (int i = 0; i < customers.length; i++) {
                Label fullName = new Label(customers[i].fullName);
                fullName.setStyle("-fx-font-size:32; -fx-font-weight:bold; -fx-font-family: Segoe UI");
                fullName.setTextFill(Color.color(1, 1, 1));
                Label email = new Label("Email: " + customers[i].email);
                email.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                email.setTextFill(Color.color(1, 1, 1));
                Label phone = new Label("Phone: " + customers[i].phone);
                phone.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                phone.setTextFill(Color.color(1, 1, 1));
                Label address = new Label("Address: " + customers[i].address);
                address.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                address.setTextFill(Color.color(1, 1, 1));
                Label gender = new Label("Gender: " + customers[i].gender);
                gender.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                gender.setTextFill(Color.color(1, 1, 1));
                Button editButton = new Button("Edit");
                editButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#eab308");
                editButton.setTextFill(Color.color(1, 1, 1));
                editButton.setId(customers[i].id);
                editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new switchToEditCustomerScreen());
                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#f97316");
                deleteButton.setTextFill(Color.color(1, 1, 1));
                deleteButton.setId(customers[i].id);
                deleteButton.getStyleClass().add("deleteButton");
                deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new deleteCustomer());
                HBox manageSection = new HBox(editButton, deleteButton);
                manageSection.setSpacing(10);

                VBox customerCard = new VBox();
                customerCard.getChildren().add(fullName);
                customerCard.getChildren().add(email);
                customerCard.getChildren().add(phone);
                customerCard.getChildren().add(address);
                customerCard.getChildren().add(gender);
                customerCard.getChildren().add(manageSection);

                customerCard.setPrefSize(425, 150);
                customerCard.setPadding(new Insets(10, 20, 10, 20));
                customerCard.setStyle("-fx-background-color:#ef4444");
                customerCard.setId("customerCard");

                grid.add(customerCard, 1, i);
            }

            // Show data to assign component
            showArea.setContent(grid);
            showArea.setPannable(true);

        }
    }

    // Delete customer function
    private class deleteCustomer implements EventHandler<Event> {
        @Override
        public void handle(Event evt) {
            try {

                // Get id from button
                String elementId = ((Button) evt.getSource()).getId();

                String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
                File f = new File(pathname);

                // Check if file exist and delete that data
                if (f.exists()) {
                    Customer[] customers = null;
                    FileInputStream fileIn = new FileInputStream(pathname);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    customers = (Customer[]) in.readObject();
                    in.close();
                    fileIn.close();

                    Customer[] newCustomers = new Customer[customers.length - 1];

                    for (int i = 0, k = 0; i < customers.length; i++) {
                        if (!(customers[i].id).equals(elementId)) {
                            newCustomers[k] = customers[i];
                            k++;
                        }
                    }

                    FileOutputStream fileOut = new FileOutputStream(pathname);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(newCustomers);
                    out.close();
                    fileOut.close();

                    // Call get all customers function again
                    getAllCustomers();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}