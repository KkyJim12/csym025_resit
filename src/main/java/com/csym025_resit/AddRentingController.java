package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import com.csym025_resit.Model.Cart;
import com.csym025_resit.Model.Customer;
import com.csym025_resit.Model.Invoice;
import com.csym025_resit.Model.Stock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddRentingController {

    @FXML
    private Button customerLinkButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ScrollPane showArea;

    @FXML
    private ChoiceBox<String> customerInput;
    @FXML
    private ChoiceBox<String> productInput;
    @FXML
    private TextField qtyInput;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        resetCart();
        getAllCustomers();
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

    public void getAllCustomers() throws IOException, ClassNotFoundException, FileNotFoundException {

        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
        File f = new File(pathname);
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

            customerInput.getItems().add("Please select cuystomer");
            customerInput.setValue(null);

            for (int i = 0; i < customers.length; i++) {
                customerInput.getItems().add(customers[i].fullName);
                customerInput.setValue(customers[i].fullName);
            }

            customerInput.getSelectionModel().select(0);
        }
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

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(5));
            grid.setVgap(10);

            productInput.getItems().add("Please select product");
            productInput.setValue(null);

            for (int i = 0; i < stocks.length; i++) {
                productInput.getItems().add(stocks[i].productName);
            }
            productInput.getSelectionModel().select(0);
        }
    }

    public void addToCart() throws IOException, ClassNotFoundException, FileNotFoundException {
        String selectedItem = productInput.getValue();
        int selectedItemQty = Integer.parseInt(qtyInput.getText());

        Cart cart = new Cart();
        String pathname1 = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
        File f1 = new File(pathname1);
        if (f1.exists()) {
            Stock[] stocks = null;
            FileInputStream fileIn = new FileInputStream(pathname1);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            stocks = (Stock[]) in.readObject();
            in.close();
            fileIn.close();

            for (int i = 0; i < stocks.length; i++) {
                if (selectedItem.equals(stocks[i].productName)) {
                    cart.productName = stocks[i].productName;
                    cart.category = stocks[i].category;
                    cart.quantity = selectedItemQty;
                    cart.pricePerDay = stocks[i].pricePerDay;
                    break;
                }
            }
        }

        String pathname = "src/main/java/com/csym025_resit/Serialization/Cart.ser";
        File f = new File(pathname);
        if (f.exists()) {
            Cart[] carts = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            carts = (Cart[]) in.readObject();
            in.close();
            fileIn.close();

            Cart[] newCart = Arrays.copyOf(carts, carts.length + 1);
            newCart[carts.length] = cart;

            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newCart);
            out.close();
            fileOut.close();
        } else {
            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new Cart[] { cart });
            out.close();
            fileOut.close();
        }

        getCart();

    }

    public void resetCart() throws IOException, ClassNotFoundException, FileNotFoundException {
        String pathname = "src/main/java/com/csym025_resit/Serialization/Cart.ser";
        File f = new File(pathname);
        if (f.exists()) {
            Cart[] newCart = {};

            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newCart);
            out.close();
            fileOut.close();
        }

        getCart();
    }

    public void getCart() throws IOException, ClassNotFoundException, FileNotFoundException {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setVgap(10);

        String pathname = "src/main/java/com/csym025_resit/Serialization/Cart.ser";
        File f = new File(pathname);
        if (f.exists()) {
            Cart[] cart = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cart = (Cart[]) in.readObject();
            in.close();
            fileIn.close();

            for (int i = 0; i < cart.length; i++) {
                Label productName = new Label(cart[i].productName);
                productName.setStyle("-fx-font-size:32; -fx-font-weight:bold; -fx-font-family: Segoe UI");
                productName.setTextFill(Color.color(1, 1, 1));
                Label category = new Label("Category: " + cart[i].category);
                category.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                category.setTextFill(Color.color(1, 1, 1));
                Label quantity = new Label("Quantity: " + Integer.toString(cart[i].quantity));
                quantity.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                quantity.setTextFill(Color.color(1, 1, 1));
                Label totalPrice = new Label(
                        "Total Price: " + Integer.toString(cart[i].pricePerDay * cart[i].quantity));
                totalPrice.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                totalPrice.setTextFill(Color.color(1, 1, 1));
                // deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new
                // removeProductFromCart());

                VBox productCard = new VBox();
                productCard.getChildren().add(productName);
                productCard.getChildren().add(category);
                productCard.getChildren().add(quantity);
                productCard.getChildren().add(totalPrice);

                productCard.setPrefSize(425, 150);
                productCard.setPadding(new Insets(10, 20, 10, 20));
                productCard.setStyle("-fx-background-color:#ef4444");

                grid.add(productCard, 1, i);
            }
        }

        showArea.setContent(grid);
        showArea.setPannable(true);
    }

    public void addInvoice() throws IOException, ClassNotFoundException, FileNotFoundException {
        int totalPrice = 0;
        int discount = 0;
        int lastPrice = 0;
        Cart[] cart = null;

        String pathname = "src/main/java/com/csym025_resit/Serialization/Cart.ser";
        File f = new File(pathname);
        if (f.exists()) {
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cart = (Cart[]) in.readObject();
            in.close();
            fileIn.close();

            for (int i = 0; i < cart.length; i++) {
                totalPrice += cart[i].pricePerDay * cart[i].quantity;
            }

            System.out.println(totalPrice);
        }

        Invoice invoice = new Invoice();
        String pathname1 = "src/main/java/com/csym025_resit/Serialization/Invoice.ser";
        invoice.id = UUID.randomUUID().toString();
        invoice.customerName = customerInput.getValue();
        invoice.cart = cart;
        invoice.totalPrice = totalPrice;
        invoice.discount = discount;
        invoice.lastPrice = lastPrice;
        invoice.rentDate = LocalDateTime.now();
        invoice.returnDate = null;

        File f1 = new File(pathname1);
        if (f1.exists()) {
            Invoice[] invoices = null;
            FileInputStream fileIn = new FileInputStream(pathname1);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            invoices = (Invoice[]) in.readObject();
            in.close();
            fileIn.close();

            Invoice[] newInvoices = Arrays.copyOf(invoices, invoices.length + 1);
            newInvoices[invoices.length] = invoice;

            FileOutputStream fileOut = new FileOutputStream(pathname1);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(newInvoices);
            out.close();
            fileOut.close();
        } else {
            FileOutputStream fileOut = new FileOutputStream(pathname1);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new Invoice[] { invoice });
            out.close();
            fileOut.close();
        }

        backButton.fire();
    }

}