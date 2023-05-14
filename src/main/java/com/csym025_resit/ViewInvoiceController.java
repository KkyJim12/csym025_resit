package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import com.csym025_resit.Holder.InvoiceHolder;
import com.csym025_resit.Holder.StockHolder;
import com.csym025_resit.Model.Cart;
import com.csym025_resit.Model.Invoice;
import com.csym025_resit.Model.Stock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewInvoiceController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ScrollPane showArea;

    @FXML
    private Button backButton;

    @FXML
    private Label titleLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label totalPricePerDayLabel;
    @FXML
    private Label discountLabel;
    @FXML
    private Label lastPriceLabel;
    @FXML
    private Label rentDateLabel;
    @FXML
    private Label returnDateLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label totalDayLabel;

    public void initialize() throws ClassNotFoundException {
        getInvoice();
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

    public void getInvoice() {
        try {
            String pathname = "src/main/java/com/csym025_resit/Serialization/Invoice.ser";
            File f = new File(pathname);

            InvoiceHolder holder = InvoiceHolder.getInstance();
            String invoiceId = holder.getInvoice();
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
                        break;
                    }
                }

                GridPane grid = new GridPane();
                grid.setPadding(new Insets(5));
                grid.setVgap(10);

                for (int j = 0; j < invoices[index].cart.length; j++) {
                    Label sort = new Label(j + 1 + "        ");
                    sort.setStyle("-fx-font-size:12; -fx-font-family: Segoe UI");
                    sort.setTextFill(Color.color(0, 0, 0));
                    Label productName = new Label("name: " + invoices[index].cart[j].productName + "        ");
                    productName.setStyle("-fx-font-size:12; -fx-font-family: Segoe UI");
                    productName.setTextFill(Color.color(0, 0, 0));
                    Label pricePerDay = new Label("price: " +
                            Integer.toString(invoices[index].cart[j].pricePerDay) + "        ");
                    pricePerDay.setStyle("-fx-font-size:12; -fx-font-family: Segoe UI");
                    pricePerDay.setTextFill(Color.color(0, 0, 0));
                    Label quantity = new Label("qty: " +
                            Integer.toString(invoices[index].cart[j].quantity) + "        ");
                    quantity.setStyle("-fx-font-size:12; -fx-font-family: Segoe UI");
                    quantity.setTextFill(Color.color(0, 0, 0));
                    Label totalPrice = new Label("total: " +
                            Integer.toString(invoices[index].cart[j].pricePerDay * invoices[index].cart[j].quantity)
                            + "        ");
                    quantity.setStyle("-fx-font-size:12; -fx-font-family: Segoe UI");
                    quantity.setTextFill(Color.color(0, 0, 0));

                    HBox cartCard = new HBox();
                    cartCard.getChildren().add(sort);
                    cartCard.getChildren().add(productName);
                    cartCard.getChildren().add(pricePerDay);
                    cartCard.getChildren().add(quantity);
                    cartCard.getChildren().add(totalPrice);

                    cartCard.setPrefSize(400, 30);
                    cartCard.setPadding(new Insets(10, 20, 10, 20));
                    cartCard.setStyle("-fx-background-color:white");

                    grid.add(cartCard, 1, j);
                }

                showArea.setContent(grid);
                showArea.setPannable(true);

                int days = Math.toIntExact(ChronoUnit.DAYS.between(invoice.rentDate, invoice.returnDate)) + 1;
                int totalPricePerDay = invoice.totalPrice * days;
                int lastPrice = totalPricePerDay - invoice.discount;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

                titleLabel.setText("Invoice-" + invoice.id);
                customerNameLabel.setText("Customer: " + invoice.customerName);
                totalPricePerDayLabel.setText("Total Price Per Day: " + invoice.totalPrice);
                totalDayLabel.setText("Total Day: " + days + " days");
                totalPriceLabel.setText("Total Price: " + totalPricePerDay);
                discountLabel.setText("Discount: " + invoice.discount);
                lastPriceLabel.setText("Last Price: " + lastPrice);
                rentDateLabel.setText("Rent Date: " + invoice.rentDate.format(formatter));
                returnDateLabel.setText("Return Date: " + invoice.returnDate.format(formatter));

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
