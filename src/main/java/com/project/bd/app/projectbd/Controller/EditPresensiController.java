package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.PresensiKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditPresensiController extends BaseController{
    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnKelolaClub;

    @FXML
    private Button btnKelolaKegiatan;

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
