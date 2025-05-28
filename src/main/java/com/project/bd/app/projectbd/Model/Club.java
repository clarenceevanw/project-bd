package com.project.bd.app.projectbd.Model;

import java.util.List;
import java.util.UUID;

public class Club {
    private UUID id_club;
    private String nama;
    private String deskripsi;
    private int tahun_berdiri;
    private List<String> kategori;
    private List<Keanggotaan> anggota;

    public Club() {
    }

    public Club(UUID id_club, String nama, String deskripsi, int tahun_berdiri){
        this.id_club = id_club;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.tahun_berdiri = tahun_berdiri;
    }

    public UUID getId_club() {
        return id_club;
    }

    public void setId_club(UUID id_club) {
        this.id_club = id_club;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getTahun_berdiri() {
        return tahun_berdiri;
    }

    public void setTahun_berdiri(int tahun_berdiri) {
        this.tahun_berdiri = tahun_berdiri;
    }

    public List<String> getKategori() {
        return kategori;
    }

    public void setKategori(List<String> kategori) {
        this.kategori = kategori;
    }

    public List<Keanggotaan> getAnggota() {
        return anggota;
    }

    public void setAnggota(List<Keanggotaan> anggota) {
        this.anggota = anggota;
    }
}
