package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.JenisKegiatan;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Model.PesertaKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.List;

public class EditKegiatanController extends BaseController {
    @FXML
    private VBox sidebar;

    @FXML
    private VBox formContainer;

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
            comboKategori.getItems().addAll("Rutin", "Eksternal");

            Kegiatan kegiatan = ClubSession.getInstance().getKegiatan();
            if(kegiatan == null) return;
            txtNama.setText(kegiatan.getNama());
            txtDokumentasi.setText(kegiatan.getLinkDokumentasi());
            tanggalMulaiPicker.setValue(kegiatan.getTanggalMulai());
            tanggalSelesaiPicker.setValue(kegiatan.getTanggalSelesai());
            comboJenis.getSelectionModel().select(kegiatan.getJenisKegiatan());
            comboKategori.getSelectionModel().select(kegiatan.getKategori());
        } catch (Exception e) {
            try{
                AlertNotification.showError(e.getMessage());
            } catch (Exception er){
                throw new RuntimeException(er);
            }
        }
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

        Kegiatan kegiatan = ClubSession.getInstance().getKegiatan();
        kegiatan.setNama(nama);
        kegiatan.setLinkDokumentasi(dokumentasi);
        kegiatan.setClub(ClubSession.getInstance().getClub());
        kegiatan.setJenisKegiatan(jenis);
        kegiatan.setKategori(kategori);
        kegiatan.setTanggalMulai(tanggalMulai);
        kegiatan.setTanggalSelesai(tanggalSelesai);
        try{
            kegiatanDAO.update(kegiatan);
        } catch (Exception e) {
            AlertNotification.showError(e.getMessage());
            return;
        }

        List<PesertaKegiatan> pesertaKegiatan = pesertaKegiatanDAO.findByKegiatan(kegiatan);
        if(!pesertaKegiatan.isEmpty()) {
            for (PesertaKegiatan peserta : pesertaKegiatan) {
                if (kategori.equals("Rutin")) {
                    peserta.setStatusSertifikat("Tidak");
                    peserta.setNomorSertifikat(null);
                    peserta.setTglSertifikat(null);
                    pesertaKegiatanDAO.updateSertifikatByKegiatan(peserta);
                    List<Keanggotaan> keanggotaanList = keanggotaanDAO.findKeanggotaanByMahasiswa(peserta.getMahasiswa().getIdMahasiswa());
                    boolean isAnggota = false;
                    for (Keanggotaan keanggotaan : keanggotaanList) {
                        if (keanggotaan.getClub().getId_club().equals(kegiatan.getClub().getId_club())) {
                            isAnggota = true;
                            break;
                        }
                    }
                    if(!isAnggota) {
                        pesertaKegiatanDAO.delete(peserta.getIdPesertaKegiatan());
                    }

                } else if (kategori.equals("Eksternal")) {
                    peserta.setStatusSertifikat("Ada");
                    pesertaKegiatanDAO.updateSertifikatByKegiatan(peserta);
                }
            }
        }
        ClubSession.getInstance().setKegiatan(kegiatan);

        switchScenes("pengurus/kegiatan.fxml", "Kelola Kegiatan");
        AlertNotification.showSuccess("Kegiatan berhasil diperbarui.");
    }

    @FXML
    public void handleKembali() throws Exception {
        switchScenes("pengurus/kegiatan.fxml", "Kelola Kegiatan");
    }
}
