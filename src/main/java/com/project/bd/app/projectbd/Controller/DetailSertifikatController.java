package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.PesertaKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.Session.PageSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DetailSertifikatController extends BaseController{
    @FXML
    private Label noLabel;

    @FXML
    private Label namaKegiatanLabel;

    @FXML
    private Label tanggalLabel;

    @FXML
    private Label namaPesertaLabel;

    private PesertaKegiatan pesertaKegiatan = ClubSession.getInstance().getPesertaKegiatan();
    public void initialize() {
        noLabel.setText(pesertaKegiatan.getNomorSertifikat() != null ? pesertaKegiatan.getNomorSertifikat() : "Belum Terbit");
        namaKegiatanLabel.setText(pesertaKegiatan.getKegiatan().getNama());
        tanggalLabel.setText(pesertaKegiatan.getTglSertifikat() != null ? pesertaKegiatan.getTglSertifikat().toString() : "Belum Terbit");
        namaPesertaLabel.setText(pesertaKegiatan.getMahasiswa().getNama());
    }

    @FXML
    public void goBack() throws Exception {
        String originPage = PageSession.getInstance().getOriginPage();
        if(originPage != null) {
            if (originPage.equals("lihatSertifikat")) {
                switchScenes("anggota/lihatSertifikat.fxml", "Lihat Sertifikat");
            }
        }else {
            switchScenes("anggota/lihatSertifikat.fxml", "Lihat Sertifikat");
        }
    }
}
