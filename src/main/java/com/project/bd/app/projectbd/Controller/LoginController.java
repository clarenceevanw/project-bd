package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField signupUsername;

    @FXML
    private PasswordField signupPassword;

    @FXML
    private VBox leftPane;

    @FXML
    private StackPane formContainer;

    @FXML
    private VBox loginForm;

    @FXML
    private VBox signUpForm;


    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Pengurus", "Anggota");
        leftPane.setTranslateX(-300);
        formContainer.setTranslateX(300);

        // Geser signupForm ke kanan agar tidak terlihat saat awal
        if (signUpForm != null) {
            signUpForm.setTranslateX(400);
            signUpForm.setVisible(false); // disembunyikan saat start
        }

        TranslateTransition slideRight = new TranslateTransition(Duration.millis(1500), leftPane);
        slideRight.setToX(0);
        slideRight.play();

        TranslateTransition slideLeft = new TranslateTransition(Duration.millis(1500), formContainer);
        slideLeft.setToX(0);
        slideLeft.play();
    }

    @FXML
    public void handleLogin() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            AlertNotification.showError("Username atau Password tidak boleh kosong!");
            return;
        }

        if (selectedRole == null) {
            AlertNotification.showError("Role tidak boleh kosong!");
            return;
        }

        Mahasiswa mhs = mhsDAO.findByNrp(username);

        if (mhs == null) {
            AlertNotification.showError("Mahasiswa tidak ditemukan!");
            return;
        }

        LocalDate tglLahir = mhs.getTglLahir();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String dateString = tglLahir.format(formatter);

        if (!password.equals(dateString)) {
            AlertNotification.showError("Password salah!");
            return;
        }

        if (!mhs.getActive()) {
            AlertNotification.showError("Mahasiswa belum aktif!");
            return;
        }

//        List<Keanggotaan> keanggotaan = mhs.getKeanggotaan();
//
//        if (keanggotaan == null || keanggotaan.isEmpty()) {
//            AlertNotification.showError("Mahasiswa tidak terdaftar sebagai anggota maupun pengurus!");
//            return;
//        }
//
//        boolean found = false;
//
//        for (Keanggotaan keanggotaan1 : keanggotaan) {
//            if (keanggotaan1.getPeran().equals(selectedRole)) {
//                found = true;
//                LoginSession.getInstance().setSession(mhs.getIdMahasiswa(), mhs.getNrp(), mhs.getNama(), mhs.getEmail(), selectedRole);
//                if (selectedRole.equals("Pengurus")) {
//                    switchScenes("pengurus/dashboard.fxml", "Dashboard Pengurus");
//                } else {
//                    switchScenes("anggota/dashboard.fxml", "Dashboard Anggota");
//                }
//                AlertNotification.showSuccess("Login berhasil sebagai " + selectedRole + "!");
//                return;
//            }
//        }
//
//        if (!found) {
//            AlertNotification.showError("Mahasiswa tidak terdaftar sebagai " + selectedRole + "!");
//            return;
//           }
        LoginSession.getInstance().setSession(mhs.getIdMahasiswa(), mhs.getNrp(), mhs.getNama(), mhs.getEmail(), selectedRole);
        if(selectedRole.equals("Pengurus")) {
            switchScenes("pengurus/dashboard.fxml", "Dashboard Pengurus");
        } else if (selectedRole.equals("Anggota")) {
            switchScenes("anggota/dashboard.fxml", "Dashboard Anggota");
        }
        AlertNotification.showSuccess("Login berhasil sebagai " + selectedRole + "!");
    }

    @FXML
    public void slideToSignUp() {
        // Animasi geser login keluar kiri dan signup masuk dari kanan
        TranslateTransition loginOut = new TranslateTransition(Duration.millis(400), loginForm);
        loginOut.setToX(400);

        TranslateTransition signUpIn = new TranslateTransition(Duration.millis(400), signUpForm);
        signUpIn.setToX(0);

        loginOut.setOnFinished(event -> {
            loginForm.setVisible(false);
        });

        signUpForm.setVisible(true);

        loginOut.play();
        signUpIn.play();
    }

    @FXML
    public void slideToLogin() {
        TranslateTransition signUpOut = new TranslateTransition(Duration.millis(400), signUpForm);
        signUpOut.setToX(400);

        TranslateTransition loginIn = new TranslateTransition(Duration.millis(400), loginForm);
        loginIn.setToX(0);

        signUpOut.setOnFinished(event -> {
            signUpForm.setVisible(false);
        });

        loginForm.setVisible(true);

        signUpOut.play();
        loginIn.play();
    }


    public void handleSignUp() throws Exception {
        String username = signupUsername.getText();
        String password = signupPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertNotification.showError("Username atau Password tidak boleh kosong!");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate tglLahir = LocalDate.parse(password, formatter);
        Mahasiswa mhs = mhsDAO.findByNrp(username);

        if (mhs == null) {
            AlertNotification.showError("Mahasiswa tidak ditemukan!");
            return;
        }

        if (!mhs.getTglLahir().equals(tglLahir)) {
            AlertNotification.showSuccess("Password salah!");
            return;
        }

        if(mhs.getActive()) {
            AlertNotification.showError("Mahasiswa sudah aktif!");
            return;
        }

        mhs.setActive(true);

        mhsDAO.updateActive(mhs);
        AlertNotification.showSuccess("Mahasiswa berhasil diaktifkan!");
        switchScenes("login.fxml", "Login");
    }
}
