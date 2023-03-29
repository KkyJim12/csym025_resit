package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import com.csym025_resit.Model.Stock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private Stage stage;
    private Scene scene;
    private Parent root;

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

    public void addStock() throws IOException, ClassNotFoundException {
        Stock stock = new Stock();
        String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
        stock.productName = productNameInput.getText();
        stock.category = categoryInput.getText();
        stock.quantity = Integer.parseInt(quantityInput.getText());
        stock.pricePerDay = Integer.parseInt(pricePerDayInput.getText());

        File f = new File(pathname);
        if (f.exists()) {
            Stock[] stocks = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            stocks = (Stock[]) in.readObject();
            in.close();
            fileIn.close();

            Stock[] newStocks = Arrays.copyOf(stocks, stocks.length + 1);
            newStocks[stocks.length] = stock;

            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newStocks);
            out.close();
            fileOut.close();
        } else {
            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new Stock[] { stock });
            out.close();
            fileOut.close();
        }

    }
}