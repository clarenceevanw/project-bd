package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.Model.PesertaKegiatan;
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

public class PesertaKegiatanController extends BaseController {

    private Kegiatan kegiatan = ClubSession.getInstance().getKegiatan();

    @FXML
    private VBox sidebar;

    @FXML
    TableView<PesertaKegiatan> pesertaTable = new TableView<>();

    @FXML
    TableColumn<PesertaKegiatan, String> colNrp = new TableColumn<>();

    @FXML
    TableColumn<PesertaKegiatan, String> colNama = new TableColumn<>();

    @FXML
    TableColumn<PesertaKegiatan, String> colEmail = new TableColumn<>();

    @FXML
    TableColumn<PesertaKegiatan, String> colProdi = new TableColumn<>();

    @FXML
    TableColumn<PesertaKegiatan, String> colStatus = new TableColumn<>();

    @FXML
    TableColumn<PesertaKegiatan, String> colNomor = new TableColumn<>();

    @FXML
    TableColumn<PesertaKegiatan, String> colTanggal = new TableColumn<>();

    @FXML
    private HBox actionBox;

    ObservableList<PesertaKegiatan> pesertaList = FXCollections.observableArrayList();

    public void initialize() throws Exception {
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        Kegiatan kegiatan = ClubSession.getInstance().getKegiatan();
        if(kegiatan.getKategori().equals("Eksternal")) {

            Button btnGenerate = new Button("Generate Sertifikat");
            btnGenerate.setStyle("-fx-background-color: #6C3BB9; -fx-text-fill: white;");
            btnGenerate.setOnAction(event -> {
                try {
                    handleGenerateSertifikat();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            actionBox.getChildren().add(btnGenerate);
            // === Animasi Sidebar (Smooth Slide In) ===
            sidebar.setTranslateX(-300);
            TranslateTransition slideInSidebar = new TranslateTransition(Duration.millis(1000), sidebar);
            slideInSidebar.setToX(0);
            slideInSidebar.setInterpolator(Interpolator.EASE_OUT);
            slideInSidebar.play();

            // === TableView Fade In ===
            pesertaTable.setOpacity(0);
            FadeTransition fadeInTable = new FadeTransition(Duration.millis(600), pesertaTable);
            fadeInTable.setFromValue(0);
            fadeInTable.setToValue(1);
            fadeInTable.setInterpolator(Interpolator.EASE_BOTH);

            // === ButtonContainer Slide Up with Fade ===
            actionBox.setOpacity(0);
            actionBox.setTranslateY(20);

            FadeTransition fadeButtons = new FadeTransition(Duration.millis(600), actionBox);
            fadeButtons.setFromValue(0);
            fadeButtons.setToValue(1);

            TranslateTransition slideUpButtons = new TranslateTransition(Duration.millis(600), actionBox);
            slideUpButtons.setFromY(20);
            slideUpButtons.setToY(0);
            slideUpButtons.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition buttonTransition = new ParallelTransition(fadeButtons, slideUpButtons);

            // === Urutan Animasi: Sidebar → Table → Buttons ===
            SequentialTransition sequence = new SequentialTransition(fadeInTable, buttonTransition);
            sequence.play();
        }

        colNrp.setCellValueFactory(cellData -> {
            PesertaKegiatan peserta = cellData.getValue();
            if (peserta != null && peserta.getMahasiswa() != null && peserta.getMahasiswa().getNrp() != null) {
                return new SimpleStringProperty(peserta.getMahasiswa().getNrp());
            }
            return new SimpleStringProperty("");
        });

        colNama.setCellValueFactory(cellData -> {
            PesertaKegiatan peserta = cellData.getValue();
            if (peserta != null && peserta.getMahasiswa() != null && peserta.getMahasiswa().getNama() != null) {
                return new SimpleStringProperty(peserta.getMahasiswa().getNama());
            }
            return new SimpleStringProperty("");
        });

        colEmail.setCellValueFactory(cellData -> {
            PesertaKegiatan peserta = cellData.getValue();
            if (peserta != null && peserta.getMahasiswa() != null && peserta.getMahasiswa().getEmail() != null) {
                return new SimpleStringProperty(peserta.getMahasiswa().getEmail());
            }
            return new SimpleStringProperty("");
        });

        colProdi.setCellValueFactory(cellData -> {
            PesertaKegiatan peserta = cellData.getValue();
            if (peserta != null && peserta.getMahasiswa() != null && peserta.getMahasiswa().getProdi() != null && peserta.getMahasiswa().getProdi().getNama() != null) {
                return new SimpleStringProperty(peserta.getMahasiswa().getProdi().getNama());
            }
            return new SimpleStringProperty("");
        });

        colStatus.setCellValueFactory(cellData -> {
            PesertaKegiatan peserta = cellData.getValue();
            if (peserta != null && peserta.getStatusSertifikat() != null) {
                return new SimpleStringProperty(peserta.getStatusSertifikat());
            }
            return new SimpleStringProperty("Tidak");
        });

        colNomor.setCellValueFactory(cellData -> {
            PesertaKegiatan peserta = cellData.getValue();
            if (peserta != null && peserta.getNomorSertifikat() != null) {
                return new SimpleStringProperty(peserta.getNomorSertifikat());
            }

            if(peserta != null && peserta.getStatusSertifikat().equals("Ada")){
                return new SimpleStringProperty("Belum di set");
            }

            return new SimpleStringProperty("Tidak ada");
        });

        colTanggal.setCellValueFactory(cellData -> {
            PesertaKegiatan peserta = cellData.getValue();
            if (peserta != null && peserta.getTglSertifikat() != null) {
                return new SimpleStringProperty(peserta.getTglSertifikat().toString());
            }
            if(peserta != null && peserta.getStatusSertifikat().equals("Ada")){
                return new SimpleStringProperty("Belum di set");
            }
            return new SimpleStringProperty("Tidak ada");
        });

        try {
            loadData();
        } catch (Exception e) {
            AlertNotification.showError("Gagal memuat data peserta: " + e.getMessage());
        }

        pesertaTable.setItems(pesertaList);
    }

    public void loadData() throws Exception {
        if (kegiatan == null) {
            pesertaList.clear(); // Kosongkan list jika tidak ada kegiatan
            return;
        }

        pesertaList.setAll(pesertaKegiatanDAO.findByKegiatan(kegiatan));
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
    public void handleBack() throws Exception{
        switchScenes("pengurus/kegiatan.fxml", "Kelola Kegiatan");
    }

    @FXML
    public void handleTambahPeserta() throws Exception{
        switchScenes("pengurus/tambah-peserta-kegiatan.fxml", "Tambah Peserta Kegiatan");
    }

    @FXML
    public void handleDeletePeserta() throws Exception{
        PesertaKegiatan selected = pesertaTable.getSelectionModel().getSelectedItem();
        if(selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apakah anda yakin ingin menghapus peserta " + selected.getMahasiswa().getNrp() + " ?", ButtonType.YES, ButtonType.CANCEL);
            confirm.showAndWait();
            if (confirm.getResult() == ButtonType.YES) {
                pesertaKegiatanDAO.delete(selected.getIdPesertaKegiatan());
                pesertaList.remove(selected);
                AlertNotification.showSuccess("Hapus Peserta, Peserta berhasil dihapus.");
                loadData();
            }
        } else {
            AlertNotification.showError("Pilih peserta terlebih dahulu.");
        }
    }

    @FXML
    public void handleLihatPresensi() throws Exception {
        PesertaKegiatan selected = pesertaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ClubSession.getInstance().setPesertaKegiatan(selected);
            switchScenes("pengurus/presensi-kegiatan.fxml", "Presensi Kegiatan");
        } else {
            AlertNotification.showError("Pilih peserta terlebih dahulu.");
        }
    }

    @FXML
    public void handleGenerateSertifikat() throws Exception{
        PesertaKegiatan pesertaKegiatan = pesertaTable.getSelectionModel().getSelectedItem();
        if(pesertaKegiatan == null) {
            AlertNotification.showError("Pilih peserta terlebih dahulu.");
            return;
        }

        if (pesertaKegiatan.getNomorSertifikat() != null) {
            AlertNotification.showError("Sertifikat sudah di generate.");
            return;
        }

        pesertaKegiatan.setStatusSertifikat("Ada");
        pesertaKegiatan.setNomorSertifikat("SERT/"+ LocalDate.now().getYear() + "/"+ pesertaKegiatan.getKegiatan().getIdKegiatan().toString().substring(pesertaKegiatan.getKegiatan().getIdKegiatan().toString().length() - 3)+ "/" + pesertaKegiatan.getKegiatan().getNama().substring(0, 3).toUpperCase() + "/" + pesertaKegiatan.getMahasiswa().getNrp().toUpperCase() + "/"+ pesertaKegiatan.getIdPesertaKegiatan().toString().substring(pesertaKegiatan.getIdPesertaKegiatan().toString().length() - 3));
        pesertaKegiatan.setTglSertifikat(LocalDate.now());
        pesertaKegiatanDAO.update(pesertaKegiatan);
        AlertNotification.showSuccess("Sertifikat berhasil di generate.");
        loadData();
    }
}
