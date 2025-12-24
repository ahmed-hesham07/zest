module com.example.zest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.zest to javafx.fxml;
    exports com.example.zest;
}