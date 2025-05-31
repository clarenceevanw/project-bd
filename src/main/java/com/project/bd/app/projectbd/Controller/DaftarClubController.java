package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class DaftarClubController extends BaseController {
    @FXML
    private VBox sidebarDaftarClub;

    @FXML
    private HBox clubContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void initialize() {
        // Posisi awal di luar layar (geser ke kiri)
        sidebarDaftarClub.setTranslateX(-300);
        scrollPane.setPrefViewportHeight(250);
        scrollPane.setPrefViewportWidth(600);
        clubContainer.setFillHeight(false);

        try {
            loadClubCards();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Animasi slide in dari kiri
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebarDaftarClub);
        slideIn.setToX(0);
        slideIn.play();

        // Hapus listener animasi scroll
        // (Bagian ini dihapus seperti yang diminta)

        scrollPane.setFitToWidth(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefViewportWidth(700); // batas maksimal lebar viewport

        clubContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        clubContainer.setMaxWidth(Region.USE_PREF_SIZE);
    }

    @FXML
    private void goToDashboard() throws Exception {
        try {
            switchScenes("anggota/dashboard.fxml", "Kelola Kegiatan");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    private void loadClubCards() throws Exception {
        try {
            List<Club> clubs = clubDAO.findAll(); // Ambil dari DB

            for (int i = 0; i < clubs.size(); i++) {
                Club club = clubs.get(i);

                VBox card = createClubCard(club);
                card.setOpacity(0); // Untuk animasi fade

                clubContainer.getChildren().add(card);

                // Fade-in animation saat load
                FadeTransition fade = new FadeTransition(Duration.millis(500), card);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setDelay(Duration.millis(i * 150)); // Delay tiap box
                fade.play();
            }
        } catch (Exception e) {
            AlertNotification.showError("Gagal memuat club: " + e.getMessage());
        }
    }

    private VBox createClubCard(Club club) {
        VBox card = new VBox(10);
        card.setPrefSize(200, 200); // Ukuran persegi
        card.setMinSize(200, 200);
        card.setMaxSize(200, 200);
        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-scale-x: 1.02; -fx-scale-y: 1.02;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-scale-x: 1.02; -fx-scale-y: 1.02;", "")));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 3);");

        Label namaLabel = new Label(club.getNama());
        namaLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label kategoriLabel = new Label("Kategori");
        kategoriLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        card.getChildren().addAll(namaLabel, kategoriLabel);
        card.setAlignment(javafx.geometry.Pos.CENTER);

        return card;
    }

    @FXML
    public void goToDaftarClubYangDiikuti() throws IOException {
        switchScenes("anggota/daftarClubYangDiikuti.fxml", "Club Yang Diikuti");
    }
}