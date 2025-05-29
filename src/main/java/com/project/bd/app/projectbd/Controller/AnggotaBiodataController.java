package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.MahasiswaDAO;
import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.UUID;

public class AnggotaBiodataController extends BaseController {

    @FXML
    private Label nrpLabel;

    @FXML
    private Label namaLabel;

    @FXML
    private Label programLabel;

    @FXML
    private Label prodiLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private VBox sidebarAnggotaBiodata;

    @FXML
    public void initialize() {
        // Posisi awal di luar layar (geser ke kiri)
        sidebarAnggotaBiodata.setTranslateX(-300);

        // Animasi slide in dari kiri
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebarAnggotaBiodata);
        slideIn.setToX(0);
        slideIn.play();
    }

    private void showBiodata() throws Exception {
        // Ambil data dari session login
        UUID idMahasiswa = LoginSession.getInstance().getIdMahasiswa();
        MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
        Mahasiswa mahasiswa = mahasiswaDAO.findById(idMahasiswa);
        if(mahasiswa != null){
            nrpLabel.setText(mahasiswa.getNrp());
            namaLabel.setText(mahasiswa.getNama());
//            programLabel.setText(mahasiswa.getIdProgram());
//            prodiLabel.setText(mahasiswa.getIdProdi());
            emailLabel.setText(mahasiswa.getEmail());
        }
    }
}
