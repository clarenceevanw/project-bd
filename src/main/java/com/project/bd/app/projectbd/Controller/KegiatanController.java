package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class KegiatanController extends BaseController{
    //Controller untuk per Kegiatan
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnKelolaClub;
    @FXML
    private Button btnKelolaKegiatan;

    @FXML
    TableView<Kegiatan> kegiatanTable = new TableView<>();

    @FXML
    TableColumn<Kegiatan, String> colNamaKegiatan = new TableColumn<>();

    @FXML
    TableColumn<Kegiatan, String> colClub = new TableColumn<>();

    @FXML
    TableColumn<Kegiatan, String> colJenis = new TableColumn<>();

    @FXML
    TableColumn<Kegiatan, String> colKategori = new TableColumn<>();

    @FXML
    TableColumn<Kegiatan, String> colDokumentasi = new TableColumn<>();

    ObservableList<Kegiatan> kegiatanList = FXCollections.observableArrayList();

    public void initialize() {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        colNamaKegiatan.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNama()));
        colClub.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClub().getNama()));
        colJenis.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getJenisKegiatan().getNama()));
        colKategori.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKategori()));
        colDokumentasi.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLinkDokumentasi()));
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        kegiatanTable.setItems(kegiatanList);
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
            // TODO: peserta-kegiatan
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
}
