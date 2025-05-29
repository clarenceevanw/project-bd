package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EditAnggotaClubController extends BaseController {
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
    private TextField txtEmail;

    @FXML
    private TextField txtJurusan;

    @FXML
    private ComboBox<String> comboPeran;

    @FXML
    private ComboBox<String> comboStatus;

    public void initialize() {
        setActiveSidebarButton("kelola", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        comboPeran.getItems().addAll("Pengurus", "Anggota");
        comboStatus.getItems().addAll("Aktif", "Tidak Aktif");
        loadData();

        txtNrp.setEditable(false);
        txtNama.setEditable(false);
        txtEmail.setEditable(false);
        txtJurusan.setEditable(false);
    }

    public void loadData() {
        Keanggotaan keanggotaan = ClubSession.getInstance().getKeanggotaan();
        txtNrp.setText(keanggotaan.getMahasiswa().getNrp());
        txtNama.setText(keanggotaan.getMahasiswa().getNama());
        txtEmail.setText(keanggotaan.getMahasiswa().getEmail());
        txtJurusan.setText(keanggotaan.getMahasiswa().getProdi().getNama());
        comboPeran.setValue(keanggotaan.getPeran());
        comboStatus.setValue(keanggotaan.getStatus());
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

    @FXML
    private void handleSimpan() throws Exception {
        String peran = comboPeran.getValue();
        String status = comboStatus.getValue();

        Keanggotaan keanggotaan = ClubSession.getInstance().getKeanggotaan();
        keanggotaan.setPeran(peran);
        keanggotaan.setStatus(status);

        try {
            keanggotaanDAO.update(keanggotaan);
            AlertNotification.showSuccess("Data berhasil disimpan.");
            switchScenes("pengurus/kelola-anggota-club.fxml", "Kelola Anggota Club");
        } catch (Exception e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    private void handleKembali() throws Exception {
        try{
            switchScenes("pengurus/kelola-anggota-club.fxml", "Kelola Anggota Club");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }
}
