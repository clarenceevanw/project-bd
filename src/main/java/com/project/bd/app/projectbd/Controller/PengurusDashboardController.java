package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.Session.PageSession;
import com.project.bd.app.projectbd.utils.AlertNotification;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PengurusDashboardController extends BaseController implements Initializable {
    @FXML
    private VBox contentBox;

    @FXML
    private VBox leftPane;

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        leftPane.setTranslateX(-300);
        List<Club> clubPengurus = new ArrayList<>();
        try{
            List<Keanggotaan> keanggotaanMhs = mhsDAO.findKeanggotaan(LoginSession.getInstance().getIdMahasiswa());

            for (Keanggotaan keanggotaan : keanggotaanMhs) {
                if (keanggotaan.getPeran().equals("Pengurus") && keanggotaan.getStatus().equals("Aktif")){
                    clubPengurus.add(keanggotaan.getClub());
                }
            }
        }catch (Exception e) {
            try {
                AlertNotification.showError(e.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }


        if (clubPengurus.isEmpty()) {
            Label info = new Label("Anda belum mengelola club mana pun.");
            info.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");

            Button buatClub = new Button("Buat Club Baru");
            buatClub.setPrefSize(200, 50);
            buatClub.setStyle("-fx-background-color: #6C3BB9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
            buatClub.setOnAction(e -> {
                try {
                    PageSession.getInstance().setOriginPage("pengurusDashboard");
                    goToBuatClub();
                } catch (IOException ex) {
                    try {
                        AlertNotification.showError(ex.getMessage());
                    } catch (Exception exc) {
                        throw new RuntimeException(exc);
                    }
                }
            });
            contentBox.getChildren().setAll(info, buatClub);
            animateNodesSequentially(List.of(info, buatClub));
        } else {
            Button kelolaButton = new Button("Kelola Club");
            kelolaButton.setPrefSize(200, 40);
            kelolaButton.setStyle("-fx-background-color: #6C3BB9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
            kelolaButton.setOnAction(event -> {
                try {
                    goToKelolaClub();
                }catch (IOException e) {
                    try {
                        AlertNotification.showError(e.getMessage());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            Button kelolaJadwal = new Button("Kelola Kegiatan");
            kelolaJadwal.setPrefSize(200, 40);
            kelolaJadwal.setStyle("-fx-background-color: #6C3BB9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
            kelolaJadwal.setOnAction(e -> {
                try {
                    goToKelolaKegiatan();
                } catch (IOException ex) {
                    try {
                        AlertNotification.showError(ex.getMessage());
                    } catch (Exception exc) {
                        throw new RuntimeException(exc);
                    }
                }
            });
            contentBox.getChildren().setAll(kelolaButton, kelolaJadwal);
            animateNodesSequentially(List.of(kelolaButton, kelolaJadwal));
        }

        Button logout = new Button("Logout");
        logout.setPrefSize(200, 40);
        logout.setStyle("-fx-background-color: #E1D6F5; -fx-text-fill: #6C3BB9; -fx-font-weight: bold; -fx-background-radius: 10;");
        logout.setOnAction(e -> {
            try {
                handleLogout();
            } catch (Exception exception) {
                try {
                    AlertNotification.showError(exception.getMessage());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        contentBox.getChildren().add(logout);
        animateNodesSequentially(contentBox.getChildren().subList(0, contentBox.getChildren().size()));

        TranslateTransition slideRight = new TranslateTransition(Duration.millis(1000), leftPane);
        slideRight.setToX(0);
        slideRight.play();
    }

    // Tambahkan helper untuk animasi bertahap
    private void animateNodesSequentially(List<javafx.scene.Node> nodes) {
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



    public void goToBuatClub() throws IOException {
        switchScenes("pengurus/buat-club.fxml", "Buat Club");
    }

    @FXML
    public void handleLogout() throws Exception {
        LoginSession.getInstance().clearSession();
        switchScenes("login.fxml", "Login");
        AlertNotification.showSuccess("Logout berhasil!");
    }

    public void goToKelolaClub() throws IOException {
        switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
    }

    public void goToKelolaKegiatan() throws IOException {
        switchScenes("pengurus/kelola-kegiatan.fxml", "Kelola Kegiatan");
    }
}