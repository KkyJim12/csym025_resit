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

import com.csym025_resit.Model.Stock;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;

@TestMethodOrder(OrderAnnotation.class)
class StockTest {

    public Stock[] openStockSer() throws IOException, ClassNotFoundException {
        Stock[] stocks = {};
        String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";
        File f = new File(pathname);
        if (f.exists()) {
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            stocks = (Stock[]) in.readObject();
            in.close();
            fileIn.close();
            return stocks;
        }
        return stocks;
    }

    @BeforeEach
    void setup() throws Exception {
        ApplicationTest.launch(Main.class);
    }

    @Test
    @Order(1)
    public void resetStock() throws IOException, ClassNotFoundException {

        String pathname = "src/main/java/com/csym025_resit/Serialization/Stock.ser";

        File f = new File(pathname);
        f.delete();
    }

    @Test
    @Order(2)
    void TestAddStock() throws IOException, ClassNotFoundException {
        FxRobot robot = new FxRobot();
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
    }

    // 2. Get all stocks
    @Test
    @Order(3)
    void TestGetStocks() throws IOException, ClassNotFoundException {
        Stock[] stocks = openStockSer();
        FxRobot robot = new FxRobot();
        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(stocks.length,
                "#stockCard");
    }

    // 3. Update stock
    @Test
    @Order(4)
    void TestUpdateStock() throws IOException, ClassNotFoundException {

        Stock[] stocks = openStockSer();
        String id = "#" + stocks[0].id;
        String previousProductName = "Iphone 14 pro max";
        String newProductName = "Samsung Galaxy";
        FxRobot robot = new FxRobot();
        robot.clickOn(id);
        robot.clickOn("#productNameInput");
        robot.type(KeyCode.BACK_SPACE, previousProductName.length());
        robot.write(newProductName);
        robot.clickOn("#updateStockButton");

        stocks = openStockSer();

        Assertions.assertThat(stocks[0].productName).isEqualTo(newProductName);
    }

    // 4. Search
    @Test
    @Order(5)
    void TestSearchStock() throws IOException, ClassNotFoundException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#searchInput");
        robot.write("abc");
        robot.clickOn("#searchButton");
        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(0, "#stockCard");

        robot.clickOn("#searchInput");
        robot.type(KeyCode.BACK_SPACE, 3);
        robot.write("Samsung");
        robot.clickOn("#searchButton");
        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(1, "#stockCard");
    }

    // 5. Test delete stock
    @Test
    @Order(6)
    void TestDeleteStock() throws IOException, ClassNotFoundException {

        FxRobot robot = new FxRobot();
        robot.clickOn(".deleteButton");

        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(0, "#stockCard");
    }

}