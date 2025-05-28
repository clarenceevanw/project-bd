package com.project.bd.app.projectbd.Model;

import java.time.LocalDate;
import java.util.UUID;

public class Keanggotaan {
    private UUID id_keanggotaan;
    private UUID id_mahasiswa;
    private UUID id_club;
    private String peran;
    private LocalDate tanggal_bergabung;

    public Keanggotaan() {}
    public Keanggotaan(UUID id_keanggotaan, UUID id_mahasiswa, UUID id_club, String peran, LocalDate tanggal_bergabung) {
        this.id_keanggotaan = id_keanggotaan;
        this.id_mahasiswa = id_mahasiswa;
        this.id_club = id_club;
        this.peran = peran;
        this.tanggal_bergabung = tanggal_bergabung;
    }

    public UUID getId_keanggotaan() {
        return id_keanggotaan;
    }

    public void setId_keanggotaan(UUID id_keanggotaan) {
        this.id_keanggotaan = id_keanggotaan;
    }

    public UUID getId_mahasiswa() {
        return id_mahasiswa;
    }

    public void setId_mahasiswa(UUID id_mahasiswa) {
        this.id_mahasiswa = id_mahasiswa;
    }

    public UUID getId_club() {
        return id_club;
    }

    public void setId_club(UUID id_club) {
        this.id_club = id_club;
    }

    public String getPeran() {
        return peran;
    }

    public void setPeran(String peran) {
        this.peran = peran;
    }

    public LocalDate getTanggal_bergabung() {
        return tanggal_bergabung;
    }

    public void setTanggal_bergabung(LocalDate tanggal_bergabung) {
        this.tanggal_bergabung = tanggal_bergabung;
    }
}
