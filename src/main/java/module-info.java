module com.unisa.progettogruppocinque {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires eu.hansolo.tilesfx;

    opens application to javafx.fxml;
    exports application;
    exports controller;
    opens controller to javafx.fxml;
}