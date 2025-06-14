package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Kategori;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DaftarClubController extends BaseController {
    @FXML private VBox sidebarDaftarClub;
    @FXML private VBox clubContainer;
    @FXML private ScrollPane scrollPane;
    @FXML private ComboBox<Kategori> comboFilter;

    // 1. Tambahkan variabel untuk menyimpan semua klub dari database
    private List<Club> semuaClub;

    @FXML
    public void initialize() throws Exception {
        setActiveSideBarButton("daftarClub", btnDashboard, btnDaftarClub, btnClubDiikuti, btnDaftarKegiatan, btnKegiatanDiikuti, btnSertifikat);
        sidebarDaftarClub.setTranslateX(-300);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // 2. Ambil SEMUA data klub SEKALI saja dan simpan
        try {
            this.semuaClub = clubDAO.findAll();
            if (this.semuaClub == null) { // Jaga-jaga jika DAO mengembalikan null
                this.semuaClub = new ArrayList<>();
            }
        } catch (Exception e) {
            this.semuaClub = new ArrayList<>();
            // Tampilkan notifikasi error ke pengguna
            AlertNotification.showError("Gagal memuat data klub dari database.");
            throw new RuntimeException(e);
        }

        // 3. Setup ComboBox dengan opsi "Semua Kategori" (sama seperti saran sebelumnya)
        setupComboBoxFilter();

        // 4. Tambahkan listener untuk memfilter saat pilihan berubah
        comboFilter.setOnAction(event -> filterAndDisplayClubs());

        // 5. Tampilkan semua klub pada awalnya
        displayClubCards(this.semuaClub);

        // Animasi sidebar
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), sidebarDaftarClub);
        slideIn.setToX(0);
        slideIn.play();

        clubContainer.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        clubContainer.setMaxWidth(Region.USE_PREF_SIZE);
    }

    private void setupComboBoxFilter() throws Exception {
        Kategori semuaKategori = new Kategori();
        semuaKategori.setId_kategori(null); // ID null sebagai penanda "Semua"
        semuaKategori.setNama("Semua Kategori");

        comboFilter.getItems().add(semuaKategori);
        comboFilter.getItems().addAll(kategoriDAO.findAll());
        comboFilter.setValue(semuaKategori); // Jadikan "Semua" sebagai default
    }

    // 6. Buat fungsi baru untuk LOGIKA FILTERING
    private void filterAndDisplayClubs() {
        Kategori selectedKategori = comboFilter.getValue();
        List<Club> filteredClubs;

        if (selectedKategori == null || selectedKategori.getId_kategori() == null) {
            filteredClubs = this.semuaClub;
        } else {
            filteredClubs = this.semuaClub.stream()
                    .filter(club -> club.getKategori() != null && selectedKategori.getId_kategori().equals(club.getKategori().getId_kategori()))
                    .collect(Collectors.toList());
        }
        displayClubCards(filteredClubs);
    }

    // 7. Ubah `loadClubCards` menjadi `displayClubCards` yang tugasnya hanya menampilkan
    private void displayClubCards(List<Club> clubsToDisplay) {
        clubContainer.getChildren().clear(); // Wajib! Hapus kartu lama sebelum menampilkan yg baru

        if (clubsToDisplay.isEmpty()) {
            Label noDataLabel = new Label("Tidak ada klub yang sesuai.");
            noDataLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #888;");
            clubContainer.getChildren().add(noDataLabel);
            return;
        }

        for (int i = 0; i < clubsToDisplay.size(); i++) {
            Club club = clubsToDisplay.get(i);
            VBox card = createClubCard(club); // Method createClubCard Anda tetap sama
            card.setOpacity(0);

            clubContainer.getChildren().add(card);
            FadeTransition fade = new FadeTransition(Duration.millis(500), card);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(i * 100L)); // Kurangi sedikit delay agar lebih responsif
            fade.play();
        }
    }

    // Method createClubCard dan method navigasi lainnya tetap sama...
    private VBox createClubCard(Club club) {
        // ... (Tidak ada perubahan di sini)
        VBox card = new VBox(10);
        card.setPrefSize(200, 200);
        card.setMinSize(200, 200);
        card.setMaxSize(200, 200);
        card.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.03);
            scale.setToY(1.03);
            scale.play();
            card.setCursor(Cursor.HAND);
        });
        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });
        card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 3);"
        );

        Label namaLabel = new Label(club.getNama());
        namaLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label kategoriLabel = new Label(club.getKategori().getNama());
        kategoriLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        card.getChildren().addAll(namaLabel, kategoriLabel);
        card.setAlignment(javafx.geometry.Pos.CENTER);

        card.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/project/bd/app/projectbd/anggota/detailDaftarClub.fxml")
                );
                Parent detailRoot = loader.load();

                DetailDaftarClubController controller = loader.getController();
                controller.setStage(this.stage);
                controller.setFromDaftarYangDiikuti(false);
                controller.setOriginPage("daftarClub");
                controller.setClubDetail(club);

                stage.getScene().setRoot(detailRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return card;
    }

    // ... method navigasi lainnya ...
    @FXML
    public void goToDashboard() throws Exception {
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

    @FXML
    public void goToLihatSertifikat() throws IOException {
        switchScenes("anggota/lihatSertifikat.fxml", "Lihat Sertifikat");
    }
}