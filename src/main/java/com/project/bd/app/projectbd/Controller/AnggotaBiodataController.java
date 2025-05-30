package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.MahasiswaDAO;
import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
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
    private Label jurusanLabel;

    @FXML
    private VBox programBox;

    @FXML
    private VBox prodiBox;

    @FXML
    private VBox jurusanBox;

    @FXML
    private VBox nrpBox;

    @FXML
    private VBox namaBox;

    @FXML
    private VBox emailBox;

    @FXML
    public void initialize() throws Exception {
        // Posisi awal di luar layar (geser ke kiri)
        sidebarAnggotaBiodata.setTranslateX(-300);

        // Animasi slide in dari kiri
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebarAnggotaBiodata);
        slideIn.setToX(0);
        slideIn.play();
        Platform.runLater(() -> {
            try {
                showBiodata();
            } catch (Exception e) {
                e.getMessage();
            }
        });
    }

    private void showBiodata() throws Exception {
        UUID idMahasiswa = LoginSession.getInstance().getIdMahasiswa();
        Mahasiswa mahasiswa = mhsDAO.findById(idMahasiswa);

        if (mahasiswa != null) {
            nrpLabel.setText(mahasiswa.getNrp().toUpperCase());
            namaLabel.setText(mahasiswa.getNama());
            emailLabel.setText(mahasiswa.getEmail());

            if (mahasiswa.getProgram() != null) {
                programLabel.setText(mahasiswa.getProgram().getNama());
                prodiLabel.setText(mahasiswa.getProdi().getNama());

                programBox.setVisible(true);
                programBox.setManaged(true);
                prodiBox.setVisible(true);
                prodiBox.setManaged(true);

                jurusanBox.setVisible(false);
                jurusanBox.setManaged(false);
            } else {
                jurusanLabel.setText(mahasiswa.getProdi().getNama());

                jurusanBox.setVisible(true);
                jurusanBox.setManaged(true);

                programBox.setVisible(false);
                programBox.setManaged(false);
                prodiBox.setVisible(false);
                prodiBox.setManaged(false);
            }
            animateBiodataItems();
        }
    }

    private void animateBiodataItems() {
        List<VBox> items = new ArrayList<>(List.of(nrpBox, namaBox, emailBox));

        // Cek apakah program ditampilkan atau jurusan
        if (programBox.isVisible()) {
            items.add(programBox);
            items.add(prodiBox);
        } else {
            items.add(jurusanBox);
        }

        // Animasi berurutan
        Duration delayBetween = Duration.millis(150);
        Duration duration = Duration.millis(500);

        for (int i = 0; i < items.size(); i++) {
            VBox item = items.get(i);
            item.setOpacity(0);
            item.setTranslateY(-20);

            FadeTransition fade = new FadeTransition(duration, item);
            fade.setFromValue(0);
            fade.setToValue(1);

            TranslateTransition slide = new TranslateTransition(duration, item);
            slide.setFromY(-20);
            slide.setToY(0);

            ParallelTransition transition = new ParallelTransition(fade, slide);
            transition.setDelay(delayBetween.multiply(i));
            transition.play();
        }
    }
    @FXML
    public void goToAnggotaDashboard() throws Exception {
        try {
            switchScenes("anggota/dashboard.fxml", "Kembali ke Dashboard");
        } catch (Exception e) {
            AlertNotification.showError("Gagal kembali ke dashboard: " + e.getMessage());
        }

    }

}
