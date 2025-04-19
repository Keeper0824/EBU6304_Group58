module com.example.transaction {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.transaction to javafx.fxml;
    exports com.example.transaction;
}