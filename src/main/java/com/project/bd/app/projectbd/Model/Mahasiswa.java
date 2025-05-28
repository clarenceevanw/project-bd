package com.project.bd.app.projectbd.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Mahasiswa {
    private UUID idMahasiswa;
    private UUID idProdi;
    private UUID idProgram;
    private String nrp;
    private String nama;
    private String email;
    private LocalDate tglLahir;
    private List<Keanggotaan> keanggotaan;

    public Mahasiswa() {}

    public Mahasiswa(UUID idMahasiswa, UUID idProdi, UUID idProgram, String nrp, String nama, String email, LocalDate tglLahir) {
        this.idMahasiswa = idMahasiswa;
        this.idProdi = idProdi;
        this.idProgram = idProgram;
        this.nrp = nrp;
        this.nama = nama;
        this.email = email;
        this.tglLahir = tglLahir;
    }

    public UUID getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(UUID idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public UUID getIdProdi() {
        return idProdi;
    }

    public void setIdProdi(UUID idProdi) {
        this.idProdi = idProdi;
    }

    public UUID getIdProgram() {
        return idProgram;
    }

    public void setIdProgram(UUID idProgram) {
        this.idProgram = idProgram;
    }

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(LocalDate tglLahir) {
        this.tglLahir = tglLahir;
    }

    public List<Keanggotaan> getKeanggotaan() {
        return keanggotaan;
    }

    public void setKeanggotaan(List<Keanggotaan> keanggotaan) throws Exception {
        this.keanggotaan = keanggotaan;
    }
}
