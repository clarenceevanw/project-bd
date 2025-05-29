package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Kategori;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

public class BuatClubController extends BaseController{

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnKelolaClub;

    @FXML
    private Button btnKelolaKegiatan;

    @FXML
    private TextField txtNama;

    @FXML
    private TextArea txtDeskripsi;

    @FXML
    private TextField txtTahun;

    @FXML
    private ComboBox<Kategori> comboKategori;

    ObservableList<Kategori> kategoriList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setActiveSidebarButton("kelola", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        try{
            kategoriList.addAll(kategoriDAO.findAll());
        } catch (Exception e) {
            try{
                AlertNotification.showError(e.getMessage());
            } catch (Exception er){
                throw new RuntimeException(er);
            }
        }
        comboKategori.setItems(kategoriList);
    }

    @FXML
    private void handleSimpan() throws Exception {
        String nama = txtNama.getText();
        String deskripsi = txtDeskripsi.getText();
        String tahun = txtTahun.getText();
        Kategori kategori = comboKategori.getValue();

        if (nama.isEmpty() || deskripsi.isEmpty() || tahun.isEmpty() || kategori == null) {
            AlertNotification.showError("Semua field harus diisi.");
            return;
        }

        try {
            int tahunInt = Integer.parseInt(tahun);

            Club club = new Club();
            club.setNama(nama);
            club.setDeskripsi(deskripsi);
            club.setTahun_berdiri(tahunInt);
            club.setKategori(kategori);

            try{
                clubDAO.insert(club);
            } catch (Exception e) {
                AlertNotification.showError(e.getMessage());
                switchScenes("pengurus/dashboard.fxml", "Dashboard");
            }

            try {
                LocalDate date = LocalDate.now();
                Keanggotaan keanggotaan = new Keanggotaan();
                Mahasiswa mhs = mhsDAO.findById(LoginSession.getInstance().getIdMahasiswa());
                keanggotaan.setMahasiswa(mhs);
                keanggotaan.setClub(club);
                keanggotaan.setPeran("Pengurus");
                keanggotaan.setTanggal_bergabung(date);
                keanggotaanDAO.insert(keanggotaan);
                switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
                AlertNotification.showSuccess("Club berhasil dibuat.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (NumberFormatException e) {
            AlertNotification.showError("Tahun berdiri harus berupa angka.");
        }
    }

    @FXML
    private void handleKembali() throws Exception {
        try{
            switchScenes("pengurus/dashboard.fxml", "Dashboard");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    private void navigateToDashboard() throws Exception {
        try{
            switchScenes("pengurus/dashboard.fxml", "Dashboard");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    private void navigateToKelolaClub() throws Exception {
        try{
            switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    private void navigateToKelolaKegiatan() throws Exception {
        try{
            switchScenes("pengurus/kelola-kegiatan.fxml", "Kelola Kegiatan");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }
}
