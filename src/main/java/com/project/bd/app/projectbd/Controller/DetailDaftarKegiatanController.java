package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.*;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.Session.PageSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.List;

import java.io.IOException;
import java.util.UUID;


public class DetailDaftarKegiatanController extends BaseController {
    @FXML
    private Label namaLabel;

    @FXML
    private Label tanggalMulaiLabel;

    @FXML
    private Label tanggalSelesaiLabel;

    @FXML
    private Label jenisKegiatanLabel;

    @FXML
    private Label kategoriLabel;

    @FXML
    private StackPane judulBox;

    @FXML
    private Pane tanggalMulaiBox;

    @FXML
    private Pane tanggalSelesaiBox;

    @FXML
    private Pane jenisKegiatanBox;

    @FXML
    private Pane kategoriBox;

    @FXML
    private Button btnJoin;

    @FXML
    private Button btnLihatJadwal;

    private Kegiatan kegiatan = ClubSession.getInstance().getKegiatan();

    public Mahasiswa getMahasiswa() throws Exception {
        return mhsDAO.findById(LoginSession.getInstance().getIdMahasiswa());
    }

    @FXML
    public void initialize() throws Exception {
        tanggalMulaiBox.setTranslateX(-600);
        tanggalSelesaiBox.setTranslateX(-600);
        jenisKegiatanBox.setTranslateX(-600);
        kategoriBox.setTranslateX(-600);

        try{
            setKegiatanDetail();
            loadButton();
        } catch (Exception e) {
            AlertNotification.showError(e.getMessage());
        }
        playAnimations();
    }

    private void loadButton() throws Exception {
        Mahasiswa mahasiswa = getMahasiswa();
        if (mahasiswa != null) {
            if (pesertaKegiatanDAO.findByMahasiswaAndKegiatan(mahasiswa, kegiatan) == null) {
                btnJoin.setVisible(true);
                btnJoin.setManaged(true);

                btnLihatJadwal.setVisible(false);
                btnLihatJadwal.setManaged(false);
            } else {
                btnJoin.setVisible(false);
                btnJoin.setManaged(false);

                btnLihatJadwal.setVisible(true);
                btnLihatJadwal.setManaged(true);
            }
        }
    }

    private void setKegiatanDetail() throws Exception {
        namaLabel.setText(kegiatan.getNama());
        tanggalMulaiLabel.setText(kegiatan.getTanggalMulai().toString());
        tanggalSelesaiLabel.setText(kegiatan.getTanggalSelesai().toString());
        jenisKegiatanLabel.setText(kegiatan.getJenisKegiatan().getNama());
        kategoriLabel.setText(kegiatan.getKategori());
    }

    private void playAnimations() {
        TranslateTransition slideJudul = new TranslateTransition(Duration.millis(500), judulBox);
        slideJudul.setFromY(-100);
        slideJudul.setToY(20);

        TranslateTransition slideTanggalMasuk = new TranslateTransition(Duration.millis(400), tanggalMulaiBox);
        slideTanggalMasuk.setFromX(-600);
        slideTanggalMasuk.setToX(0);

        TranslateTransition slideTanggalSelesai = new TranslateTransition(Duration.millis(400), tanggalSelesaiBox);
        slideTanggalSelesai.setFromX(-600);
        slideTanggalSelesai.setToX(0);
        slideTanggalSelesai.setDelay(Duration.millis(100));

        TranslateTransition slideJenis = new TranslateTransition(Duration.millis(400), jenisKegiatanBox);
        slideJenis.setFromX(-600);
        slideJenis.setToX(0);
        slideJenis.setDelay(Duration.millis(100));

        TranslateTransition slideKategori = new TranslateTransition(Duration.millis(400), kategoriBox);
        slideKategori.setFromX(-600);
        slideKategori.setToX(0);
        slideKategori.setDelay(Duration.millis(200));

        slideJudul.play();
        slideTanggalMasuk.play();
        slideTanggalSelesai.play();
        slideJenis.play();
        slideKategori.play();
    }

    @FXML
    public void handleBack() throws IOException {
        String originPage = PageSession.getInstance().getOriginPage();
        if(originPage != null) {
            if(originPage.equals("daftarKegiatan")){
                switchScenes("anggota/daftarKegiatan.fxml", "Daftar Kegiatan");
            } else if (originPage.equals("daftarKegiatanDiikuti")) {
                switchScenes("anggota/daftarKegiatanDiikuti.fxml", "Kegiatan Yang Diikuti");
            }
        }else {
            switchScenes("anggota/daftarKegiatan.fxml", "Daftar Kegiatan");
        }
    }

    @FXML
    public void handleJoinKegiatan() throws Exception {
        if(kegiatan.getKategori().equals("Rutin")){
            List<Keanggotaan> keanggotaan = keanggotaanDAO.findKeanggotaanByMahasiswa(getMahasiswa().getIdMahasiswa());
            if(keanggotaan.isEmpty()) {
                AlertNotification.showError("Anda harus menjadi anggota klub terlebih dahulu untuk bergabung ke kegiatan ini.");
                return;
            }
            boolean isAnggotaClub = false;
            for (Keanggotaan k : keanggotaan) {
                if (k.getStatus().equals("Aktif")) {
                    if(k.getClub().getId_club().equals(kegiatan.getClub().getId_club())) {
                        isAnggotaClub = true;
                        break;
                    }
                }
            }
            if(!isAnggotaClub) {
                AlertNotification.showError("Anda harus menjadi anggota klub " + kegiatan.getClub().getNama() + " terlebih dahulu untuk bergabung ke kegiatan ini.");
                return;
            }
        }
        try {
            PesertaKegiatan pesertaKegiatan = new PesertaKegiatan();
            pesertaKegiatan.setMahasiswa(getMahasiswa());
            pesertaKegiatan.setKegiatan(kegiatan);
            UUID idPeserta = pesertaKegiatanDAO.insert(pesertaKegiatan);
            pesertaKegiatan.setIdPesertaKegiatan(idPeserta);
            List<JadwalKegiatan> jadwalKegiatanList = jadwalKegiatanDAO.findByKegiatan(kegiatan);
            if(!jadwalKegiatanList.isEmpty()) {
                for (JadwalKegiatan jadwalKegiatan : jadwalKegiatanList) {
                    PresensiKegiatan presensiKegiatan = new PresensiKegiatan();
                    presensiKegiatan.setJadwalKegiatan(jadwalKegiatan);
                    presensiKegiatan.setPesertaKegiatan(pesertaKegiatan);
                    presensiKegiatan.setStatusPresensi("Tidak Hadir");
                    presensiKegiatanDAO.insert(presensiKegiatan);
                }
            }

            AlertNotification.showSuccess("Berhasil bergabung ke kegiatan");
            switchScenes("anggota/daftarKegiatan.fxml", "Daftar Kegiatan");
        }catch (Exception e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void handleLihatJadwal() throws Exception {
        ClubSession.getInstance().setKegiatan(kegiatan);
        switchScenes("anggota/lihat-jadwal-kegiatan.fxml", "Jadwal Kegiatan");
    }
}
