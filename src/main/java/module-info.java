module com.example.bmi2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.bmi2 to javafx.fxml;
    exports com.example.bmi2;
}