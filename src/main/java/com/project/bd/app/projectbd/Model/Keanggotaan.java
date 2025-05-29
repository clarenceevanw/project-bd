package com.project.bd.app.projectbd.Model;

import java.time.LocalDate;
import java.util.UUID;

public class Keanggotaan {
    private UUID id_keanggotaan;
    private Mahasiswa mahasiswa;
    private Club club;
    private String peran;
    private String status;
    private LocalDate tanggal_bergabung;

    public Keanggotaan() {}
    public Keanggotaan(UUID id_keanggotaan, Mahasiswa mahasiswa, Club club, String peran, String status, LocalDate tanggal_bergabung) {
        this.id_keanggotaan = id_keanggotaan;
        this.mahasiswa = mahasiswa;
        this.club = club;
        this.peran = peran;
        this.status = status;
        this.tanggal_bergabung = tanggal_bergabung;
    }

    public UUID getId_keanggotaan() {
        return id_keanggotaan;
    }

    public void setId_keanggotaan(UUID id_keanggotaan) {
        this.id_keanggotaan = id_keanggotaan;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
