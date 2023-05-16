package com.csym025_resit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

class StockTest {

    @FXML
    private Button confirmButton;

    @BeforeEach
    void setup() throws Exception {
        ApplicationTest.launch(Main.class);
    }

    @Test
    void TestAddStock() {
        FxRobot robot = new FxRobot();
        Assertions.assertThat(
                robot.lookup("#addStockButton").queryAs(Button.class)).hasText("Add Stock");
    }
    // 2. Get all stocks

    // 3. Update stock

    // 4. Delete stock

    // 5. Search

}