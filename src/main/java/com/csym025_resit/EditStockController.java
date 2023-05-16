package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import com.csym025_resit.Holder.StockHolder;
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

public class EditStockController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button updateButton;

    @FXML
    private TextField productNameInput;
    @FXML
    private TextField categoryInput;
    @FXML
    private TextField quantityInput;
    @FXML
    private TextField pricePerDayInput;
    @FXML
    private Button backButton;

    @FXML
    private Label productNameInputError;
    @FXML
    private Label categoryInputError;
    @FXML
    private Label quantityInputError;
    @FXML
    private Label pricePerDayInputError;

    public void initialize() throws ClassNotFoundException {
        getStock();
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

    public Boolean validateUpdateStock() throws IOException, ClassNotFoundException {

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

    public void resetValidate() throws IOException, ClassNotFoundException {
        productNameInputError.setText("");
        categoryInputError.setText("");
        pricePerDayInputError.setText("");
        quantityInputError.setText("");
    }

    public void updateStock() throws IOException, ClassNotFoundException {

        resetValidate();
        if (validateUpdateStock() == false) {
            return;
        }

        String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
        StockHolder holder = StockHolder.getInstance();
        String stockId = holder.getStock();

        File f = new File(pathname);
        if (f.exists()) {
            Stock stock = null;
            Stock[] stocks = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            stocks = (Stock[]) in.readObject();
            in.close();
            fileIn.close();

            int index = 0;

            for (int i = 0; i < stocks.length; i++) {
                if ((stocks[i].id).equals(stockId)) {
                    stock = stocks[i];
                    index = i;
                    break;
                }
            }

            // Check if same product name
            for (int j = 0; j < stocks.length; j++) {
                if (stocks[j].productName.equals(productNameInput.getText()) && j != index) {
                    productNameInputError.setText("This product name has already used.");
                    return;
                }
            }

            stock.productName = productNameInput.getText();
            stock.category = categoryInput.getText();
            stock.quantity = Integer.parseInt(quantityInput.getText());
            stock.pricePerDay = Integer.parseInt(pricePerDayInput.getText());

            Stock[] newStocks = Arrays.copyOf(stocks, stocks.length);
            newStocks[index] = stock;

            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newStocks);
            out.close();
            fileOut.close();
        }

        backButton.fire();
    }

    public void getStock() {
        try {
            String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
            File f = new File(pathname);

            StockHolder holder = StockHolder.getInstance();
            String stockId = holder.getStock();
            if (f.exists()) {
                Stock stock = null;
                Stock[] stocks = null;
                FileInputStream fileIn = new FileInputStream(pathname);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                stocks = (Stock[]) in.readObject();
                in.close();
                fileIn.close();

                for (int i = 0; i < stocks.length; i++) {
                    if ((stocks[i].id).equals(stockId)) {
                        stock = stocks[i];
                        break;
                    }
                }

                productNameInput.setText(stock.productName);
                categoryInput.setText(stock.category);
                quantityInput.setText(Integer.toString(stock.quantity));
                pricePerDayInput.setText(Integer.toString(stock.pricePerDay));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
