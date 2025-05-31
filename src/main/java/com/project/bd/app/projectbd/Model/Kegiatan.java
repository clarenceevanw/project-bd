package com.project.bd.app.projectbd.Model;

import java.util.UUID;

public class Kegiatan {
    private UUID idKegiatan;
    private String nama;
    private String linkDokumentasi;
    private Club club;
    private JenisKegiatan jenisKegiatan;
    private String kategori;

    public Kegiatan() {}

    public Kegiatan(UUID idKegiatan, String nama, String linkDokumentasi, Club club, JenisKegiatan jenisKegiatan, String kategori) {
        this.idKegiatan = idKegiatan;
        this.nama = nama;
        this.linkDokumentasi = linkDokumentasi;
        this.club = club;
        this.jenisKegiatan = jenisKegiatan;
        this.kategori = kategori;
    }

    public UUID getIdKegiatan() {
        return idKegiatan;
    }

    public void setIdKegiatan(UUID idKegiatan) {
        this.idKegiatan = idKegiatan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLinkDokumentasi() {
        return linkDokumentasi;
    }

    public void setLinkDokumentasi(String linkDokumentasi) {
        this.linkDokumentasi = linkDokumentasi;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public JenisKegiatan getJenisKegiatan() {
        return jenisKegiatan;
    }

    public void setJenisKegiatan(JenisKegiatan jenisKegiatan) {
        this.jenisKegiatan = jenisKegiatan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
