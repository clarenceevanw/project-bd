package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.ClubDAO;
import com.project.bd.app.projectbd.DAO.KeanggotaanDAO;
import com.project.bd.app.projectbd.DAO.MahasiswaDAO;
import com.project.bd.app.projectbd.utils.DatabaseConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {
    protected Stage stage;

    protected ClubDAO clubDAO = new ClubDAO();

    protected KeanggotaanDAO keanggotaanDAO = new KeanggotaanDAO();

    protected MahasiswaDAO mhsDAO = new MahasiswaDAO();


    public Stage getStage(){
        return stage;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void switchScenes(String file, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/bd/app/projectbd/" + file));
        Parent root = loader.load();

        BaseController controller = loader.getController();
        controller.setStage(stage);

        stage.setTitle(title);
        stage.setScene(new Scene(root));
        DatabaseConnection.getConnection();
        stage.show();
        stage.centerOnScreen();
    }
}
