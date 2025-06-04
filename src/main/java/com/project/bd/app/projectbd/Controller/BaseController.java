package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.*;
import com.project.bd.app.projectbd.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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

    @FXML
    protected Button btnDashboard, btnKelolaClub, btnKelolaKegiatan, btnDaftarClub, btnClubDiikuti, btnDaftarKegiatan, btnKegiatanDiikuti, btnSertifikat;

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
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/project/bd/app/projectbd/css/style.css")).toExternalForm());
        stage.setScene(scene);
        DatabaseConnection.getConnection();
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setFullScreen(false);
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

    public void setActiveSideBarButton(String page, Button btnDashboard, Button btnDaftarClub, Button btnClubDiikuti, Button btnDaftarKegiatan, Button btnKegiatanDiikuti, Button btnSertifikat) {
        String active = "-fx-background-color: #4B208E; -fx-text-fill: white; -fx-font-weight: bold;";
        String inactive = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold;";

        btnDashboard.setStyle(inactive);
        btnDaftarClub.setStyle(inactive);
        btnClubDiikuti.setStyle(inactive);
        btnDaftarKegiatan.setStyle(inactive);
        btnKegiatanDiikuti.setStyle(inactive);
        btnSertifikat.setStyle(inactive);

        if (page.equals("dashboard")) {
            btnDashboard.setStyle(active);
        } else if (page.equals("daftarClub")) {
            btnDaftarClub.setStyle(active);
        } else if (page.equals("clubDiikuti")){
            btnClubDiikuti.setStyle(active);
        } else if (page.equals("daftarKegiatan")){
            btnDaftarKegiatan.setStyle(active);
        } else if (page.equals("kegiatanDiikuti")){
            btnKegiatanDiikuti.setStyle(active);
        } else if (page.equals("sertifikat")){
            btnSertifikat.setStyle(active);
        }
    }
}
