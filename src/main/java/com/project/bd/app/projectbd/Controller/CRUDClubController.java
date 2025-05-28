package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.*;

public class CRUDClubController extends BaseController {
    @FXML
    private TableView<Club> clubTable;
    @FXML private TableColumn<Club, String> colNama;
    @FXML private TableColumn<Club, String> colKategori;
    @FXML private TableColumn<Club, String> colPembina;

    @FXML private TextField fieldNama;
    @FXML private TextField fieldKategori;
    @FXML private TextField fieldPembina;

    @FXML private void handleBackToDashboard() {  }
    @FXML private void handleTambah() {  }
    @FXML private void handleEdit() {  }
    @FXML private void handleHapus() {  }
    @FXML private void handleSimpan() {  }

}
