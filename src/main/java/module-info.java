module com.csym025_resit {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.csym025_resit to javafx.fxml;

    exports com.csym025_resit;
}
