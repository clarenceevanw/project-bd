package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.JadwalKegiatan;
import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JadwalKegiatanController extends BaseController {
    @FXML
    private VBox sidebar;

    @FXML
    private HBox buttonContainer;

    @FXML
    private TableView<JadwalKegiatan> jadwalKegiatanTable = new TableView<>();

    @FXML
    private TableColumn<JadwalKegiatan, String> colTanggal = new TableColumn<>();

    @FXML
    private TableColumn<JadwalKegiatan, String> colJam = new TableColumn<>();

    @FXML
    private TableColumn<JadwalKegiatan, String> colLokasi = new TableColumn<>();

    ObservableList<JadwalKegiatan> jadwalKegiatanList = FXCollections.observableArrayList();

    public void initialize() throws Exception {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);

        colTanggal.setCellValueFactory(cellData -> {
            JadwalKegiatan jadwal = cellData.getValue();
            if (jadwal != null && jadwal.getWaktuKegiatan() != null) {
                LocalDateTime dt = jadwal.getWaktuKegiatan();
                LocalDate date = dt.toLocalDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                String formattedDate = date.format(formatter);
                return new SimpleStringProperty(formattedDate);
            }
            return new SimpleStringProperty("");
        });

        colJam.setCellValueFactory(cellData -> {
            JadwalKegiatan jadwal = cellData.getValue();
            if (jadwal != null && jadwal.getWaktuKegiatan() != null) {
                LocalDateTime dt = jadwal.getWaktuKegiatan();
                return new SimpleStringProperty(dt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            return new SimpleStringProperty("");
        });

        colLokasi.setCellValueFactory(cellData -> {
            JadwalKegiatan jadwal = cellData.getValue();
            if (jadwal != null && jadwal.getLokasiKegiatan() != null) {
                return new SimpleStringProperty(jadwal.getLokasiKegiatan());
            }
            return new SimpleStringProperty(jadwal != null && jadwal.getLokasiKegiatan() == null ? "Lokasi N/A" : ""); // Beri tanda jika lokasi memang null
        });

        try {
            loadData();
        } catch (Exception e) {
            AlertNotification.showError("Gagal memuat data awal: " + e.getMessage());
        }
        jadwalKegiatanTable.setItems(jadwalKegiatanList);

        // === Animasi Sidebar (Smooth Slide In) ===
        sidebar.setTranslateX(-300);
        TranslateTransition slideInSidebar = new TranslateTransition(Duration.millis(1000), sidebar);
        slideInSidebar.setToX(0);
        slideInSidebar.setInterpolator(Interpolator.EASE_OUT);
        slideInSidebar.play();

        // === TableView Fade In ===
        jadwalKegiatanTable.setOpacity(0);
        FadeTransition fadeInTable = new FadeTransition(Duration.millis(600), jadwalKegiatanTable);
        fadeInTable.setFromValue(0);
        fadeInTable.setToValue(1);
        fadeInTable.setInterpolator(Interpolator.EASE_BOTH);

        // === ButtonContainer Slide Up with Fade ===
        buttonContainer.setOpacity(0);
        buttonContainer.setTranslateY(20);

        FadeTransition fadeButtons = new FadeTransition(Duration.millis(600), buttonContainer);
        fadeButtons.setFromValue(0);
        fadeButtons.setToValue(1);

        TranslateTransition slideUpButtons = new TranslateTransition(Duration.millis(600), buttonContainer);
        slideUpButtons.setFromY(20);
        slideUpButtons.setToY(0);
        slideUpButtons.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition buttonTransition = new ParallelTransition(fadeButtons, slideUpButtons);

        // === Urutan Animasi: Sidebar → Table → Buttons ===
        SequentialTransition sequence = new SequentialTransition(fadeInTable, buttonTransition);
        sequence.play();
    }

    public void loadData() throws Exception {
        Kegiatan kegiatan = ClubSession.getInstance().getKegiatan();
        jadwalKegiatanList.clear();
        try {
            List<JadwalKegiatan> dataDariDB = jadwalKegiatanDAO.findByKegiatan(kegiatan);
            jadwalKegiatanList.addAll(dataDariDB);
        } catch (Exception e) {
            AlertNotification.showError("Gagal memuat jadwal kegiatan: " + e.getMessage());
        }
        jadwalKegiatanTable.refresh();
}

    @FXML
    public void handleBack() throws Exception {
        switchScenes("pengurus/kegiatan.fxml", "Kelola Kegiatan");
    }

    @FXML
    public void navigateToDashboard() throws Exception {
        switchScenes("pengurus/dashboard.fxml", "Dashboard");
    }

    @FXML
    public void navigateToKelolaClub() throws Exception {
        switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
    }

    @FXML
    public void navigateToKelolaKegiatan() throws Exception {
        switchScenes("pengurus/kelola-kegiatan.fxml", "Kelola Kegiatan");
    }

    @FXML
    public void handleTambahJadwal() throws Exception {
        ClubSession.getInstance().setKegiatan(ClubSession.getInstance().getKegiatan());
        switchScenes("pengurus/tambah-jadwal-kegiatan.fxml", "Tambah Jadwal Kegiatan");
    }

    @FXML
    public void handleEditJadwal() throws Exception {
        JadwalKegiatan selected = jadwalKegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClubSession.getInstance().setJadwalKegiatan(selected);
            switchScenes("pengurus/edit-jadwal-kegiatan.fxml", "Edit Jadwal Kegiatan");
        } else {
            AlertNotification.showError("Pilih jadwal kegiatan terlebih dahulu.");
        }
    }

    @FXML
    public void handleHapusJadwal() throws Exception {
        JadwalKegiatan selected = jadwalKegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus jadwal kegiatan " + selected.getWaktuKegiatan() + " ?", ButtonType.YES, ButtonType.CANCEL);
            confirm.showAndWait();
            if (confirm.getResult() == ButtonType.CANCEL) return;
            jadwalKegiatanDAO.delete(selected.getIdJadwalKegiatan());
            loadData();
            AlertNotification.showSuccess("Jadwal kegiatan berhasil dihapus.");
        } else {
            AlertNotification.showError("Pilih jadwal kegiatan terlebih dahulu.");
        }
    }

    @FXML
    public void handleLihatPresensi() throws Exception {
        JadwalKegiatan selected = jadwalKegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClubSession.getInstance().setJadwalKegiatan(selected);
            switchScenes("pengurus/presensi-jadwal-kegiatan.fxml", "Presensi Kegiatan");
        } else {
            AlertNotification.showError("Pilih jadwal kegiatan terlebih dahulu.");
        }
    }
}
