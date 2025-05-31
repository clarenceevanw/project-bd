package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class DaftarClubYangDiikutiController extends BaseController{
    @FXML
    private VBox sidebarDaftarClubYangDiikuti;

    @FXML
    public void initialize() {
        // Posisi awal di luar layar (geser ke kiri)
        sidebarDaftarClubYangDiikuti.setTranslateX(-300);

        // Animasi slide in dari kiri
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebarDaftarClubYangDiikuti);
        slideIn.setToX(0);
        slideIn.play();
    }

    @FXML
    private void goToDashboard() throws Exception {
        try {
            switchScenes("anggota/dashboard.fxml", "Kelola Kegiatan");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void goToDaftarClubYangDiikuti() throws IOException {
        switchScenes("anggota/daftarClubYangDiikuti.fxml", "Club Yang Diikuti");
    }
}
