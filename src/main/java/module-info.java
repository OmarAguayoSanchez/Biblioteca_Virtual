module a.biblioteca_virtual {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens a.biblioteca_virtual to javafx.fxml;
    opens a.biblioteca_virtual.controller to javafx.fxml;
    opens a.biblioteca_virtual.Model to javafx.base;

    exports a.biblioteca_virtual;
}