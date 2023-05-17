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

import com.csym025_resit.Model.Customer;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;

@TestMethodOrder(OrderAnnotation.class)
class CustomerTest {

    public Customer[] openCustomerSer() throws IOException, ClassNotFoundException {
        Customer[] customers = {};
        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";
        File f = new File(pathname);
        if (f.exists()) {
            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            customers = (Customer[]) in.readObject();
            in.close();
            fileIn.close();
            return customers;
        }
        return customers;
    }

    @BeforeEach
    void setup() throws Exception {
        ApplicationTest.launch(Main.class);
        FxRobot robot = new FxRobot();
        robot.clickOn("#customerLinkButton");
    }

    @Test
    @Order(1)
    public void resetCustomer() throws IOException, ClassNotFoundException {

        String pathname = "src/main/java/com/csym025_resit/Serialization/Customer.ser";

        File f = new File(pathname);
        f.delete();
    }

    @Test
    @Order(2)
    void TestAddCustomer() throws IOException, ClassNotFoundException {
        FxRobot robot = new FxRobot();
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

    // 2. Get all stocks
    @Test
    @Order(3)
    void TestGetCustomers() throws IOException, ClassNotFoundException {
        Customer[] customers = openCustomerSer();
        FxRobot robot = new FxRobot();
        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(customers.length,
                "#customerCard");
    }

    // 3. Update stock
    @Test
    @Order(4)
    void TestUpdateCustomer() throws IOException, ClassNotFoundException {

        Customer[] customers = openCustomerSer();
        String id = "#" + customers[0].id;
        String previousCustomerName = "Piyakarn Nimmakulvirut";
        String newCustomerName = "Sakolsak Nimmakulvirut";
        FxRobot robot = new FxRobot();
        robot.clickOn(id);
        robot.clickOn("#fullNameInput");
        robot.type(KeyCode.BACK_SPACE, previousCustomerName.length());
        robot.write(newCustomerName);
        robot.clickOn("#updateCustomerButton");

        customers = openCustomerSer();

        Assertions.assertThat(customers[0].fullName).isEqualTo(newCustomerName);
    }

    // 4. Search
    @Test
    @Order(5)
    void TestSearchCustomer() throws IOException, ClassNotFoundException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#searchInput");
        robot.write("abc");
        robot.clickOn("#searchButton");
        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(0, "#customerCard");

        robot.clickOn("#searchInput");
        robot.type(KeyCode.BACK_SPACE, 3);
        robot.write("Sakolsak");
        robot.clickOn("#searchButton");
        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(1, "#customerCard");
    }

    // 5. Test delete stock
    @Test
    @Order(6)
    void TestDeleteCustomer() throws IOException, ClassNotFoundException {

        FxRobot robot = new FxRobot();
        robot.clickOn(".deleteButton");

        Assertions.assertThat(robot.lookup("#showArea").queryAs(ScrollPane.class)).hasExactlyChildren(0, "#stockCard");
    }

}