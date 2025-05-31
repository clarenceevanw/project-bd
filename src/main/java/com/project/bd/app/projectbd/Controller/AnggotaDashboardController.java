package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class AnggotaDashboardController extends BaseController {
    @FXML
    private VBox sidebarDashboardAnggota;

    @FXML
    public void initialize() {
        // Posisi awal di luar layar (geser ke kiri)
        sidebarDashboardAnggota.setTranslateX(-300);

        // Animasi slide in dari kiri
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebarDashboardAnggota);
        slideIn.setToX(0);
        slideIn.play();
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

}
