package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.UUID;

import com.csym025_resit.Model.Stock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStockController {

    @FXML
    private TextField productNameInput;

    @FXML
    private TextField categoryInput;

    @FXML
    private TextField quantityInput;

    @FXML
    private TextField pricePerDayInput;

    @FXML
    private Label productNameInputError;

    @FXML
    private Label categoryInputError;

    @FXML
    private Label quantityInputError;

    @FXML
    private Label pricePerDayInputError;

    @FXML
    private Button backButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

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

    // Check if string contains number
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

    // Reset validation errors
    public void resetValidate() throws IOException, ClassNotFoundException {
        productNameInputError.setText("");
        categoryInputError.setText("");
        pricePerDayInputError.setText("");
        quantityInputError.setText("");
    }

    // Validate add stock function
    public Boolean validateAddStock() throws IOException, ClassNotFoundException {

        Boolean result = true;
        // Check product name
        if (productNameInput.getText().isEmpty()) {
            productNameInputError.setText("Please inform product name.");
            result = false;
        }
        // Check category
        if (categoryInput.getText().isEmpty()) {
            categoryInputError.setText("Please inform category.");
            result = false;
        }
        // Check quantity
        if (!isNumeric(quantityInput.getText())) {
            quantityInputError.setText("Quantity must be a number.");
            result = false;
        }
        if (quantityInput.getText().isEmpty()) {
            quantityInputError.setText("Please inform quantity.");
            result = false;
        }

        // Check price per day
        if (!isNumeric(pricePerDayInput.getText())) {
            pricePerDayInputError.setText("Price per day must be a number.");
            result = false;
        }
        if (pricePerDayInput.getText().isEmpty()) {
            pricePerDayInputError.setText("Please inform price per day.");
            result = false;
        }

        return result;
    }

    // Add stock function
    public void addStock() throws IOException, ClassNotFoundException {
        // Reset validation everytimes
        resetValidate();
        if (validateAddStock() == false) {
            return;
        }

        // Assign stock class data to store
        Stock stock = new Stock();
        stock.id = UUID.randomUUID().toString();
        stock.productName = productNameInput.getText();
        stock.category = categoryInput.getText();
        stock.quantity = Integer.parseInt(quantityInput.getText());
        stock.pricePerDay = Integer.parseInt(pricePerDayInput.getText());

        String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
        File f = new File(pathname);

        // Check if file exist
        if (f.exists()) {
            Stock[] stocks = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            stocks = (Stock[]) in.readObject();
            in.close();
            fileIn.close();

            // Check if same product name
            for (int i = 0; i < stocks.length; i++) {
                if (stocks[i].productName.equals(productNameInput.getText())) {
                    productNameInputError.setText("This product name has already used.");
                    return;
                }
            }

            // Store new product
            Stock[] newStocks = Arrays.copyOf(stocks, stocks.length + 1);
            newStocks[stocks.length] = stock;

            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newStocks);
            out.close();
            fileOut.close();
        } else {
            // If file not exist create new file and store a single data
            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new Stock[] { stock });
            out.close();
            fileOut.close();
        }

        // Click on back button to return to main scene
        backButton.fire();
    }
}