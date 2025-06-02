package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.DAO.KeanggotaanDAO;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Model.Mahasiswa;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import com.project.bd.app.projectbd.Model.Club;

import java.time.LocalDate;
import java.util.UUID;

public class DetailDaftarClubController extends BaseController {

    @FXML
    private Label namaClubLabel;

    @FXML
    private Label deskripsiLabel;

    @FXML
    private Label tahunBerdiriLabel;

    @FXML
    private Label kategoriLabel;

    @FXML
    private Pane kategoriBox;

    @FXML
    private Pane judulBox;

    @FXML
    private Pane deskripsiBox;

    @FXML
    private Pane tahunBox;

    @FXML
    private Button btnLihatAnggota;

    @FXML
    private Button btnJoin;

    private Club club;

    private final KeanggotaanDAO keanggotaanDAO = new KeanggotaanDAO();

    private boolean fromDaftarYangDiikuti = false;

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
                setModeDetail(true); // selalu "lihat anggota"
            } else {
                setModeDetail(sudahIkut); // join kalau belum
            }
        } catch (Exception e) {
            e.printStackTrace();
            setModeDetail(false);
        }

        playAnimations();
    }

    private void playAnimations() {
        // Judul: masuk dari atas ke tengah agak bawah (misal Y = 20)
        TranslateTransition slideJudul = new TranslateTransition(Duration.millis(500), judulBox);
        slideJudul.setFromY(-100); // sudah diset di initialize()
        slideJudul.setToY(20);     // supaya tidak terlalu atas (tidak terpotong)

        // Deskripsi: dari kiri masuk ke posisi normal (X = 0)
        TranslateTransition slideDeskripsi = new TranslateTransition(Duration.millis(400), deskripsiBox);
        slideDeskripsi.setFromX(-600);
        slideDeskripsi.setToX(0);

        // Tahun: dari kiri masuk ke posisi normal (X = 0)
        TranslateTransition slideTahun = new TranslateTransition(Duration.millis(400), tahunBox);
        slideTahun.setFromX(-600);
        slideTahun.setToX(0);
        slideTahun.setDelay(Duration.millis(100)); // sedikit delay agar bertahap

        TranslateTransition slideKategori = new TranslateTransition(Duration.millis(400), kategoriBox);
        slideKategori.setToX(0);
        slideKategori.setDelay(Duration.millis(200));

        // Jalankan semuanya bersamaan
        slideJudul.play();
        slideDeskripsi.play();
        slideTahun.play();
        slideKategori.play();
    }

    @FXML
    public void goBack() {
        try {
            if (fromDaftarYangDiikuti) {
                switchScenes("anggota/daftarClubYangDiikuti.fxml", "Club Yang Diikuti");
            } else {
                switchScenes("anggota/daftarClub.fxml", "Daftar Club");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToLihatAnggotaCLub() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/bd/app/projectbd/anggota/lihatAnggotaClub.fxml"));
            Parent root = loader.load();

            LihatAnggotaClubController controller = loader.getController();
            controller.setStage(this.stage);
            controller.setClub(club); // kirim club agar tahu club mana yang ditampilkan

            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            AlertNotification.showError("Gagal membuka daftar anggota: " + e.getMessage());
        }
    }


    public void goToJoinClub() throws Exception {
        try {
            UUID idMahasiswa = LoginSession.getInstance().getIdMahasiswa();

            Keanggotaan keanggotaan = new Keanggotaan();
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
