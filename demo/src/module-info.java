module demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens GUI to javafx.fxml;
    exports GUI;
}