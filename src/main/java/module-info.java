module com.unisa.progettogruppocinque {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens application to javafx.fxml;
    exports application;
    exports controller;
    opens controller to javafx.fxml;
}