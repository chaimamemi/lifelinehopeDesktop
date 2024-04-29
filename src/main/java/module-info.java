module com.example.sinda {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.sinda.models to javafx.base;
    opens com.example.sinda to javafx.fxml;
    exports com.example.sinda;

    exports com.example.sinda.controllers.user to javafx.fxml;
    opens com.example.sinda.controllers.user to javafx.fxml;

    exports com.example.sinda.controllers.admin to javafx.fxml;
    opens com.example.sinda.controllers.admin to javafx.fxml;

}