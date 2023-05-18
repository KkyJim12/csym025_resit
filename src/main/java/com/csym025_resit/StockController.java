package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.csym025_resit.Holder.StockHolder;
import com.csym025_resit.Model.Stock;

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

public class StockController implements Serializable {

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
        getAllStocks();
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

    // Switch to add stock scene
    public void switchToAddStockScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AddStock.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Switch to edit stock scene
    private class switchToEditStockScene implements EventHandler<Event> {
        @Override
        public void handle(Event evt) {
            try {
                // Get id from button
                String elementId = ((Button) evt.getSource()).getId();

                // Store id in holder class
                StockHolder holder = StockHolder.getInstance();
                holder.setStock(elementId);

                // Switch scene
                root = FXMLLoader.load(getClass().getResource("EditStock.fxml"));
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

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(5));
            grid.setVgap(10);

            // Find stock from search input value
            for (int i = 0; i < stocks.length; i++) {
                if (stocks[i].productName.contains(searchInput.getText())) {
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
                    editButton.setId(stocks[i].id);
                    editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new switchToEditStockScene());
                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#f97316");
                    deleteButton.setTextFill(Color.color(1, 1, 1));
                    deleteButton.setId(stocks[i].id);
                    deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new deleteStock());
                    HBox manageSection = new HBox(editButton, deleteButton);
                    manageSection.setSpacing(10);

                    VBox stockCard = new VBox();
                    stockCard.getChildren().add(productName);
                    stockCard.getChildren().add(category);
                    stockCard.getChildren().add(pricePerDay);
                    stockCard.getChildren().add(quantity);
                    stockCard.getChildren().add(manageSection);

                    stockCard.setPrefSize(425, 150);
                    stockCard.setPadding(new Insets(10, 20, 10, 20));
                    stockCard.setStyle("-fx-background-color:#ef4444");
                    stockCard.setId("stockCard");

                    grid.add(stockCard, 1, i);
                }
            }

            showArea.setContent(grid);
            showArea.setPannable(true);

        }
    }

    // Get all stocks function
    public void getAllStocks() throws IOException, ClassNotFoundException, FileNotFoundException {

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

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(5));
            grid.setVgap(10);

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
                editButton.setId(stocks[i].id);
                editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new switchToEditStockScene());
                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#f97316");
                deleteButton.setTextFill(Color.color(1, 1, 1));
                deleteButton.setId(stocks[i].id);
                deleteButton.getStyleClass().add("deleteButton");
                deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new deleteStock());
                HBox manageSection = new HBox(editButton, deleteButton);
                manageSection.setSpacing(10);

                VBox stockCard = new VBox();
                stockCard.getChildren().add(productName);
                stockCard.getChildren().add(category);
                stockCard.getChildren().add(pricePerDay);
                stockCard.getChildren().add(quantity);
                stockCard.getChildren().add(manageSection);

                stockCard.setPrefSize(425, 150);
                stockCard.setPadding(new Insets(10, 20, 10, 20));
                stockCard.setStyle("-fx-background-color:#ef4444");
                stockCard.setId("stockCard");

                grid.add(stockCard, 1, i);
            }

            showArea.setContent(grid);
            showArea.setPannable(true);

        }
    }

    // Delete stock by id
    private class deleteStock implements EventHandler<Event> {
        @Override
        public void handle(Event evt) {
            try {

                // Get id from button
                String elementId = ((Button) evt.getSource()).getId();

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

                    Stock[] newStocks = new Stock[stocks.length - 1];

                    // Find matching id
                    for (int i = 0, k = 0; i < stocks.length; i++) {
                        if (!(stocks[i].id).equals(elementId)) {
                            newStocks[k] = stocks[i];
                            k++;
                        }
                    }

                    FileOutputStream fileOut = new FileOutputStream(pathname);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(newStocks);
                    out.close();
                    fileOut.close();

                    getAllStocks();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}