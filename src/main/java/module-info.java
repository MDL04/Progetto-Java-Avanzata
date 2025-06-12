module com.unisa.progettogruppocinque {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;

    opens application to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.base;

    exports application;
    exports controller;
}
