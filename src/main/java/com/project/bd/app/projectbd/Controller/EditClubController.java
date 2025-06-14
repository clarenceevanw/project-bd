package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Kategori;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class EditClubController extends BaseController{
    @FXML
    private VBox sidebar;

    @FXML
    private VBox mainContent;

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
        try {
            kategoriList.addAll(kategoriDAO.findAll());
            comboKategori.setItems(kategoriList);

            Club club = ClubSession.getInstance().getClub();
            if (club != null) {
                txtNama.setText(club.getNama());
                txtDeskripsi.setText(club.getDeskripsi());
                txtTahun.setText(String.valueOf(club.getTahun_berdiri()));

                // Pilih kategori yang sesuai di combo box
                for (Kategori kategori : kategoriList) {
                    if (kategori.getId_kategori().equals(club.getKategori().getId_kategori())) {
                        comboKategori.getSelectionModel().select(kategori);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            try{
                AlertNotification.showError("Gagal inisialisasi data club: " + e.getMessage());
            } catch (Exception er) {
                throw new RuntimeException(er);
            }
        }

        sidebar.setTranslateX(-300);
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebar);
        slideIn.setToX(0);
        slideIn.setInterpolator(Interpolator.EASE_BOTH);
        slideIn.play();

        FadeTransition fade = new FadeTransition(Duration.millis(1000), mainContent);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(Interpolator.EASE_BOTH);
        fade.play();
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

        if(deskripsi.length() < 20){
            AlertNotification.showError("Deskripsi minimal 20 karakter.");
            return;
        }

        try {
            int tahunInt = Integer.parseInt(tahun);

            Club club = new Club();
            club.setId_club(ClubSession.getInstance().getClub().getId_club());
            club.setNama(nama);
            club.setDeskripsi(deskripsi);
            club.setTahun_berdiri(tahunInt);
            club.setKategori(kategori);

            try {
                clubDAO.update(club);
                switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
                AlertNotification.showSuccess("Club berhasil diperbarui.");
            } catch (Exception e) {
                AlertNotification.showError(e.getMessage());
                switchScenes("pengurus/dashboard.fxml", "Dashboard");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleKembali() throws Exception {
        try{
            switchScenes("pengurus/kelola-club.fxml", "Dashboard");
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
