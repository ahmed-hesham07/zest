module zest { 
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;
 // Essential for DBConnection and DataService
 // SQLite JDBC is an automatic module, no explicit requires needed

    // Allow JavaFX to "see" your controllers and FXML files
    opens com.zest.controller to javafx.fxml; 
    opens com.zest to javafx.fxml, javafx.graphics;
    opens com.zest.model to javafx.base; // Required for populating UI tables/lists

    // Export your packages so other parts of the app can use them
    exports com.zest;
    exports com.zest.controller;
    exports com.zest.dao;
    exports com.zest.model;
    exports com.zest.logic;
}