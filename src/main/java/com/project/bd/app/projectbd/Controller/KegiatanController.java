package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class KegiatanController extends BaseController{
    //Controller untuk per Kegiatan
    @FXML
    private VBox sidebar;

    @FXML
    private HBox buttonContainer;

    @FXML
    private HBox publishContainer;

    @FXML
    TableView<Kegiatan> kegiatanTable;

    @FXML
    TableColumn<Kegiatan, String> colNamaKegiatan;

    @FXML
    TableColumn<Kegiatan, String> colClub;

    @FXML
    TableColumn<Kegiatan, String> colJenis;

    @FXML
    TableColumn<Kegiatan, String> colKategori;

    @FXML
    TableColumn<Kegiatan, String> colDokumentasi;

    @FXML
    TableColumn<Kegiatan, String> colTanggalMulai;

    @FXML
    TableColumn<Kegiatan, String> colTanggalSelesai;

    @FXML
    TableColumn<Kegiatan, String> colPublish;

    ObservableList<Kegiatan> kegiatanList = FXCollections.observableArrayList();

    public void initialize() {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        colNamaKegiatan.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNama()));
        colClub.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClub().getNama()));
        colJenis.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getJenisKegiatan().getNama()));
        colKategori.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKategori()));
        colDokumentasi.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLinkDokumentasi()));
        colTanggalMulai.setCellValueFactory(cellData -> {
            if(cellData.getValue().getTanggalMulai() == null) {
                return new javafx.beans.property.SimpleStringProperty("Belum di atur");
            }
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTanggalMulai().toString());
        });
        colTanggalSelesai.setCellValueFactory(cellData -> {
            if(cellData.getValue().getTanggalSelesai() == null) {
                return new javafx.beans.property.SimpleStringProperty("Belum diatur");
            }
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTanggalSelesai().toString());
        });
        colPublish.setCellValueFactory(cellData -> {
            if (cellData.getValue().isPublish()) {
                return new javafx.beans.property.SimpleStringProperty("Sudah");
            } else {
                return new javafx.beans.property.SimpleStringProperty("Belum");
            }
    });
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        kegiatanTable.setItems(kegiatanList);
        // === Animasi Sidebar (Smooth Slide In) ===
        sidebar.setTranslateX(-300);
        TranslateTransition slideInSidebar = new TranslateTransition(Duration.millis(1000), sidebar);
        slideInSidebar.setToX(0);
        slideInSidebar.setInterpolator(Interpolator.EASE_OUT);
        slideInSidebar.play();

        // === TableView Fade In ===
        kegiatanTable.setOpacity(0);
        FadeTransition fadeInTable = new FadeTransition(Duration.millis(600), kegiatanTable);
        fadeInTable.setFromValue(0);
        fadeInTable.setToValue(1);
        fadeInTable.setInterpolator(Interpolator.EASE_BOTH);

        // === ButtonContainer Slide Up with Fade ===
        buttonContainer.setOpacity(0);
        buttonContainer.setTranslateY(20);

        FadeTransition fadeButtons = new FadeTransition(Duration.millis(600), buttonContainer);
        fadeButtons.setFromValue(0);
        fadeButtons.setToValue(1);

        FadeTransition fadePublish = new FadeTransition(Duration.millis(600), publishContainer);
        fadePublish.setFromValue(0);
        fadePublish.setToValue(1);
        fadePublish.setInterpolator(Interpolator.EASE_BOTH);
        fadePublish.play();

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
        kegiatanList.clear();
        kegiatanList.addAll(kegiatanDAO.findByClub(ClubSession.getInstance().getClub().getId_club()));
        kegiatanTable.refresh();
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
    public void handleTambahKegiatan() throws IOException {
        switchScenes("pengurus/tambah-kegiatan.fxml", "Tambah Kegiatan");
    }

    @FXML
    public void handleEditKegiatan() throws Exception {
        Kegiatan selected = kegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClubSession.getInstance().setKegiatan(selected);
            switchScenes("pengurus/edit-kegiatan.fxml", "Edit Kegiatan");
        } else {
            AlertNotification.showError("Pilih kegiatan terlebih dahulu.");
        }
    }

    @FXML
    public void handleHapusKegiatan() throws Exception {
        Kegiatan selected = kegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus kegiatan " + selected.getNama() + " ?", ButtonType.YES, ButtonType.CANCEL);
            confirm.showAndWait();
            if (confirm.getResult() == ButtonType.YES) {
                kegiatanDAO.delete(selected.getIdKegiatan());
                kegiatanList.remove(selected);
                AlertNotification.showSuccess("Hapus Kegiatan, Kegiatan berhasil dihapus.");
                loadData();
            }
        } else {
            AlertNotification.showError("Pilih kegiatan yang ingin dihapus.");
        }
    }

    @FXML
    public void handleLihatPeserta() throws Exception {
        Kegiatan selected = kegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClubSession.getInstance().setKegiatan(selected);
            switchScenes("pengurus/peserta-kegiatan.fxml", "Peserta Kegiatan");
        } else {
            AlertNotification.showError("Pilih kegiatan terlebih dahulu.");
        }
    }

    @FXML
    public void handleLihatJadwal() throws Exception {
        Kegiatan selected = kegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClubSession.getInstance().setKegiatan(selected);
            switchScenes("pengurus/jadwal-kegiatan.fxml", "Jadwal Kegiatan");
        } else {
            AlertNotification.showError("Pilih kegiatan terlebih dahulu.");
        }
    }

    @FXML
    public void handlePublish() throws Exception {
        Kegiatan selected = kegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (selected.getTanggalMulai() == null || selected.getTanggalSelesai() == null) {
                AlertNotification.showError("Tanggal mulai dan selesai harus diisi.");
            } else if (selected.isPublish()){
                AlertNotification.showError("Kegiatan sudah dipublish.");
            } else {
                selected.setPublish(true);
                kegiatanDAO.updatePublish(selected);
                AlertNotification.showSuccess("Kegiatan berhasil dipublish.");
                loadData();
            }
        }else {
            AlertNotification.showError("Pilih kegiatan terlebih dahulu.");
        }
    }

    @FXML
    public void handleUnpublish() throws Exception {
        Kegiatan selected = kegiatanTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (!selected.isPublish()){
                AlertNotification.showError("Kegiatan sudah unpublish.");
                return;
            }
            selected.setPublish(false);
            kegiatanDAO.updatePublish(selected);
            AlertNotification.showSuccess("Kegiatan berhasil diunpublish.");
            loadData();
        }else {
            AlertNotification.showError("Pilih kegiatan terlebih dahulu.");
        }
    }
}
