package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.JadwalKegiatan;
import com.project.bd.app.projectbd.Model.PresensiKegiatan;
import com.project.bd.app.projectbd.Session.ClubSession;
import com.project.bd.app.projectbd.utils.AlertNotification;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PresensiJadwalKegiatanController extends BaseController{
    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnKelolaClub;

    @FXML
    private Button btnKelolaKegiatan;

    @FXML
    private TableView<PresensiKegiatan> presensiTable = new TableView<>();

    @FXML
    private TableColumn<PresensiKegiatan, String> colNrp = new TableColumn<>();

    @FXML
    private TableColumn<PresensiKegiatan, String> colNama = new TableColumn<>();

    @FXML
    private TableColumn<PresensiKegiatan,String> colJurusan = new TableColumn<>();

    @FXML
    private TableColumn<PresensiKegiatan,String> colKehadiran = new TableColumn<>();

    ObservableList<PresensiKegiatan> presensiList = FXCollections.observableArrayList();

    public void initialize(){
        setActiveSidebarButton("kegiatan", btnDashboard, btnKelolaClub, btnKelolaKegiatan);
        colNrp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPesertaKegiatan().getMahasiswa().getNrp()));
        colNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPesertaKegiatan().getMahasiswa().getNama()));
        colJurusan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPesertaKegiatan().getMahasiswa().getProdi().getNama()));
        colKehadiran.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusPresensi()));
        try{
            loadData();
        }catch (Exception e){
            e.printStackTrace();
        }
        presensiTable.setItems(presensiList);
    }

    public void loadData() throws Exception {
        JadwalKegiatan jadwalKegiatan = ClubSession.getInstance().getJadwalKegiatan();
        presensiList.addAll(presensiKegiatanDAO.findByJadwalKegiatan(jadwalKegiatan));
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
    public void handleBack() throws Exception {
        switchScenes("pengurus/jadwal-kegiatan.fxml", "Kelola Kegiatan");
    }

    @FXML
    public void handleEditPresensi() throws Exception {
        PresensiKegiatan presensiKegiatan = presensiTable.getSelectionModel().getSelectedItem();
        ClubSession.getInstance().setPresensiKegiatan(presensiKegiatan);
        if(presensiKegiatan == null) {
            AlertNotification.showError("Pilih presensi kegiatan terlebih dahulu.");
            return;
        }
        switchScenes("pengurus/edit-presensi-kegiatan.fxml", "Edit Presensi Kegiatan");
    }
}
