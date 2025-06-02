package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.*;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JadwalKegiatanDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public UUID insert(JadwalKegiatan jadwalKegiatan) throws Exception {
        String sql = "INSERT INTO jadwal_kegiatan (waktu_kegiatan, lokasi_kegiatan, id_kegiatan) VALUES(?, ?, ?) RETURNING id_jadwal_kegiatan;";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(jadwalKegiatan.getWaktuKegiatan()));
            stmt.setString(2, jadwalKegiatan.getLokasiKegiatan());
            stmt.setObject(3, jadwalKegiatan.getKegiatan().getIdKegiatan());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                jadwalKegiatan.setIdJadwalKegiatan(rs.getObject("id_jadwal_kegiatan", UUID.class));
                return jadwalKegiatan.getIdJadwalKegiatan();
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return null;
    }

    public void update(JadwalKegiatan jadwalKegiatan) throws Exception {
        String sql = "UPDATE jadwal_kegiatan SET waktu_kegiatan = ?, lokasi_kegiatan = ?, id_kegiatan = ? WHERE id_jadwal_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(jadwalKegiatan.getWaktuKegiatan()));
            stmt.setString(2, jadwalKegiatan.getLokasiKegiatan());
            stmt.setObject(3, jadwalKegiatan.getKegiatan().getIdKegiatan());
            stmt.setObject(4, jadwalKegiatan.getIdJadwalKegiatan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void delete(UUID idJadwalKegiatan) throws Exception {
        String sql = "DELETE FROM jadwal_kegiatan WHERE id_jadwal_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idJadwalKegiatan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public List<JadwalKegiatan> findAll() throws Exception {
        List<JadwalKegiatan> list = new ArrayList<>();
        String sql = "SELECT ja.*, k.id_kegiatan, k.nama as nama_kegiatan, k.link_dokumentasi, k.kategori, c.id_club, c.nama, c.deskripsi, c.tahun_berdiri, ka.id_kategori, ka.nama as nama_kategori, j.id_jenis_kegiatan, j.nama as nama_jenis_kegiatan FROM jadwal_kegiatan ja " +
                "JOIN kegiatan k ON ja.id_kegiatan = k.id_kegiatan " +
                "JOIN club c ON k.id_club = c.id_club " +
                "JOIN kategori ka on c.id_kategori = ka.id_kategori " +
                "JOIN jenis_kegiatan j ON k.id_jenis_kegiatan = j.id_jenis_kegiatan " +
                "ORDER BY ja.waktu_kegiatan";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToJadwalKegiatan(rs));
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return list;
    }

    public List<JadwalKegiatan> findByKegiatan(Kegiatan kegiatan) throws Exception {
        List<JadwalKegiatan> list = new ArrayList<>();
        // Query Anda sudah benar, namun pastikan nama alias dan kolom sudah tepat
        String sql = "SELECT ja.*, k.id_kegiatan, k.nama as nama_kegiatan, k.link_dokumentasi, k.kategori as kategori_kegiatan, " + // Beri alias jika 'kategori' ambigu
                "c.id_club, c.nama as nama_club, c.deskripsi, c.tahun_berdiri, " + // Beri alias 'nama_club'
                "ka.id_kategori, ka.nama as nama_kategori_club, " + // Beri alias 'nama_kategori_club'
                "j.id_jenis_kegiatan, j.nama as nama_jenis_kegiatan " +
                "FROM jadwal_kegiatan ja " +
                "JOIN kegiatan k ON ja.id_kegiatan = k.id_kegiatan " +
                "JOIN club c ON k.id_club = c.id_club " +
                "JOIN kategori ka on c.id_kategori = ka.id_kategori " +
                "JOIN jenis_kegiatan j ON k.id_jenis_kegiatan = j.id_jenis_kegiatan " +
                "WHERE ja.id_kegiatan = ? " +
                "ORDER BY ja.waktu_kegiatan";

        if (kegiatan == null || kegiatan.getIdKegiatan() == null) {
            System.out.println("[DAO] Objek Kegiatan atau ID Kegiatan adalah null. Mengembalikan list kosong.");
            return list;
        }
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setObject(1, kegiatan.getIdKegiatan());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    try {
                        list.add(mapResultSetToJadwalKegiatan(rs));
                    } catch (SQLException mapEx) {
                        AlertNotification.showError(mapEx.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return list;
    }

    public List<JadwalKegiatan> findByJadwalKegiatan(JadwalKegiatan jadwalKegiatan) throws Exception {
        List<JadwalKegiatan> list = new ArrayList<>();
        String sql = "SELECT ja.*, k.id_kegiatan, k.nama as nama_kegiatan, k.link_dokumentasi, k.kategori, c.id_club, c.nama, c.deskripsi, c.tahun_berdiri, ka.id_kategori, ka.nama as nama_kategori, j.id_jenis_kegiatan, j.nama as nama_jenis_kegiatan FROM jadwal_kegiatan ja " +
                "JOIN kegiatan k ON ja.id_kegiatan = k.id_kegiatan " +
                "JOIN club c ON k.id_club = c.id_club " +
                "JOIN kategori ka on c.id_kategori = ka.id_kategori " +
                "JOIN jenis_kegiatan j ON k.id_jenis_kegiatan = j.id_jenis_kegiatan " +
                "WHERE ja.id_jadwal_kegiatan = ? " +
                "ORDER BY ja.waktu_kegiatan";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, jadwalKegiatan.getIdJadwalKegiatan());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToJadwalKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return list;
    }

    public JadwalKegiatan mapResultSetToJadwalKegiatan(ResultSet rs) throws SQLException {
        JadwalKegiatan jadwalKegiatan = new JadwalKegiatan();
        jadwalKegiatan.setIdJadwalKegiatan(rs.getObject("id_jadwal_kegiatan", UUID.class));
        jadwalKegiatan.setWaktuKegiatan(rs.getTimestamp("waktu_kegiatan").toLocalDateTime());
        jadwalKegiatan.setLokasiKegiatan(rs.getString("lokasi_kegiatan"));

        Kategori kategoriClub = new Kategori(rs.getObject("id_kategori", UUID.class), rs.getString("nama_kategori_club"));
        Club club = new Club(rs.getObject("id_club", UUID.class), kategoriClub, rs.getString("nama_club"), rs.getString("deskripsi"), rs.getInt("tahun_berdiri"));
        JenisKegiatan jenisKegiatan = new JenisKegiatan(rs.getObject("id_jenis_kegiatan", UUID.class), rs.getString("nama_jenis_kegiatan"));

        Kegiatan kegiatanObj = new Kegiatan(rs.getObject("id_kegiatan", UUID.class), rs.getString("nama_kegiatan"), rs.getString("link_dokumentasi"), club, jenisKegiatan, rs.getString("kategori_kegiatan")); // atau rs.getString("kategori") jika tidak ada alias
        jadwalKegiatan.setKegiatan(kegiatanObj);
        return jadwalKegiatan;
    }
}
