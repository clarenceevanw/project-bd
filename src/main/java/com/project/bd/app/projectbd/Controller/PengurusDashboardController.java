package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class PengurusDashboardController extends BaseController {
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
