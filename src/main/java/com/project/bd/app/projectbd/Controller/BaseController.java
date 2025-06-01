package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.*;
import com.project.bd.app.projectbd.utils.DatabaseConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {
    protected Stage stage;

    protected ClubDAO clubDAO = new ClubDAO();

    protected KeanggotaanDAO keanggotaanDAO = new KeanggotaanDAO();

    protected KategoriDAO kategoriDAO = new KategoriDAO();

    protected MahasiswaDAO mhsDAO = new MahasiswaDAO();

    protected KegiatanDAO kegiatanDAO = new KegiatanDAO();

    protected JenisKegiatanDAO jenisKegiatanDAO = new JenisKegiatanDAO();

    protected JadwalKegiatanDAO jadwalKegiatanDAO = new JadwalKegiatanDAO();

    protected PresensiDAO presensiKegiatanDAO = new PresensiDAO();

    protected PesertaKegiatanDAO pesertaKegiatanDAO = new PesertaKegiatanDAO();

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

    public void setActiveSidebarButton(String page, Button btnDashboard, Button btnKelolaClub, Button btnKelolaKegiatan) {
        String active = "-fx-background-color: #4B208E; -fx-text-fill: white; -fx-font-weight: bold;";
        String inactive = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold;";

        btnDashboard.setStyle(inactive);
        btnKelolaClub.setStyle(inactive);
        btnKelolaKegiatan.setStyle(inactive);

        if (page.equals("dashboard")) {
            btnDashboard.setStyle(active);
        } else if (page.equals("kelola")) {
            btnKelolaClub.setStyle(active);
        } else if (page.equals("kegiatan")){
            btnKelolaKegiatan.setStyle(active);
        }
    }
}
