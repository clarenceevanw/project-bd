package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.*;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

public class TambahPesertaKegiatanController extends BaseController{
    @FXML
    private VBox sidebar;

    @FXML
    private VBox formContainer;

    @FXML
    private TextField txtNrp;

    @FXML
    private TextField txtEmail;

    public void initialize() throws Exception {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
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
    public void handleSimpan() throws Exception{
        String nrp = txtNrp.getText().toLowerCase().trim();
        String email = txtEmail.getText().toLowerCase().trim();

        if (nrp.isEmpty() || email.isEmpty()) {
            AlertNotification.showError("Semua field harus diisi.");
            return;
        }

        Mahasiswa mhs = mhsDAO.findByNrpEmail(nrp, email);
        if (mhs == null) {
            AlertNotification.showError("Mahasiswa tidak ditemukan.");
            return;
        }


        Kegiatan kegiatan = ClubSession.getInstance().getKegiatan();
        Club clubPenyelenggara = kegiatan.getClub();
        if(kegiatan.getKategori().equals("Rutin")){
            Keanggotaan keanggotaan = keanggotaanDAO.findKeanggotaanByClubAndMahasiswa(clubPenyelenggara.getId_club(), mhs.getIdMahasiswa());
            if(keanggotaan == null){
                AlertNotification.showError("Mahasiswa harus menjadi anggota club penyelenggara.");
                return;
            }
        }
        PesertaKegiatan peserta = pesertaKegiatanDAO.findByMahasiswaAndKegiatan(mhs, kegiatan);
        if(peserta != null){
            AlertNotification.showError("Mahasiswa sudah terdaftar pada kegiatan ini.");
            return;
        }

        PesertaKegiatan pesertaKegiatan = new PesertaKegiatan();
        pesertaKegiatan.setMahasiswa(mhs);
        pesertaKegiatan.setKegiatan(kegiatan);

        pesertaKegiatan.setIdPesertaKegiatan(pesertaKegiatanDAO.insert(pesertaKegiatan));

        List<JadwalKegiatan> jadwalKegiatanList = jadwalKegiatanDAO.findByKegiatan(kegiatan);
        if(!jadwalKegiatanList.isEmpty()){
            for (JadwalKegiatan jadwalKegiatan : jadwalKegiatanList) {
                PresensiKegiatan presensiKegiatan = new PresensiKegiatan();
                presensiKegiatan.setPesertaKegiatan(pesertaKegiatan);
                presensiKegiatan.setJadwalKegiatan(jadwalKegiatan);
                presensiKegiatan.setStatusPresensi("Tidak Hadir");
                presensiKegiatanDAO.insert(presensiKegiatan);
            }
        }
        AlertNotification.showSuccess("Peserta berhasil ditambahkan.");
        switchScenes("pengurus/peserta-kegiatan.fxml", "Kelola Kegiatan");
    }

    @FXML
    public void handleKembali() throws Exception{
        switchScenes("pengurus/peserta-kegiatan.fxml", "Kelola Kegiatan");
    }
}
