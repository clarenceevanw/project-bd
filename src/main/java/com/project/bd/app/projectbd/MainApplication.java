package com.project.bd.app.projectbd;

import com.project.bd.app.projectbd.Controller.LoginController;
import com.project.bd.app.projectbd.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = fxmlLoader.load();

        LoginController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setScene(new Scene(root));
        stage.setTitle("Login");

        DatabaseConnection.getConnection();
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}