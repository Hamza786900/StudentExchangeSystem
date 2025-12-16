module studentExchangeSystem {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.management;

    opens com.studentexchange to javafx.fxml;
    exports com.studentexchange;
    exports com.studentexchange.gui;
}
