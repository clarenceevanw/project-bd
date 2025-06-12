package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.JadwalKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EditJadwalKegiatanController extends BaseController {
    @FXML
    private VBox sidebar;

    @FXML
    private VBox formContainer;

    @FXML
    private DatePicker datePickerTanggal;

    @FXML
    private TextField txtJam;

    @FXML
    private TextField txtLokasi;

    private JadwalKegiatan jadwal = ClubSession.getInstance().getJadwalKegiatan();

    public void initialize() {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);


        if (jadwal != null) {
            // Set tanggal ke DatePicker
            if (jadwal.getWaktuKegiatan() != null) {
                datePickerTanggal.setValue(jadwal.getWaktuKegiatan().toLocalDate());

                // Format jam menjadi "HH:mm"
                txtJam.setText(jadwal.getWaktuKegiatan().toLocalTime().toString());
            }

            // Set lokasi
            txtLokasi.setText(jadwal.getLokasiKegiatan());
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

    @FXML
    public void handleSimpan() throws Exception {
        LocalDate tanggal = datePickerTanggal.getValue();
        String timeText = txtJam.getText();  // contoh: 14:30
        String lokasi = txtLokasi.getText();

        if (tanggal == null && timeText == null && lokasi == null) {
            AlertNotification.showError("Semua field harus diisi.");
            return;
        }

        if(tanggal.isBefore(jadwal.getKegiatan().getTanggalMulai())) {
            AlertNotification.showError("Tanggal harus setelah tanggal mulai kegiatan.");
            return;
        }

        if(tanggal.isAfter(jadwal.getKegiatan().getTanggalSelesai())) {
            AlertNotification.showError("Tanggal harus sebelum tanggal selesai kegiatan.");
            return;
        }

        if(!(timeText.matches("\\d{2}:\\d{2}"))){
            AlertNotification.showError("Format waktu harus HH:MM.");
            return;
        }

        try {
            LocalTime time = LocalTime.parse(timeText);
            LocalDateTime waktuKegiatan = LocalDateTime.of(tanggal, time);
            JadwalKegiatan jadwalKegiatan = ClubSession.getInstance().getJadwalKegiatan();
            jadwalKegiatan.setWaktuKegiatan(waktuKegiatan);
            jadwalKegiatan.setLokasiKegiatan(lokasi);
            jadwalKegiatan.setKegiatan(jadwalKegiatan.getKegiatan());
            jadwalKegiatanDAO.update(jadwalKegiatan);
            AlertNotification.showSuccess("Jadwal kegiatan berhasil diperbarui.");
            switchScenes("pengurus/jadwal-kegiatan.fxml", "Jadwal Kegiatan");
        } catch (Exception e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void handleKembali() throws Exception {
        switchScenes("pengurus/jadwal-kegiatan.fxml", "Kelola Kegiatan");
    }
}
