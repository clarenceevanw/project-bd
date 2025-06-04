package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.JenisKegiatan;
import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class TambahKegiatanController extends BaseController {
    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtDokumentasi;

    @FXML
    private DatePicker tanggalMulaiPicker;

    @FXML
    private DatePicker tanggalSelesaiPicker;

    @FXML
    private ComboBox<JenisKegiatan> comboJenis;

    @FXML
    private ComboBox<String> comboKategori;

    @FXML
    public void initialize() {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        try{
            comboJenis.getItems().addAll(jenisKegiatanDAO.findAll());
        } catch (Exception e) {
            try{
                AlertNotification.showError(e.getMessage());
            } catch (Exception er){
                throw new RuntimeException(er);
            }
        }
        comboKategori.getItems().addAll("Rutin", "Eksternal");
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
        String nama = txtNama.getText().trim();
        String dokumentasi = txtDokumentasi.getText().trim();
        JenisKegiatan jenis = comboJenis.getSelectionModel().getSelectedItem();
        String kategori = comboKategori.getSelectionModel().getSelectedItem();
        LocalDate tanggalMulai = tanggalMulaiPicker.getValue();
        LocalDate tanggalSelesai = tanggalSelesaiPicker.getValue();

        if (nama.isEmpty() || dokumentasi.isEmpty() || jenis == null || kategori == null || tanggalMulai == null || tanggalSelesai == null) {
            AlertNotification.showError("Semua field harus diisi.");
            return;
        }

        if(tanggalMulai.isAfter(tanggalSelesai)) {
            AlertNotification.showError("Tanggal mulai harus sebelum tanggal selesai.");
            return;
        }

        if(tanggalMulai.isBefore(LocalDate.now())) {
            AlertNotification.showError("Tanggal mulai harus setelah hari ini.");
            return;
        }

        if(tanggalSelesai.isBefore(LocalDate.now())) {
            AlertNotification.showError("Tanggal selesai harus setelah hari ini.");
            return;
        }

        if(tanggalSelesai.isBefore(tanggalMulai)) {
            AlertNotification.showError("Tanggal selesai harus setelah tanggal mulai.");
            return;
        }

        Kegiatan kegiatan = new Kegiatan();
        kegiatan.setNama(nama);
        kegiatan.setLinkDokumentasi(dokumentasi);
        kegiatan.setClub(ClubSession.getInstance().getClub());
        kegiatan.setJenisKegiatan(jenis);
        kegiatan.setKategori(kategori);
        kegiatan.setTanggalMulai(tanggalMulai);
        kegiatan.setTanggalSelesai(tanggalSelesai);
        try{
            kegiatanDAO.insert(kegiatan);
        } catch (Exception e) {
            AlertNotification.showError(e.getMessage());
            return;
        }
        AlertNotification.showSuccess("Kegiatan berhasil disimpan.");
        switchScenes("pengurus/kegiatan.fxml", "Kelola Kegiatan");
    }

    @FXML
    public void handleKembali() throws Exception {
        switchScenes("pengurus/kegiatan.fxml", "Kelola Kegiatan");
    }
}
