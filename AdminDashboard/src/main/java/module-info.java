module com.example.userstory4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.userstory4 to javafx.fxml;
    exports com.example.userstory4;
}