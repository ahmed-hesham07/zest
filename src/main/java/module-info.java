module com.example.zest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.zest to javafx.fxml;
    exports com.zest;
}