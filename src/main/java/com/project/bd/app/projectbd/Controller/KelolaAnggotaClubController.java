package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Keanggotaan;
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
import java.util.UUID;

public class KelolaAnggotaClubController extends BaseController{
    @FXML private VBox sidebar;
    @FXML private HBox buttonContainer;
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
        anggotaTable.setItems(anggotaList);

        // === Animasi Sidebar (Smooth Slide In) ===
        sidebar.setTranslateX(-300);
        TranslateTransition slideInSidebar = new TranslateTransition(Duration.millis(1000), sidebar);
        slideInSidebar.setToX(0);
        slideInSidebar.setInterpolator(Interpolator.EASE_OUT);
        slideInSidebar.play();

        // === TableView Fade In ===
        anggotaTable.setOpacity(0);
        FadeTransition fadeInTable = new FadeTransition(Duration.millis(600), anggotaTable);
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
        UUID idClub = ClubSession.getInstance().getClub().getId_club();
        anggotaList.clear();
        anggotaList.addAll(keanggotaanDAO.findKeanggotaanByClub(idClub));
        anggotaTable.refresh();
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
    public void handleKembali() throws Exception {
        try{
            switchScenes("pengurus/kelola-club.fxml", "Kelola Club");
        } catch (IOException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    @FXML
    public void handleEditAnggota() throws Exception {
        Keanggotaan keanggotaan = anggotaTable.getSelectionModel().getSelectedItem();
        if(keanggotaan == null){
            AlertNotification.showError("Pilih anggota terlebih dahulu.");
            return;
        }
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
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus anggota " + keanggotaan.getMahasiswa().getNrp() + " ?", ButtonType.YES, ButtonType.CANCEL);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES){
            keanggotaanDAO.delete(keanggotaan.getId_keanggotaan());
            AlertNotification.showSuccess("Hapus Anggota, Anggota berhasil dihapus.");
            loadData();
        }
    }
}
