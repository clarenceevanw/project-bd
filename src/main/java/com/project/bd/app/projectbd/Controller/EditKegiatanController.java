package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.JenisKegiatan;
import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.Model.PesertaKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

public class EditKegiatanController extends BaseController {
    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtDokumentasi;

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
            comboJenis.getSelectionModel().select(kegiatan.getJenisKegiatan());
            comboKategori.getSelectionModel().select(kegiatan.getKategori());
        } catch (Exception e) {
            try{
                AlertNotification.showError(e.getMessage());
            } catch (Exception er){
                throw new RuntimeException(er);
            }
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
        String nama = txtNama.getText().trim();
        String dokumentasi = txtDokumentasi.getText().trim();
        JenisKegiatan jenis = comboJenis.getSelectionModel().getSelectedItem();
        String kategori = comboKategori.getSelectionModel().getSelectedItem();

        if (nama.isEmpty() || dokumentasi.isEmpty() || jenis == null || kategori == null) {
            AlertNotification.showError("Semua field harus diisi.");
            return;
        }

        Kegiatan kegiatan = ClubSession.getInstance().getKegiatan();
        kegiatan.setNama(nama);
        kegiatan.setLinkDokumentasi(dokumentasi);
        kegiatan.setClub(ClubSession.getInstance().getClub());
        kegiatan.setJenisKegiatan(jenis);
        kegiatan.setKategori(kategori);
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
