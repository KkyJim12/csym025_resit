package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.csym025_resit.Holder.StockHolder;
import com.csym025_resit.Model.Stock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
