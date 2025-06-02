package com.project.bd.app.projectbd.Model;

import java.util.UUID;

public class PresensiKegiatan {
    private UUID idPesertaJadwalKegiatan;
    private PesertaKegiatan pesertaKegiatan;
    private JadwalKegiatan jadwalKegiatan;
    private String statusPresensi;

    public PresensiKegiatan() {}
    public PresensiKegiatan(UUID idPesertaJadwalKegiatan, PesertaKegiatan pesertaKegiatan, JadwalKegiatan jadwalKegiatan, String statusPresensi) {
        this.idPesertaJadwalKegiatan = idPesertaJadwalKegiatan;
        this.pesertaKegiatan = pesertaKegiatan;
        this.jadwalKegiatan = jadwalKegiatan;
        this.statusPresensi = statusPresensi;
    }

    public UUID getIdPesertaJadwalKegiatan() {
        return idPesertaJadwalKegiatan;
    }
    public void setIdPesertaJadwalKegiatan(UUID idPesertaJadwalKegiatan) {
        this.idPesertaJadwalKegiatan = idPesertaJadwalKegiatan;
    }
    public PesertaKegiatan getPesertaKegiatan() {
        return pesertaKegiatan;
    }
    public void setPesertaKegiatan(PesertaKegiatan pesertaKegiatan) {
        this.pesertaKegiatan = pesertaKegiatan;
    }
    public JadwalKegiatan getJadwalKegiatan() {
        return jadwalKegiatan;
    }
    public void setJadwalKegiatan(JadwalKegiatan jadwalKegiatan) {
        this.jadwalKegiatan = jadwalKegiatan;
    }
    public String getStatusPresensi() {
        return statusPresensi;
    }
    public void setStatusPresensi(String statusPresensi) {
        this.statusPresensi = statusPresensi;
    }
}
