package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.PesertaKegiatan;
import com.project.bd.app.projectbd.Model.PresensiKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PresensiKegiatanController extends BaseController {
    private PesertaKegiatan peserta = ClubSession.getInstance().getPesertaKegiatan();

    @FXML
    private Label txtJudul;

    @FXML
    private VBox sidebar;

    @FXML
    private TableView<PresensiKegiatan> presensiTable = new TableView<>();

    @FXML
    private TableColumn<PresensiKegiatan, String> colTanggal = new TableColumn<>();

    @FXML
    private TableColumn<PresensiKegiatan, String> colWaktu = new TableColumn<>();

    @FXML
    private TableColumn<PresensiKegiatan, String> colKehadiran = new TableColumn<>();

    @FXML
    private ObservableList<PresensiKegiatan> presensiList = FXCollections.observableArrayList();

    public void initialize() throws Exception {
        txtJudul.setText(txtJudul.getText()+ " " + peserta.getMahasiswa().getNama());
        txtJudul.setMinWidth(Region.USE_COMPUTED_SIZE);
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        colTanggal.setCellValueFactory(cellData -> {
            LocalDateTime waktu = cellData.getValue().getJadwalKegiatan().getWaktuKegiatan();
            return new SimpleStringProperty(waktu.toLocalDate().toString());
        });

        colWaktu.setCellValueFactory(cellData -> {
            LocalDateTime waktu = cellData.getValue().getJadwalKegiatan().getWaktuKegiatan();
            return new SimpleStringProperty(waktu.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        });

        colKehadiran.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusPresensi()));

        loadData();
        presensiTable.setItems(presensiList);
        sidebar.setTranslateX(-300);
        TranslateTransition slideInSidebar = new TranslateTransition(Duration.millis(1000), sidebar);
        slideInSidebar.setToX(0);
        slideInSidebar.setInterpolator(Interpolator.EASE_BOTH);
        slideInSidebar.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(600), presensiTable);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
        fadeTransition.play();
    }

    public void loadData() throws Exception {
        List<PresensiKegiatan> presensiKegiatan = presensiKegiatanDAO.findByPesertaKegiatan(peserta);
        presensiList.addAll(presensiKegiatan);
        presensiTable.refresh();
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
        switchScenes("pengurus/peserta-kegiatan.fxml", "Kelola Kegiatan");
    }
}
