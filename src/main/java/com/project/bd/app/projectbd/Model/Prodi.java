package com.project.bd.app.projectbd.Model;

import java.util.UUID;

public class Prodi {
    private UUID id_prodi;
    private String nama;

    public Prodi(){}
    public Prodi(UUID id_prodi, String nama) {
        this.id_prodi = id_prodi;
        this.nama = nama;
    }

    public UUID getIdProdi() {
        return id_prodi;
    }

    public void setIdProdi(UUID id_prodi) {
        this.id_prodi = id_prodi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
