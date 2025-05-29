package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;

public class KelolaClubController extends BaseController {
    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnKelolaClub;

    @FXML
    private Button btnKelolaKegiatan;

    @FXML
    private TableView<Club> clubTable;

    @FXML
    private TableColumn<Club, String> colNama;

    @FXML
    private TableColumn<Club, String> colDeskripsi;

    @FXML
    private TableColumn<Club, Integer> colTahun;

    @FXML
    private TableColumn<Club, String> colKategori;

    private final ObservableList<Club> clubList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setActiveSidebarButton("kelola", btnDashboard, btnKelolaClub, btnKelolaKegiatan);

        // Inisialisasi kolom
        colNama.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNama()));
        colDeskripsi.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDeskripsi()));
        colTahun.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTahun_berdiri()));
        colKategori.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKategori().getNama())); // nanti bisa diganti nama kategori
        colDeskripsi.setCellFactory(tc -> {
            TableCell<Club, String> cell = new TableCell<>() {
                private final Text text = new Text();

                {
                    text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10)); // kurangi padding
                    setGraphic(text);
                    setPrefHeight(Control.USE_COMPUTED_SIZE);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText("");
                    } else {
                        text.setText(item);
                    }
                }
            };
            return cell;
        });

        clubTable.setFixedCellSize(-1);
        loadData();
    }

    public void loadData(){
        clubList.clear();
        List<Club> clubs = new ArrayList<>();
        try{
            List<Keanggotaan> keanggotaanMhs = mhsDAO.findKeanggotaan(LoginSession.getInstance().getIdMahasiswa());

            for (Keanggotaan keanggotaan : keanggotaanMhs) {
                if (keanggotaan.getPeran().equals("Pengurus")) {
                    clubs.add(keanggotaan.getClub());
                }
            }
        }catch (Exception e) {
            try {
                AlertNotification.showError(e.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        clubList.addAll(clubs);
        clubTable.setItems(clubList);
    }

    @FXML
    public void handleAddClub() throws Exception {
        try{
            switchScenes("pengurus/buat-club.fxml", "Buat Club");
        }catch(Exception e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void handleEditClub() throws Exception {
        Club selected = clubTable.getSelectionModel().getSelectedItem();
        try{
            if (selected != null) {
                ClubSession.getInstance().setClub(selected);
                switchScenes("pengurus/edit-club.fxml", "Edit Club");
            } else {
                AlertNotification.showError("Pilih club terlebih dahulu.");
            }
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void handleDeleteClub() throws Exception {
        Club selected = clubTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            clubDAO.delete(selected.getId_club());
            clubList.remove(selected);
            AlertNotification.showSuccess("Hapus Club, Club berhasil dihapus.");
            switchScenes("pengurus/dashboard.fxml", "Dashboard");
        } else {
            AlertNotification.showError("Pilih club yang ingin dihapus.");
        }
    }

    @FXML
    public void handleViewAnggota() throws Exception {
        Club selected = clubTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // TODO: buka view anggota club
            ClubSession.getInstance().setClub(selected);
            switchScenes("pengurus/kelola-anggota-club.fxml", "Kelola Anggota Club");
            AlertNotification.showSuccess("Melihat anggota dari club: " + selected.getNama());
        } else {
            AlertNotification.showError("Pilih club terlebih dahulu.");
        }
    }

    @FXML
    public void handleViewKegiatan() throws Exception {
        Club selected = clubTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClubSession.getInstance().setClub(selected);
            switchScenes("pengurus/kegiatan.fxml", "Kelola Kegiatan Club");
            AlertNotification.showSuccess("Melihat kegiatan dari club: " + selected.getNama());
        } else {
            AlertNotification.showError("Pilih club terlebih dahulu.");
        }
    }

    @FXML
    public void navigateToDashboard() throws Exception {
        switchScenes("pengurus/dashboard.fxml", "Dashboard");
    }

    @FXML
    private void navigateToKelolaKegiatan() throws Exception {
        try{
            switchScenes("pengurus/kelola-kegiatan.fxml", "Kelola Kegiatan");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

}
