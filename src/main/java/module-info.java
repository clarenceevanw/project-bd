module com.project.bd.app.projectbd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.project.bd.app.projectbd.Controller to javafx.fxml;
    exports com.project.bd.app.projectbd;
}