module com.example.studentexchangesystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.studentexchange to javafx.fxml;
    exports com.studentexchange;
}