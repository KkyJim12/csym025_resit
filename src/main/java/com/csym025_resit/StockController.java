package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.csym025_resit.Model.Stock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StockController implements Serializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ScrollPane showArea;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        getAllStocks();
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

    public void switchToAddStockScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AddStock.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void getAllStocks() throws IOException, ClassNotFoundException, FileNotFoundException {

        String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
        File f = new File(pathname);
        if (f.exists()) {
            Stock[] stocks = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            stocks = (Stock[]) in.readObject();
            in.close();
            fileIn.close();

            for (int i = 0; i < stocks.length; i++) {
                Label productName = new Label(stocks[i].productName);
                productName.setStyle("-fx-font-size:32; -fx-font-weight:bold; -fx-font-family: Segoe UI");
                productName.setTextFill(Color.color(1, 1, 1));
                Label category = new Label("Category: " + stocks[i].category);
                category.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                category.setTextFill(Color.color(1, 1, 1));
                Label pricePerDay = new Label("Price Per Day: " + Integer.toString(stocks[i].pricePerDay));
                pricePerDay.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                pricePerDay.setTextFill(Color.color(1, 1, 1));
                Label quantity = new Label("Quantity: " + Integer.toString(stocks[i].quantity));
                quantity.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                quantity.setTextFill(Color.color(1, 1, 1));
                Button editButton = new Button("Edit");
                editButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#eab308");
                editButton.setTextFill(Color.color(1, 1, 1));
                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#f97316");
                deleteButton.setTextFill(Color.color(1, 1, 1));
                HBox manageSection = new HBox(editButton, deleteButton);
                manageSection.setSpacing(10);

                VBox stockCard = new VBox();
                stockCard.getChildren().add(productName);
                stockCard.getChildren().add(category);
                stockCard.getChildren().add(pricePerDay);
                stockCard.getChildren().add(quantity);
                stockCard.getChildren().add(manageSection);

                stockCard.setPrefSize(450, 180);
                stockCard.setPadding(new Insets(10, 20, 10, 20));
                stockCard.setStyle("-fx-background-color:#ef4444");

                showArea.setContent(stockCard);
            }

        }
    }
}