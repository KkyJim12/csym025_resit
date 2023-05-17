package com.csym025_resit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.robot.Motion;

import com.csym025_resit.Model.Invoice;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

@TestMethodOrder(OrderAnnotation.class)
class RentTest {

    public Invoice[] openInvoiceSer() throws IOException, ClassNotFoundException {
        Invoice[] invoices = {};
        String pathname = "src/main/java/com/csym025_resit/Serialization/Invoice.ser";
        File f = new File(pathname);
        if (f.exists()) {
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            invoices = (Invoice[]) in.readObject();
            in.close();
            fileIn.close();
            return invoices;
        }
        return invoices;
    }

    @BeforeEach
    protected void setUp() throws Exception {
        ApplicationTest.launch(Main.class);
        FxRobot robot = new FxRobot();
        robot.clickOn("#rentLinkButton");
    }

    @Test
    @Order(1)
    public void resetAllSer() throws IOException, ClassNotFoundException {
        String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
        File f = new File(pathname);
        f.delete();

        String pathname1 = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
        File f1 = new File(pathname1);
        f1.delete();

        String pathname2 = "src/main/java/com/csym025_resit/Serialization/Invoice.ser";
        File f2 = new File(pathname2);
        f2.delete();
    }

    @Test
    @Order(2)
    void TestAddCustomerAndStock() throws IOException, ClassNotFoundException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#stockLinkButton");
        robot.clickOn("#addStockButton");
        robot.clickOn("#productNameInput");
        robot.write("Iphone 14 pro max");
        robot.clickOn("#categoryInput");
        robot.write("Smart Phone");
        robot.clickOn("#pricePerDayInput");
        robot.write("200");
        robot.clickOn("#quantityInput");
        robot.write("12");
        robot.clickOn("#confirmButton");

        robot.clickOn("#customerLinkButton");
        robot.clickOn("#addCustomerButton");
        robot.clickOn("#fullNameInput");
        robot.write("Piyakarn Nimmakulvirut");
        robot.clickOn("#emailInput");
        robot.write("piyakarn.nmk@gmail.com");
        robot.clickOn("#phoneInput");
        robot.write("0658528414");
        robot.clickOn("#addressInput");
        robot.write("Northampton NN1 2BH");
        robot.clickOn("#confirmButton");
    }

    @Test
    @Order(3)
    void TestAddRenting() throws IOException, ClassNotFoundException, InterruptedException {

        FxRobot robot = new FxRobot();
        robot.clickOn("#addRentingButton");
        robot.clickOn("#customerInput");
        robot.press(KeyCode.DOWN);
        robot.press(KeyCode.ENTER);
        robot.clickOn("#productInput");
        robot.moveBy(0, 30, Motion.DEFAULT);
        robot.clickOn(MouseButton.PRIMARY);
        robot.clickOn("#qtyInput");
        robot.write("3");
        robot.clickOn("#addToCartButton");
        robot.clickOn("#confirmButton");

        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(1,
                "#invoiceCard");
    }

    @Test
    @Order(4)
    void TestAcceptReturn() throws IOException, ClassNotFoundException, InterruptedException {

        Invoice[] invoices = openInvoiceSer();

        FxRobot robot = new FxRobot();
        robot.moveTo("#" + invoices[0].id);
        robot.moveBy(125, 0, Motion.DEFAULT);
        robot.clickOn(MouseButton.PRIMARY);

        invoices = openInvoiceSer();
        Assertions.assertThat(invoices[0].returnDate).isNotNull();
    }

    @Test
    @Order(5)
    void TestSearchInvoice() throws IOException, ClassNotFoundException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#searchInput");
        robot.write("abc");
        robot.clickOn("#searchButton");
        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(0,
                "#invoiceCard");

        robot.clickOn("#searchInput");
        robot.type(KeyCode.BACK_SPACE, 3);
        robot.write("Piyakarn");
        robot.clickOn("#searchButton");
        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(1,
                "#invoiceCard");
    }

}