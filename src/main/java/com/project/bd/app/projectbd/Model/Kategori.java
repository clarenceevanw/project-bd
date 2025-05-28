package com.project.bd.app.projectbd.Model;

import java.util.UUID;

public class Kategori {
    private UUID id_kategori;
    private String nama;

    public Kategori() {}

    public Kategori(UUID id_kategori, String nama) {
        this.id_kategori = id_kategori;
        this.nama = nama;
    }

    public UUID getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(UUID id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
