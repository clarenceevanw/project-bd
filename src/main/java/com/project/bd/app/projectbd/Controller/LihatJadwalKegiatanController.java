package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Model.PesertaKegiatan;
import com.project.bd.app.projectbd.Model.PresensiKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.Session.LoginSession;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LihatJadwalKegiatanController extends BaseController{
    @FXML
    private VBox jadwalContainer;


    public void initialize() throws Exception {
        Mahasiswa mhs = mhsDAO.findById(LoginSession.getInstance().getIdMahasiswa());
        PesertaKegiatan pesertaKegiatan = pesertaKegiatanDAO.findByMahasiswaAndKegiatan(mhs, ClubSession.getInstance().getKegiatan());
        List<PresensiKegiatan> listPresensi = presensiKegiatanDAO.findByPesertaKegiatan(pesertaKegiatan);
        showListJadwal(listPresensi);
    }

    private void showListJadwal(List<PresensiKegiatan> presensiKegiatans) {
        if(presensiKegiatans.isEmpty()) {
            Label label = new Label("Belum ada jadwal kegiatan");
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #888;");
            jadwalContainer.setAlignment(Pos.CENTER);
            jadwalContainer.getChildren().add(label);
            return;
        }

        Duration delayBetween = Duration.millis(150);
        Duration duration = Duration.millis(500);

        for (int i = 0; i < presensiKegiatans.size(); i++) {
            PresensiKegiatan presensiKegiatan = presensiKegiatans.get(i);

            VBox box = new VBox();
            box.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                    + "-fx-padding: 10 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 3);");
            box.setSpacing(4);

            LocalDate tanggalJadwal = presensiKegiatan.getJadwalKegiatan().getWaktuKegiatan().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String formattedDate = tanggalJadwal.format(formatter);
            DayOfWeek hari = tanggalJadwal.getDayOfWeek();
            Label tanggal = new Label(hari + ", " + formattedDate);
            tanggal.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            Label jam = new Label("Jam: " + presensiKegiatan.getJadwalKegiatan().getWaktuKegiatan().toLocalTime().toString());
            Label lokasi = new Label("Lokasi: " + presensiKegiatan.getJadwalKegiatan().getLokasiKegiatan());

            Label labelStatus = new Label("Status: ");
            labelStatus.setStyle("-fx-text-fill: #333; -fx-font-weight: normal;");

            String status = presensiKegiatan.getStatusPresensi();
            Label labelNilaiStatus = new Label(status);
            labelNilaiStatus.setStyle("-fx-font-weight: bold;");

            switch (status) {
                case "Hadir":
                    labelNilaiStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    break;
                case "Tidak Hadir":
                    labelNilaiStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    break;
                case "Izin":
                    labelNilaiStatus.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                    break;
                default:
                    labelNilaiStatus.setStyle("-fx-text-fill: gray; -fx-font-weight: bold;");
                    break;
            }

            HBox presensiBox = new HBox(labelStatus, labelNilaiStatus);
            presensiBox.setSpacing(4);

            box.getChildren().addAll(tanggal, jam, lokasi, presensiBox);
            box.setOpacity(0);
            box.setTranslateY(-20);

            jadwalContainer.getChildren().add(box);

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
    public void goBack() throws IOException {
        switchScenes("anggota/detailDaftarKegiatan.fxml", "Detail Kegiatan");
    }
}
