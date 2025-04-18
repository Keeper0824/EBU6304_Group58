module com.example.consume {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.consume to javafx.fxml;
    exports com.example.consume;
}