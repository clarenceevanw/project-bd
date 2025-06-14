package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.KeanggotaanDAO;
import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class LihatAnggotaClubController extends BaseController {

    @FXML private VBox anggotaContainer;
    @FXML private Button btnBack; // Pastikan di FXML fx:id="btnBack" onAction="#goToDetailDaftarClub"

    private Stage stage;

    // ----- ORIGIN PAGE dari mana detail berasal -----
    // Nilai akan diteruskan dari DetailDaftarClubController
    private String originPage;              // "daftarClub" atau "daftarClubYangDiikuti"
    private boolean fromDaftarYangDiikuti;  // true jika berasal dari daftar yang diikuti

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOriginPage(String originPage) {
        this.originPage = originPage;
    }

    public void setFromDaftarYangDiikuti(boolean fromDaftarYangDiikuti) {
        this.fromDaftarYangDiikuti = fromDaftarYangDiikuti;
    }

    public void initialize() throws Exception {
        Club club = ClubSession.getInstance().getClub();
        if (club != null) {
            List<Keanggotaan> daftarAnggota = new KeanggotaanDAO()
                    .findKeanggotaanByClub(club.getId_club());
            tampilkanDaftarAnggota(daftarAnggota);
        } else {
            AlertNotification.showError("Data club tidak ditemukan");
        }
    }

    private void tampilkanDaftarAnggota(List<Keanggotaan> anggotaList) {
        Duration delayBetween = Duration.millis(150);
        Duration duration = Duration.millis(500);

        for (int i = 0; i < anggotaList.size(); i++) {
            Keanggotaan keanggotaan = anggotaList.get(i);

            VBox box = new VBox();
            box.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                    + "-fx-padding: 10 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 3);");
            box.setSpacing(4);

            Label nama = new Label(keanggotaan.getMahasiswa().getNama());
            nama.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label peran = new Label("Peran: " + keanggotaan.getPeran());
            Label tanggal = new Label("Bergabung: " + keanggotaan.getTanggal_bergabung().toString());
            Label status = new Label("Status: " + keanggotaan.getStatus());

            box.getChildren().addAll(nama, peran, tanggal, status);
            box.setOpacity(0);
            box.setTranslateY(-20);

            anggotaContainer.getChildren().add(box);

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

    /**
     * Button “Back” mengembalikan ke DetailDaftarClub sesuai originPage
     */
    @FXML
    private void goToDetailDaftarClub(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/project/bd/app/projectbd/anggota/detailDaftarClub.fxml")
        );
        Parent root = loader.load();

        // Ambil controller Detail
        DetailDaftarClubController controller = loader.getController();
        // Inject stage
        controller.setStage(this.stage);
        // Teruskan originPage dan flag fromDaftarYangDiikuti
        controller.setOriginPage(this.originPage);
        controller.setFromDaftarYangDiikuti(this.fromDaftarYangDiikuti);
        // Kirim ulang Club-nya
        controller.setClubDetail(ClubSession.getInstance().getClub());

        // Ganti scene
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}
