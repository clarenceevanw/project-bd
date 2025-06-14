package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Model.PesertaKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.Session.PageSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LihatSertifikatController extends BaseController{
    @FXML
    private VBox sideBar;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox sertifikatContainer;

    private List<PesertaKegiatan> semuaPesertaKegiatan;

    public void initialize() {
        sideBar.setTranslateX(-300);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        setActiveSideBarButton("sertifikat", btnDashboard, btnDaftarClub, btnClubDiikuti, btnDaftarKegiatan, btnKegiatanDiikuti, btnSertifikat);
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sideBar);
        slideIn.setToX(0);
        slideIn.play();

        try{
            Mahasiswa mhs = mhsDAO.findById(LoginSession.getInstance().getIdMahasiswa());
            this.semuaPesertaKegiatan = pesertaKegiatanDAO.findByMahasiswa(mhs);
            if(this.semuaPesertaKegiatan == null) { // Jaga-jaga jika DAO mengembalikan null
                this.semuaPesertaKegiatan = new ArrayList<>();
            }
        } catch (Exception e) {
            this.semuaPesertaKegiatan = new ArrayList<>();
            AlertNotification.showSuccess("Gagal memuat data peserta kegiatan dari database.");
            throw new RuntimeException(e);
        }

        renderKegiatanCard(this.semuaPesertaKegiatan);
    }

    private void renderKegiatanCard(List<PesertaKegiatan> listPesertaKegiatan) {
        if(listPesertaKegiatan.isEmpty()){
            Label label = new Label("Belum ada sertifikat");
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #888;");
            sertifikatContainer.getChildren().add(label);
            return;
        }

        int count = 0;
        for (int i = 0; i < listPesertaKegiatan.size(); i++) {
            if(!listPesertaKegiatan.get(i).getStatusSertifikat().equals("Ada")) {
                count++;
            }
        }
        
        if(count == listPesertaKegiatan.size()) {
            Label label = new Label("Belum ada sertifikat");
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #888;");
            sertifikatContainer.getChildren().add(label);
            return;
        }

        Duration delayBetween = Duration.millis(150);
        Duration duration = Duration.millis(500);

        for (int i = 0; i < listPesertaKegiatan.size(); i++) {
            PesertaKegiatan pesertaKegiatan = listPesertaKegiatan.get(i);
            if(!pesertaKegiatan.getStatusSertifikat().equals("Ada")) continue;

            VBox box = new VBox();
            box.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                    + "-fx-padding: 10 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 3);");
            box.setSpacing(4);

            Label nama = new Label(pesertaKegiatan.getKegiatan().getNama());
            nama.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label noSertifikat = new Label("No Sertifikat: " + (pesertaKegiatan.getNomorSertifikat() != null ? pesertaKegiatan.getNomorSertifikat() : "Belum Terbit"));
            Label tanggalTerbit = new Label("Tanggal Terbit: " + (pesertaKegiatan.getTglSertifikat() != null ? pesertaKegiatan.getTglSertifikat().toString() : "Belum Terbit"));

            box.getChildren().addAll(nama, noSertifikat, tanggalTerbit);
            box.setOpacity(0);
            box.setTranslateY(-20);

            sertifikatContainer.getChildren().add(box);

            box.setOnMouseEntered(event -> {
                ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), box);
                scaleUp.setToX(1.05);
                scaleUp.setToY(1.05);
                scaleUp.play();
                box.setCursor(Cursor.HAND);
            });

            box.setOnMouseExited(event -> {
                ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), box);
                scaleDown.setToX(1.0);
                scaleDown.setToY(1.0);
                scaleDown.play();
            });


            box.setOnMouseClicked(event -> {
                if(pesertaKegiatan.getNomorSertifikat() == null || pesertaKegiatan.getTglSertifikat() == null) {
                    try {
                        AlertNotification.showError("Sertifikat belum terbit.");
                        return;
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    ClubSession.getInstance().setPesertaKegiatan(pesertaKegiatan);
                    PageSession.getInstance().setOriginPage("lihatSertifikat");
                    switchScenes("anggota/detailSertifikat.fxml", "Detail Sertifikat");
                }catch (Exception e) {
                    try {
                        AlertNotification.showError(e.getMessage());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            // Animasi Fade + Slide
            FadeTransition fade = new FadeTransition(duration, box);
            fade.setFromValue(0);
            fade.setToValue(1);

            TranslateTransition slide = new TranslateTransition(duration, box);
            slide.setFromY(-20);
            slide.setToY(0);

            ParallelTransition animation = new ParallelTransition(fade, slide);
            animation.setDelay(delayBetween.multiply(i));
            animation.play();
        }
    }

    @FXML
    private void goToDashboard() throws Exception {
        switchScenes("anggota/dashboard.fxml", "Kelola Kegiatan");
    }

    @FXML
    public void goToDaftarClubYangDiikuti() throws IOException {
        switchScenes("anggota/daftarClubYangDiikuti.fxml", "Club Yang Diikuti");
    }

    @FXML
    public void goToDaftarClub() throws IOException {
        switchScenes("anggota/daftarClub.fxml", "Daftar Club");
    }

    @FXML
    public void goToDaftarKegiatan() throws IOException {
        switchScenes("anggota/daftarKegiatan.fxml", "Daftar Kegiatan");
    }

    @FXML
    public void goToDaftarKegiatanDiikuti () throws IOException {
        switchScenes("anggota/daftarKegiatanDiikuti.fxml", "Kegiatan Yang Diikuti");
    }

    @FXML
    public void goToLihatSertifikat() throws IOException {
        switchScenes("anggota/lihatSertifikat.fxml", "Lihat Sertifikat");
    }
}
