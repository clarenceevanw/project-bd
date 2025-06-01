package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.JadwalKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TambahJadwalKegiatanController extends BaseController {
    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnKelolaClub;

    @FXML
    private Button btnKelolaKegiatan;

    @FXML
    private DatePicker datePickerTanggal;

    @FXML
    private TextField txtJam;

    @FXML
    private TextField txtLokasi;

    public void initialize() {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
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

        if(!(timeText.matches("\\d{2}:\\d{2}"))){
            AlertNotification.showError("Format waktu harus HH:MM.");
            return;
        }

        try {
            LocalTime time = LocalTime.parse(timeText);
            LocalDateTime waktuKegiatan = LocalDateTime.of(tanggal, time);
            JadwalKegiatan jadwalKegiatan = new JadwalKegiatan();
            jadwalKegiatan.setWaktuKegiatan(waktuKegiatan);
            jadwalKegiatan.setLokasiKegiatan(lokasi);
            jadwalKegiatan.setKegiatan(ClubSession.getInstance().getKegiatan());
            jadwalKegiatanDAO.insert(jadwalKegiatan);
            AlertNotification.showSuccess("Jadwal kegiatan berhasil ditambahkan.");
            switchScenes("pengurus/jadwal-kegiatan.fxml", "Kelola Kegiatan");
        } catch (Exception e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void handleKembali() throws Exception {
        switchScenes("pengurus/jadwal-kegiatan.fxml", "Kelola Kegiatan");
    }
}
