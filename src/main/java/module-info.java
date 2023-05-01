module com.csym025_resit {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.csym025_resit to javafx.fxml;
    opens com.csym025_resit.Controller.Customer to javafx.fxml;
    opens com.csym025_resit.Controller.Rent to javafx.fxml;
    opens com.csym025_resit.Controller.Stock to javafx.fxml;
    
    exports com.csym025_resit;
    exports com.csym025_resit.Controller.Customer;
    exports com.csym025_resit.Controller.Rent;
    exports com.csym025_resit.Controller.Stock;
}
