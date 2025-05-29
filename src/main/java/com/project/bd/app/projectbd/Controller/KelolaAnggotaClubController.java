package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.UUID;

public class KelolaAnggotaClubController extends BaseController{
    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnKelolaClub;

    @FXML
    private Button btnKelolaKegiatan;

    @FXML private TableView<Keanggotaan> anggotaTable;
    @FXML private TableColumn<Keanggotaan, String> colNrp;
    @FXML private TableColumn<Keanggotaan, String> colNama;
    @FXML private TableColumn<Keanggotaan, String> colEmail;
    @FXML private TableColumn<Keanggotaan, String> colProdi;
    @FXML private TableColumn<Keanggotaan, String> colPeran;
    @FXML private TableColumn<Keanggotaan, String> colStatus;

    private ObservableList<Keanggotaan> anggotaList = FXCollections.observableArrayList();


    @FXML
    public void initialize() throws Exception {
        setActiveSidebarButton("kelola", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        colNrp.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMahasiswa().getNrp()));
        colNama.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMahasiswa().getNama()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMahasiswa().getEmail()));
        colProdi.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMahasiswa().getProdi().getNama()));
        colPeran.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPeran()));
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        loadData();
    }

    public void loadData() throws Exception {
        UUID idClub = ClubSession.getInstance().getClub().getId_club();
        anggotaList = FXCollections.observableArrayList(keanggotaanDAO.findKeanggotaanByClub(idClub));
        anggotaTable.setItems(anggotaList);
    }

    @FXML
    private void navigateToDashboard() throws Exception {
        try{
            switchScenes("pengurus/dashboard.fxml", "Dashboard");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    private void navigateToKelolaClub() throws Exception {
        try{
            switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    private void navigateToKelolaKegiatan() throws Exception {
        try{
            switchScenes("pengurus/kelola-kegiatan.fxml", "Kelola Kegiatan");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void handleEditAnggota() throws Exception {
        Keanggotaan keanggotaan = anggotaTable.getSelectionModel().getSelectedItem();
        ClubSession.getInstance().setKeanggotaan(keanggotaan);
        try{
            switchScenes("pengurus/edit-anggota-club.fxml", "Edit Anggota Club");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void handleDeleteAnggota() throws Exception {
        Keanggotaan keanggotaan = anggotaTable.getSelectionModel().getSelectedItem();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus anggota " + keanggotaan.getMahasiswa().getNrp() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES){
            keanggotaanDAO.delete(keanggotaan.getId_keanggotaan());
            loadData();
        }
    }
}
