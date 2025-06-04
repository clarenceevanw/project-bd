package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.*;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PresensiDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public void insert(PresensiKegiatan presensi) throws Exception {
        // Kolom primary key untuk peserta_jadwal_kegiatan diasumsikan 'id_peserta_jadwal_kegiatan'
        String sql = "INSERT INTO peserta_jadwal_kegiatan (id_peserta, id_jadwal_kegiatan, status_kehadiran) VALUES (?, ?, ?) RETURNING id_peserta_jadwal_kegiatan;";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            // Pastikan PesertaKegiatan model memiliki getIdPeserta()
            if (presensi.getPesertaKegiatan() != null && presensi.getPesertaKegiatan().getIdPesertaKegiatan() != null) {
                stmt.setObject(1, presensi.getPesertaKegiatan().getIdPesertaKegiatan());
            } else {
                stmt.setNull(1, Types.OTHER); // Atau Types.UUID jika tipe datanya UUID
                // Handle kasus jika pesertaKegiatan atau ID-nya null, mungkin throw exception atau log
                // throw new IllegalArgumentException("Peserta Kegiatan atau ID Peserta tidak boleh null");
            }

            if (presensi.getJadwalKegiatan() != null && presensi.getJadwalKegiatan().getIdJadwalKegiatan() != null) {
                stmt.setObject(2, presensi.getJadwalKegiatan().getIdJadwalKegiatan());
            } else {
                stmt.setNull(2, Types.OTHER); // Atau Types.UUID
                // Handle kasus jika jadwalKegiatan atau ID-nya null
                // throw new IllegalArgumentException("Jadwal Kegiatan atau ID Jadwal Kegiatan tidak boleh null");
            }

            stmt.setString(3, presensi.getStatusPresensi());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                presensi.setIdPesertaJadwalKegiatan(rs.getObject("id_peserta_jadwal_kegiatan", UUID.class));
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat memasukkan data presensi: " + e.getMessage());
            throw e; // Re-throw exception
        }
    }

    public void update(PresensiKegiatan presensi) throws Exception {
        String sql = "UPDATE peserta_jadwal_kegiatan SET status_kehadiran = ? WHERE id_peserta_jadwal_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, presensi.getStatusPresensi());
            stmt.setObject(2, presensi.getIdPesertaJadwalKegiatan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat memperbarui data presensi: " + e.getMessage());
            throw e; // Re-throw exception
        }
    }

    // Helper method untuk mapping, agar tidak duplikasi kode
    private PresensiKegiatan mapRowToPresensiKegiatan(ResultSet rs) throws SQLException {
        Prodi prodi = null;
        if (rs.getObject("id_prodi") != null) {
            prodi = new Prodi();
            prodi.setIdProdi(rs.getObject("id_prodi", UUID.class));
            prodi.setNama(rs.getString("prodi_nama"));
        }

        Program program = null;
        if (rs.getObject("id_program") != null) {
            program = new Program();
            program.setIdProgram(rs.getObject("id_program", UUID.class));
            program.setNama(rs.getString("program_nama"));
            // Asumsi: Program tidak secara langsung memiliki objek Prodi di model Program itu sendiri
            // Jika Program memiliki list Prodi, mappingnya akan berbeda dan lebih kompleks di sini.
            // program.setProdi(prodi); // Ini mungkin tidak tepat jika relasi tidak langsung
        }

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setIdMahasiswa(rs.getObject("id_mahasiswa", UUID.class));
        mahasiswa.setNrp(rs.getString("nrp"));
        mahasiswa.setNama(rs.getString("nama_mahasiswa")); // Menggunakan alias
        mahasiswa.setEmail(rs.getString("email"));
        Date tglLahirDb = rs.getDate("tgl_lahir");
        if (tglLahirDb != null) {
            mahasiswa.setTglLahir(tglLahirDb.toLocalDate());
        } else {
            mahasiswa.setTglLahir(null);
        }
        mahasiswa.setProgram(program);
        mahasiswa.setProdi(prodi);

        Club club = null;
        if (rs.getObject("id_club") != null) {
            club = new Club();
            club.setId_club(rs.getObject("id_club", UUID.class));
            club.setNama(rs.getString("club_nama"));
        }

        Kegiatan kegiatan = new Kegiatan();
        kegiatan.setIdKegiatan(rs.getObject("id_kegiatan", UUID.class));
        kegiatan.setNama(rs.getString("nama_kegiatan")); // Menggunakan alias
        kegiatan.setKategori(rs.getString("kategori_kegiatan")); // Menggunakan alias
        kegiatan.setTanggalMulai(rs.getObject("tanggal_mulai", LocalDate.class));
        kegiatan.setTanggalSelesai(rs.getObject("tanggal_selesai", LocalDate.class));
        kegiatan.setPublish(rs.getBoolean("publish"));
        kegiatan.setClub(club);

        JadwalKegiatan jadwalKegiatan = new JadwalKegiatan();
        jadwalKegiatan.setIdJadwalKegiatan(rs.getObject("id_jadwal_kegiatan", UUID.class));
        Timestamp waktuKegiatanTs = rs.getTimestamp("waktu_kegiatan");
        if (waktuKegiatanTs != null) {
            jadwalKegiatan.setWaktuKegiatan(waktuKegiatanTs.toLocalDateTime());
        } else {
            jadwalKegiatan.setWaktuKegiatan(null);
        }
        jadwalKegiatan.setLokasiKegiatan(rs.getString("lokasi_kegiatan"));
        jadwalKegiatan.setKegiatan(kegiatan); // Jadwal memiliki Kegiatan

        PesertaKegiatan pesertaKegiatan = new PesertaKegiatan();
        // Menggunakan id_peserta sesuai konfirmasi PK sebelumnya
        pesertaKegiatan.setIdPesertaKegiatan(rs.getObject("id_peserta", UUID.class));
        pesertaKegiatan.setStatusSertifikat(rs.getString("status_sertifikat"));
        pesertaKegiatan.setNomorSertifikat(rs.getString("nomor_sertifikat"));
        Date tglSertifikatDb = rs.getDate("tanggal_terbit_sertifikat");
        if (tglSertifikatDb != null) {
            pesertaKegiatan.setTglSertifikat(tglSertifikatDb.toLocalDate());
        } else {
            pesertaKegiatan.setTglSertifikat(null);
        }
        pesertaKegiatan.setMahasiswa(mahasiswa);
        // Objek Kegiatan untuk PesertaKegiatan mungkin sama dengan objek Kegiatan untuk JadwalKegiatan
        // atau bisa berbeda jika PesertaKegiatan merujuk ke entitas Kegiatan yang lebih umum.
        // Untuk konsistensi, kita bisa set Kegiatan yang sama jika logikanya demikian.
        pesertaKegiatan.setKegiatan(kegiatan);


        PresensiKegiatan presensi = new PresensiKegiatan();
        presensi.setIdPesertaJadwalKegiatan(rs.getObject("id_peserta_jadwal_kegiatan", UUID.class));
        presensi.setStatusPresensi(rs.getString("status_kehadiran"));
        presensi.setJadwalKegiatan(jadwalKegiatan);
        presensi.setPesertaKegiatan(pesertaKegiatan);
        return presensi;
    }

    private String getCommonSelectQueryFields() {
        return "SELECT pre.id_peserta_jadwal_kegiatan, pre.status_kehadiran, "  +
                "p.id_peserta, p.status_sertifikat, p.nomor_sertifikat, p.tanggal_terbit_sertifikat, " +
                "j.id_jadwal_kegiatan, j.waktu_kegiatan, j.lokasi_kegiatan, " + // Koma ditambahkan
                "m.id_mahasiswa, m.nrp, m.nama AS nama_mahasiswa, m.email, m.tgl_lahir, " + // Alias untuk m.nama
                "pr.id_program, pr.nama AS program_nama, " +
                "pro.id_prodi, pro.nama AS prodi_nama, " +
                "k.id_kegiatan, k.nama AS nama_kegiatan, k.kategori AS kategori_kegiatan, k.tanggal_mulai, k.tanggal_selesai, k.publish, " + // Alias untuk k.nama dan k.kategori
                "c.id_club, c.nama AS club_nama " +
                "FROM peserta_jadwal_kegiatan pre " +
                "JOIN jadwal_kegiatan j ON pre.id_jadwal_kegiatan = j.id_jadwal_kegiatan " +
                "JOIN peserta_kegiatan p ON pre.id_peserta = p.id_peserta " + // Menggunakan id_peserta
                "JOIN mahasiswa m ON p.id_mahasiswa = m.id_mahasiswa " +
                "LEFT JOIN program pr ON m.id_program = pr.id_program " + // LEFT JOIN untuk opsional
                "LEFT JOIN prodi pro ON m.id_prodi = pro.id_prodi " +   // LEFT JOIN untuk opsional
                "JOIN kegiatan k ON j.id_kegiatan = k.id_kegiatan " + // Kegiatan dari JadwalKegiatan
                "LEFT JOIN club c ON k.id_club = c.id_club "; // LEFT JOIN untuk opsional
    }

    public List<PresensiKegiatan> findByJadwalKegiatan(JadwalKegiatan jadwalKegiatan) throws Exception {
        List<PresensiKegiatan> list = new ArrayList<>();
        String sql = getCommonSelectQueryFields() +
                "WHERE pre.id_jadwal_kegiatan = ? " +
                "ORDER BY m.nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, jadwalKegiatan.getIdJadwalKegiatan());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPresensiKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat mencari presensi berdasarkan jadwal: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<PresensiKegiatan> findByMahasiswa(Mahasiswa mahasiswaObj) throws Exception {
        List<PresensiKegiatan> list = new ArrayList<>();
        String sql = getCommonSelectQueryFields() +
                "WHERE p.id_mahasiswa = ? " +
                "ORDER BY k.nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, mahasiswaObj.getIdMahasiswa());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPresensiKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat mencari presensi berdasarkan mahasiswa: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<PresensiKegiatan> findByKegiatan(Kegiatan kegiatanObj) throws Exception {
        List<PresensiKegiatan> list = new ArrayList<>();
        String sql = getCommonSelectQueryFields() +
                "WHERE j.id_kegiatan = ? " +
                "ORDER BY m.nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, kegiatanObj.getIdKegiatan());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPresensiKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat mencari presensi berdasarkan kegiatan: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<PresensiKegiatan> findByPesertaKegiatan(PesertaKegiatan pesertaKegiatanObj) throws Exception {
        List<PresensiKegiatan> list = new ArrayList<>();
        String sql = getCommonSelectQueryFields() +
                "WHERE pre.id_peserta = ? " +
                "ORDER BY j.waktu_kegiatan";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, pesertaKegiatanObj.getIdPesertaKegiatan());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPresensiKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat mencari presensi berdasarkan peserta kegiatan: " + e.getMessage());
            throw e;
        }
        return list;
    }
}
