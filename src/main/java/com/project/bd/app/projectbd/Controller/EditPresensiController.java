package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.PresensiKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class EditPresensiController extends BaseController{
    @FXML
    private VBox sidebar;

    @FXML
    private VBox formContainer;

    @FXML
    private TextField txtNrp;

    @FXML
    private TextField txtNama;

    @FXML
    ComboBox<String> comboKehadiran;

    public void initialize() {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        comboKehadiran.getItems().addAll("Hadir", "Tidak Hadir", "Izin");

        PresensiKegiatan presensiKegiatan = ClubSession.getInstance().getPresensiKegiatan();
        txtNrp.setText(presensiKegiatan.getPesertaKegiatan().getMahasiswa().getNrp());
        txtNrp.setEditable(false);
        txtNama.setText(presensiKegiatan.getPesertaKegiatan().getMahasiswa().getNama());
        txtNama.setEditable(false);
        comboKehadiran.getSelectionModel().select(presensiKegiatan.getStatusPresensi());

        sidebar.setTranslateX(-300);
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebar);
        slideIn.setToX(0);
        slideIn.setInterpolator(Interpolator.EASE_BOTH);
        slideIn.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), formContainer);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
        fadeTransition.play();
    }

    @FXML
    public void handleSimpan() throws Exception {
        PresensiKegiatan presensiKegiatan = ClubSession.getInstance().getPresensiKegiatan();
        presensiKegiatan.setStatusPresensi(comboKehadiran.getValue());
        presensiKegiatanDAO.update(presensiKegiatan);
        switchScenes("pengurus/presensi-jadwal-kegiatan.fxml", "Presensi Kegiatan");
        AlertNotification.showSuccess("Presensi kegiatan berhasil diperbarui.");
    }

    @FXML
    public void handleKembali() throws Exception {
        switchScenes("pengurus/presensi-jadwal-kegiatan.fxml", "Presensi Kegiatan");
    }

    @FXML
    public void navigateToDashboard() throws Exception {
        switchScenes("pengurus/dashboard.fxml", "Dashboard");
    }

    @FXML
    public void navigateToKelolaClub() throws Exception {
        switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
    }

    @FXML
    public void navigateToKelolaKegiatan() throws Exception {
        switchScenes("pengurus/kelola-kegiatan.fxml", "Kelola Kegiatan");
    }
}
