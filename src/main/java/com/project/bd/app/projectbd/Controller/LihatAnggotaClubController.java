package com.project.bd.app.projectbd.Controller;

import com.project.bd.app.projectbd.Model.Club;
import javafx.stage.Stage;

public class LihatAnggotaClubController {

    private Club club;

    public void setClub(Club club) {
        this.club = club;
        // bisa langsung load daftar anggota saat club sudah diterima
    }

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
