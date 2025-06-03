package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.KeanggotaanDAO;
import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public class DetailDaftarClubController extends BaseController {

    @FXML private Label namaClubLabel;
    @FXML private Label deskripsiLabel;
    @FXML private Label tahunBerdiriLabel;
    @FXML private Label kategoriLabel;
    @FXML private Pane kategoriBox;
    @FXML private Pane judulBox;
    @FXML private Pane deskripsiBox;
    @FXML private Pane tahunBox;
    @FXML private Button btnLihatAnggota;
    @FXML private Button btnJoin;

    private Club club;
    private final KeanggotaanDAO keanggotaanDAO = new KeanggotaanDAO();
    private boolean fromDaftarYangDiikuti = false;

    // ----- ORIGIN PAGE -----
    // Bisa "daftarClub" atau "daftarClubYangDiikuti"
    private String originPage = "daftarClub";

    public void setOriginPage(String originPage) {
        this.originPage = originPage;
    }
    // -----------------------

    public void setFromDaftarYangDiikuti(boolean fromDaftarYangDiikuti) {
        this.fromDaftarYangDiikuti = fromDaftarYangDiikuti;
    }

    public void setModeDetail(boolean sudahIkut) {
        btnJoin.setVisible(!sudahIkut);
        btnJoin.setManaged(!sudahIkut);
        btnLihatAnggota.setVisible(sudahIkut);
        btnLihatAnggota.setManaged(sudahIkut);
    }

    @FXML
    public void initialize() {
        deskripsiBox.setTranslateX(-600);
        tahunBox.setTranslateX(-600);
        kategoriBox.setTranslateX(-600);
    }

    public void setClubDetail(Club club) {
        this.club = club;
        namaClubLabel.setText(club.getNama());
        deskripsiLabel.setText(club.getDeskripsi());
        tahunBerdiriLabel.setText(Integer.toString(club.getTahun_berdiri()));
        kategoriLabel.setText(club.getKategori().getNama());

        try {
            UUID idMahasiswa = LoginSession.getInstance().getIdMahasiswa();
            boolean sudahIkut = keanggotaanDAO.isUserJoinedClub(idMahasiswa, club.getId_club());

            if (fromDaftarYangDiikuti) {
                setModeDetail(true);
            } else {
                setModeDetail(sudahIkut);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setModeDetail(false);
        }

        playAnimations();
    }

    private void playAnimations() {
        TranslateTransition slideJudul = new TranslateTransition(Duration.millis(500), judulBox);
        slideJudul.setFromY(-100);
        slideJudul.setToY(20);

        TranslateTransition slideDeskripsi = new TranslateTransition(Duration.millis(400), deskripsiBox);
        slideDeskripsi.setFromX(-600);
        slideDeskripsi.setToX(0);

        TranslateTransition slideTahun = new TranslateTransition(Duration.millis(400), tahunBox);
        slideTahun.setFromX(-600);
        slideTahun.setToX(0);
        slideTahun.setDelay(Duration.millis(100));

        TranslateTransition slideKategori = new TranslateTransition(Duration.millis(400), kategoriBox);
        slideKategori.setFromX(-600); // Tambahkan ini agar animasi konsisten
        slideKategori.setToX(0);
        slideKategori.setDelay(Duration.millis(200));

        slideJudul.play();
        slideDeskripsi.play();
        slideTahun.play();
        slideKategori.play();
    }


    /**
     * Tombol Back: kembali ke daftar sesuai originPage
     */
    @FXML
    public void goBack() {
        try {
            FXMLLoader loader;
            String title;

            if (originPage.equals("daftarClubYangDiikuti")) {
                loader = new FXMLLoader(getClass().getResource("/com/project/bd/app/projectbd/anggota/daftarClubYangDiikuti.fxml"));
                title = "Club Yang Diikuti";
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/project/bd/app/projectbd/anggota/daftarClub.fxml"));
                title = "Daftar Club";
            }

            Parent root = loader.load();
            BaseController controller = loader.getController();
            controller.setStage(stage);

            stage.getScene().setRoot(root);
            stage.setTitle(title);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLihatAnggotaCLub(ActionEvent event) throws IOException {
        // Simpan club ke session
        ClubSession.getInstance().setClub(club);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/project/bd/app/projectbd/anggota/lihatAnggotaClub.fxml")
        );
        Parent root = loader.load();

        LihatAnggotaClubController controller = loader.getController();
        // ★ INJECT stage ke controller list anggota
        controller.setStage(this.stage);
        // ★ TURUNKAN juga informasi asal halaman detail:
        //   - apakah datang dari daftarClub atau daftarClubYangDiikuti?
        controller.setOriginPage(this.originPage);
        controller.setFromDaftarYangDiikuti(this.fromDaftarYangDiikuti);
        //   (jika belum memiliki setter di LihatAnggotaClubController, tambahkan saja)
        //   di sana, nanti goBack()‐nya akan kembali ke DetailDaftarClub.

        stage.getScene().setRoot(root);
    }


    public void goToJoinClub() throws Exception {
        try {
            UUID idMahasiswa = LoginSession.getInstance().getIdMahasiswa();

            com.project.bd.app.projectbd.Model.Keanggotaan keanggotaan = new Keanggotaan();
            keanggotaan.setId_keanggotaan(UUID.randomUUID());

            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setIdMahasiswa(idMahasiswa);
            keanggotaan.setMahasiswa(mahasiswa);

            keanggotaan.setClub(club);
            keanggotaan.setPeran("Anggota");
            keanggotaan.setTanggal_bergabung(LocalDate.now());

            keanggotaanDAO.insert(keanggotaan);
            AlertNotification.showSuccess("Berhasil bergabung ke club!");
            switchScenes("anggota/daftarClub.fxml", "Daftar Club");

        } catch (Exception e) {
            AlertNotification.showError("Gagal bergabung: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
