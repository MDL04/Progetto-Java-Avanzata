module com.unisa.progettogruppocinque {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires eu.hansolo.tilesfx;

    opens com.mdl04.progettogruppo05 to javafx.fxml;
    exports com.mdl04.progettogruppo05;
}