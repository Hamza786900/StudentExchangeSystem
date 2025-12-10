module com.example.studentexchangesystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.studentexchangesystem to javafx.fxml;
    exports com.example.studentexchangesystem;
}