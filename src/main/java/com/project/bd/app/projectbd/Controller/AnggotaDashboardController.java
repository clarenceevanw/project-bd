package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.fxml.FXML;

import java.io.IOException;

public class AnggotaDashboardController extends BaseController {
    @FXML
    public void handleLogout() throws Exception {
        LoginSession.getInstance().clearSession();
        switchScenes("login.fxml", "Login");
        AlertNotification.showSuccess("Logout berhasil!");
    }

    @FXML
    public void goToBiodata() throws IOException {

    }
}
