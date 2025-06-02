package com.project.bd.app.projectbd.Model;

import java.time.LocalDate;
import java.util.UUID;

public class PesertaKegiatan {
    private UUID idPesertaKegiatan;
    private Mahasiswa mahasiswa;
    private Kegiatan kegiatan;
    private String statusSertifikat;
    private String nomorSertifikat;
    private LocalDate tglSertifikat;

    public PesertaKegiatan() {}
    public PesertaKegiatan(UUID idPesertaKegiatan, Mahasiswa mahasiswa, Kegiatan kegiatan, String statusSertifikat, String nomorSertifikat, LocalDate tglSertifikat) {
        this.idPesertaKegiatan = idPesertaKegiatan;
        this.mahasiswa = mahasiswa;
        this.kegiatan = kegiatan;
        this.statusSertifikat = statusSertifikat;
        this.nomorSertifikat = nomorSertifikat;
        this.tglSertifikat = tglSertifikat;
    }

    public UUID getIdPesertaKegiatan() {
        return idPesertaKegiatan;
    }

    public void setIdPesertaKegiatan(UUID idPesertaKegiatan) {
        this.idPesertaKegiatan = idPesertaKegiatan;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public Kegiatan getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(Kegiatan kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getStatusSertifikat() {
        return statusSertifikat;
    }

    public void setStatusSertifikat(String statusSertifikat) {
        this.statusSertifikat = statusSertifikat;
    }

    public String getNomorSertifikat() {
        return nomorSertifikat;
    }

    public void setNomorSertifikat(String nomorSertifikat) {
        this.nomorSertifikat = nomorSertifikat;
    }

    public LocalDate getTglSertifikat() {
        return tglSertifikat;
    }

    public void setTglSertifikat(LocalDate tglSertifikat) {
        this.tglSertifikat = tglSertifikat;
    }
}
