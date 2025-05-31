package com.project.bd.app.projectbd.Session;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.Model.Kegiatan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClubSession {
    private static ClubSession instance;
    private Club club; //Untuk menyimpan club yang dipilih oleh user
    private Keanggotaan keanggotaan;
    private Kegiatan kegiatan;

    public static ClubSession getInstance() {
        if (instance == null) {
            instance = new ClubSession();
        }
        return instance;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Keanggotaan getKeanggotaan() {
        return keanggotaan;
    }

    public void setKeanggotaan(Keanggotaan keanggotaan) {
        this.keanggotaan = keanggotaan;
    }

    public Kegiatan getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(Kegiatan kegiatan) {
        this.kegiatan = kegiatan;
    }
}
