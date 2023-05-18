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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddRentingController {

    @FXML
    private Button customerLinkButton;

    @FXML
    private ScrollPane showArea;

    @FXML
    private ChoiceBox<String> customerInput;

    @FXML
    private ChoiceBox<String> productInput;

    @FXML
    private TextField qtyInput;

    @FXML
    private Label customerInputError;

    @FXML
    private Label productInputError;

    @FXML
    private Label qtyInputError;

    @FXML
    private Label cartInputError;

    @FXML
    private Button backButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Initial function
    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        // Reset cart function
        resetCart();
        // Get all customers
        getAllCustomers();
        // Get all stocks
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

    // Get all customers and set choicebox selects
    public void getAllCustomers() throws IOException, ClassNotFoundException, FileNotFoundException {
        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
        File f = new File(pathname);

        // Add choice box select
        customerInput.getItems().add("Please select customer");

        // If file exist
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

            // Add choice box selects
            for (int i = 0; i < customers.length; i++) {
                customerInput.getItems().add(customers[i].fullName);
                customerInput.setValue(customers[i].fullName);
            }
        }

        // Set default select
        customerInput.getSelectionModel().select(0);
    }

    // Get all stocks and set choicebox selects
    public void getAllStocks() throws IOException, ClassNotFoundException, FileNotFoundException {
        String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
        File f = new File(pathname);

        // Set choice box label
        productInput.getItems().add("Please select product");

        // If file exist
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

            // Add choice box selects
            for (int i = 0; i < stocks.length; i++) {
                productInput.getItems().add(stocks[i].productName);
            }
        }

        // Set default select
        productInput.getSelectionModel().select(0);
    }

    // Validate add to cart function
    public Boolean validateAddToCart() throws IOException, ClassNotFoundException {

        Boolean result = true;
        // Check product
        if (productInput.getValue() == "Please select product") {
            productInputError.setText("Please select product.");
            result = false;
        }
        // Check qty
        if (!isNumeric(qtyInput.getText())) {
            qtyInputError.setText("Please inform qty as number");
            result = false;
        }
        if (qtyInput.getText().isEmpty()) {
            qtyInputError.setText("Please inform qty.");
            result = false;
        }

        return result;
    }

    public void addToCart() throws IOException, ClassNotFoundException, FileNotFoundException {

        resetValidate();
        if (validateAddToCart() == false) {
            return;
        }

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

                    // Check avaiable stock
                    if (selectedItemQty > stocks[i].quantity) {
                        qtyInputError.setText("Not enough stock for this product.");
                        return;
                    }

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

            // Check if already add
            for (int j = 0; j < carts.length; j++) {
                if (carts[j].productName.equals(selectedItem)) {
                    productInputError.setText("You have already add this product.");
                    return;
                }
            }

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

        // Set up grid pane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setVgap(10);

        String pathname = "src/main/java/com/csym025_resit/Serialization/Cart.ser";
        File f = new File(pathname);

        // If file exist
        if (f.exists()) {

            // Read cart serialization data
            Cart[] cart = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cart = (Cart[]) in.readObject();
            in.close();
            fileIn.close();

            // Set product in cart
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

        // Display items in cart
        showArea.setContent(grid);
        showArea.setPannable(true);
    }

    // Check if String contain number
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
        customerInputError.setText("");
        productInputError.setText("");
        qtyInputError.setText("");
        cartInputError.setText("");
    }

    // Validate add invoice function
    public Boolean validateAddInvoice() throws IOException, ClassNotFoundException {

        Boolean result = true;
        // Check customer name
        if (customerInput.getValue() == "Please select customer") {
            customerInputError.setText("Please select customer.");
            result = false;
        }
        // Check product exist in cart
        String pathname = "src/main/java/com/csym025_resit/Serialization/Cart.ser";
        File f = new File(pathname);
        if (f.exists()) {
            Cart[] cart = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cart = (Cart[]) in.readObject();
            in.close();
            fileIn.close();
            if (cart.length == 0) {
                cartInputError.setText("Please select at least 1 product.");
                result = false;
            }
        } else {
            cartInputError.setText("Please select at least 1 product.");
            result = false;
        }

        return result;
    }

    // Add invoice function
    public void addInvoice() throws IOException, ClassNotFoundException, FileNotFoundException {

        // Reset validation error everytimes when click add invoice
        resetValidate();
        if (validateAddInvoice() == false) {
            return;
        }

        int totalPrice = 0;
        int lastPrice = 0;
        Cart[] cart = null;

        String pathname1 = "src/main/java/com/csym025_resit/Serialization/Cart.ser";
        File f1 = new File(pathname1);

        // Check if file exist
        if (f1.exists()) {
            FileInputStream fileIn1 = new FileInputStream(pathname1);
            ObjectInputStream in1 = new ObjectInputStream(fileIn1);
            cart = (Cart[]) in1.readObject();
            in1.close();
            fileIn1.close();

            // Find total price and deduct quantity
            for (int i = 0; i < cart.length; i++) {
                totalPrice += cart[i].pricePerDay * cart[i].quantity;

                // Deduct quantity
                String pathname2 = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
                File f2 = new File(pathname2);
                if (f2.exists()) {
                    Stock stock = null;
                    Stock[] stocks = null;
                    FileInputStream fileIn2 = new FileInputStream(pathname2);
                    ObjectInputStream in2 = new ObjectInputStream(fileIn2);
                    stocks = (Stock[]) in2.readObject();
                    in2.close();
                    fileIn2.close();

                    // Deduct quantity
                    for (int j = 0; j < stocks.length; j++) {
                        if ((stocks[j].productName).equals(cart[i].productName)) {
                            String pathname3 = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
                            stock = stocks[j];
                            stock.productName = stocks[j].productName;
                            stock.category = stocks[j].category;
                            stock.quantity = stocks[j].quantity - cart[i].quantity;
                            stock.pricePerDay = stocks[j].pricePerDay;

                            Stock[] newStocks = Arrays.copyOf(stocks, stocks.length);
                            newStocks[j] = stock;

                            FileOutputStream fileOut3 = new FileOutputStream(pathname3);
                            ObjectOutputStream out3 = new ObjectOutputStream(fileOut3);
                            out3.writeObject(newStocks);
                            out3.close();
                            fileOut3.close();
                            break;
                        }
                    }

                }
            }

            // Create invoice
            Invoice invoice = new Invoice();
            String pathname4 = "src/main/java/com/csym025_resit/Serialization/Invoice.ser";
            invoice.id = UUID.randomUUID().toString();
            invoice.customerName = customerInput.getValue();
            invoice.cart = cart;
            invoice.totalPrice = totalPrice;
            invoice.lastPrice = lastPrice;
            invoice.rentDate = LocalDateTime.now();
            invoice.returnDate = null;

            File f4 = new File(pathname4);
            if (f4.exists()) {
                Invoice[] invoices = null;
                FileInputStream fileIn4 = new FileInputStream(pathname4);
                ObjectInputStream in4 = new ObjectInputStream(fileIn4);
                invoices = (Invoice[]) in4.readObject();
                in4.close();
                fileIn4.close();

                Invoice[] newInvoices = Arrays.copyOf(invoices, invoices.length + 1);
                newInvoices[invoices.length] = invoice;

                FileOutputStream fileOut4 = new FileOutputStream(pathname4);
                ObjectOutputStream out4 = new ObjectOutputStream(fileOut4);
                out4.writeObject(newInvoices);
                out4.close();
                fileOut4.close();
            } else {
                FileOutputStream fileOut4 = new FileOutputStream(pathname4);
                ObjectOutputStream out4 = new ObjectOutputStream(fileOut4);
                out4.writeObject(new Invoice[] { invoice });
                out4.close();
                fileOut4.close();
            }

            // Click back button to return back to main scene
            backButton.fire();
        }

    }
}