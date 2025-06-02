package com.project.bd.app.projectbd.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class JadwalKegiatan {
    private UUID idJadwalKegiatan;
    private LocalDateTime waktuKegiatan;
    private String lokasiKegiatan;
    private Kegiatan kegiatan;

    public JadwalKegiatan() {}

    public JadwalKegiatan(UUID idJadwalKegiatan, LocalDateTime waktuKegiatan, String lokasiKegiatan, Kegiatan kegiatan) {
        this.idJadwalKegiatan = idJadwalKegiatan;
        this.waktuKegiatan = waktuKegiatan;
        this.lokasiKegiatan = lokasiKegiatan;
        this.kegiatan = kegiatan;
    }

    public UUID getIdJadwalKegiatan() {
        return idJadwalKegiatan;
    }

    public void setIdJadwalKegiatan(UUID idJadwalKegiatan) {
        this.idJadwalKegiatan = idJadwalKegiatan;
    }

    public LocalDateTime getWaktuKegiatan() {
        return waktuKegiatan;
    }

    public void setWaktuKegiatan(LocalDateTime waktuKegiatan) {
        this.waktuKegiatan = waktuKegiatan;
    }

    public String getLokasiKegiatan() {
        return lokasiKegiatan;
    }

    public void setLokasiKegiatan(String lokasiKegiatan) {
        this.lokasiKegiatan = lokasiKegiatan;
    }

    public Kegiatan getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(Kegiatan kegiatan) {
        this.kegiatan = kegiatan;
    }
}
