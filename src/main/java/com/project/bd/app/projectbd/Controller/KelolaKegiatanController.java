package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KelolaKegiatanController extends BaseController {
    //Contoller untuk agar user memilih kegiatan club mana
    @FXML
    private VBox sidebar;

    @FXML
    private VBox mainContent;

    @FXML
    ComboBox<Club> comboClub;

    ObservableList<Club> clubs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        List<Club> clubPengurus = new ArrayList<>();
        try{
            List<Keanggotaan> keanggotaanMhs = mhsDAO.findKeanggotaan(LoginSession.getInstance().getIdMahasiswa());

            for (Keanggotaan keanggotaan : keanggotaanMhs) {
                if (keanggotaan.getPeran().equals("Pengurus")) {
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
        clubs.addAll(clubPengurus);
        comboClub.setItems(clubs);

        sidebar.setTranslateX(-300);
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebar);
        slideIn.setToX(0);
        slideIn.setInterpolator(Interpolator.EASE_BOTH);
        slideIn.play();

        FadeTransition fade = new FadeTransition(Duration.millis(1000), mainContent);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(Interpolator.EASE_BOTH);
        fade.play();
    }

    @FXML
    public void handlePilihClub() throws Exception {
        if(comboClub.getValue() == null) {
            AlertNotification.showError("Silahkan pilih club terlebih dahulu.");
            return;
        }
        ClubSession.getInstance().setClub(comboClub.getValue());
        try {
            switchScenes("pengurus/kegiatan.fxml", "Kegiatan");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void navigateToDashboard() throws IOException {
        switchScenes("pengurus/dashboard.fxml", "Dashboard");
    }

    @FXML
    public void navigateToKelolaClub() throws IOException {
        switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
    }
}
