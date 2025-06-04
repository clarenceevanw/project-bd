package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
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
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class DaftarKegiatanController extends BaseController{
    @FXML
    private VBox sideBarDaftarKegiatan;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox kegiatanContainer;

    public void initialize() {
        sideBarDaftarKegiatan.setTranslateX(-300);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        setActiveSideBarButton("daftarKegiatan", btnDashboard, btnDaftarClub, btnClubDiikuti, btnDaftarKegiatan, btnKegiatanDiikuti, btnSertifikat);
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sideBarDaftarKegiatan);
        slideIn.setToX(0);
        slideIn.play();
        try {
            loadData();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() throws Exception {
        List<Kegiatan> listKegiatan = kegiatanDAO.findAll();
        if(listKegiatan.isEmpty()) kegiatanContainer.getChildren().add(new Text("Belum ada kegiatan"));
        renderKegiatanCard(listKegiatan);
    }

    private void renderKegiatanCard(List<Kegiatan> listKegiatan) {
        Duration delayBetween = Duration.millis(150);
        Duration duration = Duration.millis(500);

        for (int i = 0; i < listKegiatan.size(); i++) {
            Kegiatan kegiatan = listKegiatan.get(i);
            if(!kegiatan.isPublish()) continue;

            VBox box = new VBox();
            box.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                    + "-fx-padding: 10 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 3);");
            box.setSpacing(4);

            Label nama = new Label(kegiatan.getNama());
            nama.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label club = new Label("Penyelenggara: Club " + kegiatan.getClub().getNama());
            Label jenis = new Label("Jenis: " + kegiatan.getJenisKegiatan());
            Label kategori = new Label(kegiatan.getKategori());

            box.getChildren().addAll(nama, club,jenis, kategori);
            box.setOpacity(0);
            box.setTranslateY(-20);

            kegiatanContainer.getChildren().add(box);

//            Popup popup = new Popup();
//            VBox popupContent = new VBox();
//            popupContent.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1;");
//            popupContent.getChildren().add(new Text("Detail kegiatan: " + kegiatan.getNama()));
//            popup.getContent().add(popupContent);

            box.setOnMouseEntered(event -> {
                ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), box);
                scaleUp.setToX(1.05);
                scaleUp.setToY(1.05);
                scaleUp.play();

//                popup.show(box, event.getScreenX() + 10, event.getScreenY() + 10);
                box.setCursor(Cursor.HAND);
            });

            box.setOnMouseExited(event -> {
                ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), box);
                scaleDown.setToX(1.0);
                scaleDown.setToY(1.0);
                scaleDown.play();

//                popup.hide();
            });


            box.setOnMouseClicked(event -> {
                try {
                    ClubSession.getInstance().setKegiatan(kegiatan);
                    PageSession.getInstance().setOriginPage("daftarKegiatan");
                    switchScenes("anggota/detailDaftarKegiatan.fxml", "Detail Kegiatan");
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
}
