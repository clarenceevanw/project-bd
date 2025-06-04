package com.project.bd.app.projectbd;

import com.project.bd.app.projectbd.Controller.LoginController;
import com.project.bd.app.projectbd.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = fxmlLoader.load();

        Image icon = new Image("file:src/main/resources/com/project/bd/app/projectbd/image/iconPCUFix.png");

        LoginController controller = fxmlLoader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/project/bd/app/projectbd/css/style.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Login");

        DatabaseConnection.getConnection();
        stage.centerOnScreen();
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.getIcons().add(icon);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}