package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.Session.LoginSession;
import com.project.bd.app.projectbd.Session.PageSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.animation.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KelolaClubController extends BaseController {
    @FXML
    private HBox buttonContainer;

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

    @FXML
    private VBox sidebar;

    private final ObservableList<Club> clubList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setActiveSidebarButton("kelola", btnDashboard, btnKelolaClub, btnKelolaKegiatan);

        // Inisialisasi kolom
        colNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));
        colDeskripsi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeskripsi()));
        colTahun.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTahun_berdiri()));
        colKategori.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKategori().getNama()));
        colDeskripsi.setCellFactory(tc -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                setGraphic(text);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            private void updateTextColor(boolean isSelected) {
                text.setStyle("-fx-fill: " + (isSelected ? "white" : "#7e43ba") + ";");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    text.setText("");
                } else {
                    text.setText(item);
                    updateTextColor(getTableRow() != null && getTableRow().isSelected());
                }
            }
        });

        loadData();
        clubTable.setItems(clubList);

        // === Animasi Sidebar (Smooth Slide In) ===
        sidebar.setTranslateX(-300);
        TranslateTransition slideInSidebar = new TranslateTransition(Duration.millis(1000), sidebar);
        slideInSidebar.setToX(0);
        slideInSidebar.setInterpolator(Interpolator.EASE_OUT);
        slideInSidebar.play();

        // === TableView Fade In ===
        clubTable.setOpacity(0);
        FadeTransition fadeInTable = new FadeTransition(Duration.millis(600), clubTable);
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


    public void loadData(){
        clubList.clear();
        List<Club> clubs = new ArrayList<>();
        try{
            List<Keanggotaan> keanggotaanMhs = mhsDAO.findKeanggotaan(LoginSession.getInstance().getIdMahasiswa());

            for (Keanggotaan keanggotaan : keanggotaanMhs) {
                if (keanggotaan.getPeran().equals("Pengurus") && keanggotaan.getStatus().equals("Aktif")) {
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
        clubTable.refresh();
    }

    @FXML
    public void handleAddClub() throws Exception {
        try{
            PageSession.getInstance().setOriginPage("kelolaClub");
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
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus club " + selected.getNama() + " ?", ButtonType.YES,  ButtonType.CANCEL);
            confirm.showAndWait();
            if (confirm.getResult() == ButtonType.YES) {
                clubDAO.delete(selected.getId_club());
                clubList.remove(selected);
                AlertNotification.showSuccess("Hapus Club, Club berhasil dihapus.");
                if(clubList.isEmpty()) switchScenes("pengurus/dashboard.fxml", "Dashboard");
                loadData();
            }
        } else {
            AlertNotification.showError("Pilih club yang ingin dihapus.");
        }
    }

    @FXML
    public void handleViewAnggota() throws Exception {
        Club selected = clubTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClubSession.getInstance().setClub(selected);
            switchScenes("pengurus/kelola-anggota-club.fxml", "Kelola Anggota Club");
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
