package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PengurusDashboardController extends BaseController implements Initializable {
    @FXML
    private VBox contentBox;

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        List<Keanggotaan> anggota = keanggotaanDAO.findKeanggotaanByMahasiswa(LoginSession.getInstance().getIdMahasiswa());
        List<Club> clubPengurus = new ArrayList<>();

        System.out.println(LoginSession.getInstance().getIdMahasiswa());
        System.out.println(anggota);

        for (Keanggotaan keanggotaan : anggota) {
            Club club = new Club();
            try {
                club = clubDAO.findById(keanggotaan.getId_club());
            } catch (Exception e) {
                try {
                    AlertNotification.showError(e.getMessage());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (club != null) {
                clubPengurus.add(club);
            }
        }

        if (clubPengurus.isEmpty()) {
            Label info = new Label("Anda belum tergabung dalam club mana pun.");
            info.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");

            Button buatClub = new Button("Buat Club Baru");
            buatClub.setPrefSize(200, 50);
            buatClub.setStyle("-fx-background-color: #6C3BB9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
            buatClub.setOnAction(e -> goToBuatClub());

            contentBox.getChildren().setAll(info, buatClub);
        } else {
            ComboBox<Club> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(clubPengurus);
            comboBox.setPromptText("Pilih club yang ingin dikelola");
            comboBox.setPrefWidth(250);
            comboBox.setStyle("-fx-background-color: white; -fx-border-color: #6C3BB9; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 12px;");

            Label pilihLabel = new Label("Club Anda:");
            pilihLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

            Button kelolaButton = new Button("Kelola Club");
            kelolaButton.setPrefSize(200, 40);
            kelolaButton.setStyle("-fx-background-color: #6C3BB9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
            kelolaButton.setOnAction(e -> {
                Club selectedClub = comboBox.getValue();
                if (selectedClub != null) {
                    LoginSession.getInstance().setIdClub(selectedClub.getId_club()); // simpan club yg dipilih
                    try {
                        goToKelolaClub();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    try {
                        AlertNotification.showError("Silakan pilih club terlebih dahulu.");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            Button kelolaJadwal = new Button("Kelola Jadwal Kegiatan");
            kelolaJadwal.setPrefSize(200, 40);
            kelolaJadwal.setStyle("-fx-background-color: #6C3BB9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
            kelolaJadwal.setOnAction(e -> {
                try {
                    goToKelolaJadwal();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            contentBox.getChildren().setAll(pilihLabel, comboBox, kelolaButton, kelolaJadwal);
        }

        Button logout = new Button("Logout");
        logout.setPrefSize(200, 40);
        logout.setStyle("-fx-background-color: #E1D6F5; -fx-text-fill: #6C3BB9; -fx-font-weight: bold; -fx-background-radius: 10;");
        logout.setOnAction(e -> {
            try {
                handleLogout();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        contentBox.getChildren().add(logout);
    }

    public void goToBuatClub() {
        try {
            switchScenes("pengurus/buat-club.fxml", "Buat Club");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() throws Exception {
        LoginSession.getInstance().clearSession();
        switchScenes("login.fxml", "Login");
        AlertNotification.showSuccess("Logout berhasil!");
    }

    public void goToKelolaClub() throws IOException {
        switchScenes("pengurus/crud-club.fxml", "Kelola Club");
    }

    public void goToKelolaJadwal() throws IOException {
        switchScenes("pengurus/crud-jadwal-kegiatan.fxml", "Kelola Jadwal Kegiatan");
    }
}