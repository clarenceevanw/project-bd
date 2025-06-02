package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.PesertaKegiatan;
import com.project.bd.app.projectbd.Model.PresensiKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PresensiKegiatanController extends BaseController {
    private PesertaKegiatan peserta = ClubSession.getInstance().getPesertaKegiatan();

    @FXML
    private Label txtJudul;

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
