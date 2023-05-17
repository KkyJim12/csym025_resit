package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import com.csym025_resit.Holder.InvoiceHolder;
import com.csym025_resit.Model.Invoice;
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

public class RentController {

    @FXML
    private Button customerLinkButton;

    @FXML
    private ScrollPane showArea;

    @FXML
    private TextField searchInput;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        getAllInvoices();
    }

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

    public void switchToAddRentingScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AddRenting.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void search() throws IOException, ClassNotFoundException, FileNotFoundException {

        String pathname = "src/main/java/com/csym025_resit/Serialization/Invoice.ser";
        File f = new File(pathname);
        if (f.exists()) {
            Invoice[] invoices = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            invoices = (Invoice[]) in.readObject();
            in.close();
            fileIn.close();

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(5));
            grid.setVgap(10);

            for (int i = 0; i < invoices.length; i++) {
                if (invoices[i].customerName.contains(searchInput.getText())) {
                    Label invoiceName = new Label("Invoice-" + invoices[i].id);
                    invoiceName.setStyle("-fx-font-size:32; -fx-font-weight:bold; -fx-font-family: Segoe UI");
                    invoiceName.setTextFill(Color.color(1, 1, 1));
                    Label customerName = new Label("Customer: " + invoices[i].customerName);
                    customerName.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    customerName.setTextFill(Color.color(1, 1, 1));
                    Label totalPrice = new Label("TotalPrice Per Day: " + Integer.toString(invoices[i].totalPrice));
                    totalPrice.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    totalPrice.setTextFill(Color.color(1, 1, 1));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
                    Label rentDate = new Label("Rent Date: " + invoices[i].rentDate.format(formatter));
                    rentDate.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    rentDate.setTextFill(Color.color(1, 1, 1));

                    Button viewInvoiceButton = new Button("View Invoice");
                    viewInvoiceButton
                            .setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#f97316");
                    viewInvoiceButton.setTextFill(Color.color(1, 1, 1));
                    viewInvoiceButton.setId(invoices[i].id);
                    viewInvoiceButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new viewInvoice());

                    Button acceptReturnButton = new Button("Accept Return");
                    acceptReturnButton
                            .setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#10b981");
                    acceptReturnButton.setTextFill(Color.color(1, 1, 1));
                    acceptReturnButton.setId(invoices[i].id);
                    acceptReturnButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new acceptReturn());
                    HBox manageSection = new HBox(viewInvoiceButton, acceptReturnButton);
                    manageSection.setSpacing(10);

                    VBox invoiceCard = new VBox();
                    invoiceCard.getChildren().add(invoiceName);
                    invoiceCard.getChildren().add(customerName);
                    invoiceCard.getChildren().add(totalPrice);
                    invoiceCard.getChildren().add(rentDate);

                    if (invoices[i].returnDate != null) {
                        int days = Math
                                .toIntExact(ChronoUnit.DAYS.between(invoices[i].rentDate, invoices[i].returnDate))
                                + 1;
                        Label lastPrice = new Label("Last Price: " + invoices[i].totalPrice * days);
                        lastPrice.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                        lastPrice.setTextFill(Color.color(1, 1, 1));
                        invoiceCard.getChildren().add(lastPrice);

                        Label returnDate = new Label("Return Date: " + invoices[i].returnDate.format(formatter));
                        returnDate.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                        returnDate.setTextFill(Color.color(1, 1, 1));
                        invoiceCard.getChildren().add(returnDate);

                        manageSection = new HBox(viewInvoiceButton);
                    }

                    invoiceCard.getChildren().add(manageSection);

                    invoiceCard.setPrefSize(425, 150);
                    invoiceCard.setPadding(new Insets(10, 20, 10, 20));
                    invoiceCard.setId("invoiceCard");

                    if (invoices[i].returnDate != null) {
                        invoiceCard.setStyle("-fx-background-color:#22c55e");
                    } else {
                        invoiceCard.setStyle("-fx-background-color:#ef4444");
                    }
                    grid.add(invoiceCard, 1, i);
                }
            }

            showArea.setContent(grid);
            showArea.setPannable(true);

        }
    }

    public void getAllInvoices() throws IOException, ClassNotFoundException, FileNotFoundException {

        String pathname = "src/main/java/com/csym025_resit/Serialization/Invoice.ser";
        File f = new File(pathname);
        if (f.exists()) {
            Invoice[] invoices = null;
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            invoices = (Invoice[]) in.readObject();
            in.close();
            fileIn.close();

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(5));
            grid.setVgap(10);

            for (int i = 0; i < invoices.length; i++) {
                Label invoiceName = new Label("Invoice-" + invoices[i].id);
                invoiceName.setStyle("-fx-font-size:32; -fx-font-weight:bold; -fx-font-family: Segoe UI");
                invoiceName.setTextFill(Color.color(1, 1, 1));
                Label customerName = new Label("Customer: " + invoices[i].customerName);
                customerName.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                customerName.setTextFill(Color.color(1, 1, 1));
                Label totalPrice = new Label("TotalPrice Per Day: " + Integer.toString(invoices[i].totalPrice));
                totalPrice.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                totalPrice.setTextFill(Color.color(1, 1, 1));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
                Label rentDate = new Label("Rent Date: " + invoices[i].rentDate.format(formatter));
                rentDate.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                rentDate.setTextFill(Color.color(1, 1, 1));

                Button viewInvoiceButton = new Button("View Invoice");
                viewInvoiceButton.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#f97316");
                viewInvoiceButton.setTextFill(Color.color(1, 1, 1));
                viewInvoiceButton.setId(invoices[i].id);
                viewInvoiceButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new viewInvoice());

                Button acceptReturnButton = new Button("Accept Return");
                acceptReturnButton
                        .setStyle("-fx-font-size:15; -fx-font-family: Segoe UI; -fx-background-color:#10b981");
                acceptReturnButton.setTextFill(Color.color(1, 1, 1));
                acceptReturnButton.setId(invoices[i].id);
                acceptReturnButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new acceptReturn());
                HBox manageSection = new HBox(viewInvoiceButton, acceptReturnButton);
                manageSection.setSpacing(10);

                VBox invoiceCard = new VBox();
                invoiceCard.getChildren().add(invoiceName);
                invoiceCard.getChildren().add(customerName);
                invoiceCard.getChildren().add(totalPrice);
                invoiceCard.getChildren().add(rentDate);

                if (invoices[i].returnDate != null) {
                    int days = Math.toIntExact(ChronoUnit.DAYS.between(invoices[i].rentDate, invoices[i].returnDate))
                            + 1;
                    Label lastPrice = new Label("Last Price: " + invoices[i].totalPrice * days);
                    lastPrice.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    lastPrice.setTextFill(Color.color(1, 1, 1));
                    invoiceCard.getChildren().add(lastPrice);

                    Label returnDate = new Label("Return Date: " + invoices[i].returnDate.format(formatter));
                    returnDate.setStyle("-fx-font-size:15; -fx-font-family: Segoe UI");
                    returnDate.setTextFill(Color.color(1, 1, 1));
                    invoiceCard.getChildren().add(returnDate);

                    manageSection = new HBox(viewInvoiceButton);
                }

                invoiceCard.getChildren().add(manageSection);

                invoiceCard.setPrefSize(425, 150);
                invoiceCard.setPadding(new Insets(10, 20, 10, 20));
                invoiceCard.setId("invoiceCard");

                if (invoices[i].returnDate != null) {
                    invoiceCard.setStyle("-fx-background-color:#22c55e");
                } else {
                    invoiceCard.setStyle("-fx-background-color:#ef4444");
                }
                grid.add(invoiceCard, 1, i);
            }

            showArea.setContent(grid);
            showArea.setPannable(true);

        }
    }

    private class acceptReturn implements EventHandler<Event> {
        @Override
        public void handle(Event evt) {
            try {

                String elementId = ((Button) evt.getSource()).getId();

                InvoiceHolder holder = InvoiceHolder.getInstance();
                holder.setInvoice(elementId);

                String pathname = "src/main/java/com/csym025_resit/Serialization/Invoice.ser";
                InvoiceHolder holder2 = InvoiceHolder.getInstance();
                String invoiceId = holder2.getInvoice();

                File f = new File(pathname);
                if (f.exists()) {
                    Invoice invoice = null;
                    Invoice[] invoices = null;
                    FileInputStream fileIn = new FileInputStream(pathname);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    invoices = (Invoice[]) in.readObject();
                    in.close();
                    fileIn.close();

                    int index = 0;

                    for (int i = 0; i < invoices.length; i++) {
                        if ((invoices[i].id).equals(invoiceId)) {
                            invoice = invoices[i];
                            index = i;

                            // Return stock
                            for (int k = 0; k < invoices[i].cart.length; k++) {
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

                                    int index2 = 0;

                                    for (int j = 0; j < stocks.length; j++) {
                                        if ((stocks[j].productName).equals(invoices[i].cart[k].productName)) {
                                            stock = stocks[j];
                                            index2 = j;
                                            break;
                                        }
                                    }

                                    stock.productName = stocks[index2].productName;
                                    stock.category = stocks[index2].category;
                                    stock.quantity = stocks[index2].quantity + invoices[i].cart[k].quantity;
                                    stock.pricePerDay = stocks[index2].pricePerDay;

                                    Stock[] newStocks = Arrays.copyOf(stocks, stocks.length);
                                    newStocks[index2] = stock;

                                    FileOutputStream fileOut2 = new FileOutputStream(pathname2);
                                    ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);
                                    out2.writeObject(newStocks);
                                    out2.close();
                                    fileOut2.close();
                                }

                            }
                        }
                    }

                    invoice.id = invoices[index].id;
                    invoice.customerName = invoices[index].customerName;
                    invoice.cart = invoices[index].cart;
                    invoice.totalPrice = invoices[index].totalPrice;
                    invoice.lastPrice = invoices[index].lastPrice;
                    invoice.rentDate = invoices[index].rentDate;
                    invoice.returnDate = LocalDateTime.now();

                    Invoice[] newInvoices = Arrays.copyOf(invoices, invoices.length);
                    newInvoices[index] = invoice;

                    FileOutputStream fileOut = new FileOutputStream(pathname);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(newInvoices);
                    out.close();
                    fileOut.close();
                }

                getAllInvoices();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private class viewInvoice implements EventHandler<Event> {
        @Override
        public void handle(Event evt) {
            try {
                String elementId = ((Button) evt.getSource()).getId();

                InvoiceHolder holder = InvoiceHolder.getInstance();
                holder.setInvoice(elementId);

                root = FXMLLoader.load(getClass().getResource("ViewInvoice.fxml"));
                stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}