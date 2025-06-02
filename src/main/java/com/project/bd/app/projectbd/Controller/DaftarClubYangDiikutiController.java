package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.KeanggotaanDAO;
import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DaftarClubYangDiikutiController extends BaseController {

    @FXML
    private VBox sidebarDaftarClub;

    @FXML
    private VBox clubContainer;

    @FXML
    private ScrollPane scrollPane;

    private final KeanggotaanDAO keanggotaanDAO = new KeanggotaanDAO();

    @FXML
    public void initialize() throws Exception {
        sidebarDaftarClub.setTranslateX(-300);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        try {
            loadClubsUserIkuti();
        } catch (Exception e) {
            AlertNotification.showError(e.getMessage());
        }

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebarDaftarClub);
        slideIn.setToX(0);
        slideIn.play();

        clubContainer.setAlignment(Pos.TOP_CENTER);
        clubContainer.setMaxWidth(Region.USE_PREF_SIZE);
    }

    private void loadClubsUserIkuti() throws Exception {
        UUID idMahasiswa = LoginSession.getInstance().getIdMahasiswa();
        List<Keanggotaan> keanggotaanList = keanggotaanDAO.findKeanggotaanByMahasiswa(idMahasiswa);

        for (int i = 0; i < keanggotaanList.size(); i++) {
            Club club = keanggotaanList.get(i).getClub();
            VBox card = createClubCard(club);
            card.setOpacity(0);
            clubContainer.getChildren().add(card);

            FadeTransition fade = new FadeTransition(Duration.millis(500), card);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(i * 150));
            fade.play();
        }
    }

    private VBox createClubCard(Club club) {
        VBox card = new VBox(10);
        card.setPrefSize(200, 200);
        card.setMinSize(200, 200);
        card.setMaxSize(200, 200);
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 3);");

        Label namaLabel = new Label(club.getNama());
        namaLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label kategoriLabel = new Label(club.getKategori().getNama());
        kategoriLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        card.getChildren().addAll(namaLabel, kategoriLabel);
        card.setAlignment(Pos.CENTER);

        card.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/bd/app/projectbd/anggota/detailDaftarClub.fxml"));
                Parent detailRoot = loader.load();

                DetailDaftarClubController controller = loader.getController();
                controller.setClubDetail(club);
                controller.setStage(this.stage);
                controller.setFromDaftarYangDiikuti(true);
                controller.setModeDetail(true);
                stage.getScene().setRoot(detailRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return card;
    }

    @FXML
    public void goToDashboard() throws IOException {
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
}
