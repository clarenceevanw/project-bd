package com.project.bd.app.projectbd.Model;

import java.util.UUID;

public class JenisKegiatan {
    private UUID idJenisKegiatan;
    private String nama;

    public JenisKegiatan() {}
    public JenisKegiatan(UUID idJenisKegiatan, String nama) {
        this.idJenisKegiatan = idJenisKegiatan;
        this.nama = nama;
    }

    public UUID getIdJenisKegiatan() {
        return idJenisKegiatan;
    }

    public void setIdJenisKegiatan(UUID idJenisKegiatan) {
        this.idJenisKegiatan = idJenisKegiatan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public String toString() {
        return this.nama;
    }
}
