module a.biblioteca_virtual {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens a.biblioteca_virtual to javafx.fxml;
    exports a.biblioteca_virtual;
}