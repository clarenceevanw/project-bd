package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class AnggotaDashboardController extends BaseController {
    @FXML
    private VBox sidebarDashboardAnggota;

    @FXML
    private Button btnBiodata, btnLogout;

    @FXML
    public void initialize() {
        // Posisi awal di luar layar (geser ke kiri)
        sidebarDashboardAnggota.setTranslateX(-300);

        // Animasi slide in dari kiri
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebarDashboardAnggota);
        slideIn.setToX(0);
        slideIn.play();

        animateNodesSequentially(List.of(btnBiodata, btnDaftarClub, btnClubDiikuti, btnDaftarKegiatan, btnKegiatanDiikuti, btnSertifikat, btnLogout));
    }

    private void animateNodesSequentially(List<Node> nodes) {
        int delayMultiplier = 0;
        for (javafx.scene.Node node : nodes) {
            node.setOpacity(0);          // awalnya tak terlihat
            node.setTranslateY(-50);     // posisi awal di atas

            TranslateTransition slide = new TranslateTransition(Duration.millis(500), node);
            slide.setToY(0);

            FadeTransition fade = new FadeTransition(Duration.millis(500), node);
            fade.setToValue(1.0);

            ParallelTransition parallel = new ParallelTransition(node, slide, fade);
            parallel.setDelay(Duration.millis(150 * delayMultiplier)); // delay antar elemen
            parallel.play();

            delayMultiplier++;
        }
    }

    @FXML
    public void handleLogout() throws Exception {
        LoginSession.getInstance().clearSession();
        switchScenes("login.fxml", "Login");
        AlertNotification.showSuccess("Logout berhasil!");
    }

    @FXML
    public void goToBiodata() throws IOException {
        switchScenes("anggota/biodata.fxml", "Biodata Anggota");
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
    public void goToDaftarClubYangDiikuti() throws IOException {
        switchScenes("anggota/daftarClubYangDiikuti.fxml", "Club Yang Diikuti");
    }

    @FXML
    public void goToDaftarKegiatanDiikuti () throws IOException {
        switchScenes("anggota/daftarKegiatanDiikuti.fxml", "Kegiatan Yang Diikuti");
    }
}
