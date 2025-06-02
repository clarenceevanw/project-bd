package com.project.bd.app.projectbd.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Mahasiswa {
    private UUID idMahasiswa;
    private Prodi prodi;
    private Program program;
    private String nrp;
    private String nama;
    private String email;
    private LocalDate tglLahir;
    private List<Keanggotaan> keanggotaan;
    private Boolean active;

    public Mahasiswa() {}

    public Mahasiswa(UUID idMahasiswa, Prodi prodi, Program program, String nrp, String nama, String email, LocalDate tglLahir) {
        this.idMahasiswa = idMahasiswa;
        this.prodi = prodi;
        this.program = program;
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

    public Prodi getProdi() {
        return prodi;
    }

    public void setProdi(Prodi prodi) {
        this.prodi = prodi;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
