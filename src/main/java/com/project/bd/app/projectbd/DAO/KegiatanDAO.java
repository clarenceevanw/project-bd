package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.JenisKegiatan;
import com.project.bd.app.projectbd.Model.Kategori;
import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KegiatanDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public void insert(Kegiatan kegiatan) throws Exception {
        String sql = "INSERT INTO kegiatan (nama, link_dokumentasi, id_club, id_jenis_kegiatan, kategori, tanggal_mulai, tanggal_selesai) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_kegiatan;";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, kegiatan.getNama());
            stmt.setString(2, kegiatan.getLinkDokumentasi());
            stmt.setObject(3, kegiatan.getClub().getId_club());
            stmt.setObject(4, kegiatan.getJenisKegiatan().getIdJenisKegiatan());
            stmt.setString(5, kegiatan.getKategori());
            stmt.setDate(6, Date.valueOf(kegiatan.getTanggalMulai()));
            stmt.setDate(7, Date.valueOf(kegiatan.getTanggalSelesai()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                kegiatan.setIdKegiatan(rs.getObject("id_kegiatan", UUID.class));
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void update(Kegiatan kegiatan) throws Exception {
        String sql = "UPDATE kegiatan SET nama = ?, link_dokumentasi = ?, id_club = ?, id_jenis_kegiatan = ?, kategori = ?, tanggal_mulai = ?, tanggal_selesai = ? WHERE id_kegiatan = ?;";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, kegiatan.getNama());
            stmt.setString(2, kegiatan.getLinkDokumentasi());
            stmt.setObject(3, kegiatan.getClub().getId_club());
            stmt.setObject(4, kegiatan.getJenisKegiatan().getIdJenisKegiatan());
            stmt.setString(5, kegiatan.getKategori());
            stmt.setDate(6, Date.valueOf(kegiatan.getTanggalMulai()));
            stmt.setDate(7, Date.valueOf(kegiatan.getTanggalSelesai()));
            stmt.setObject(8, kegiatan.getIdKegiatan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void updatePublish(Kegiatan kegiatan) throws Exception {
        String sql = "UPDATE kegiatan SET publish = ? WHERE id_kegiatan = ?;";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setBoolean(1, kegiatan.isPublish());
            stmt.setObject(2, kegiatan.getIdKegiatan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void delete(UUID idKegiatan) throws Exception {
        String sql = "DELETE FROM kegiatan WHERE id_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idKegiatan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public Kegiatan findById(UUID idKegiatan) throws Exception {
        String sql = "SELECT k.id_kegiatan, k.nama as nama_kegiatan, k.link_dokumentasi, k.kategori, k.tanggal_mulai, k.tanggal_selesai, k.publish , c.id_club, c.nama, c.deskripsi, c.tahun_berdiri, ka.id_kategori, ka.nama as nama_kategori, j.id_jenis_kegiatan, j.nama as nama_jenis_kegiatan FROM kegiatan k " +
                "JOIN club c ON k.id_club = c.id_club " +
                "JOIN kategori ka on c.id_kategori = ka.id_kategori " +
                "JOIN jenis_kegiatan j ON k.id_jenis_kegiatan = j.id_jenis_kegiatan " +
                "WHERE k.id_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idKegiatan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKegiatan(rs);
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return null;
    }

    public List<Kegiatan> findByClub(UUID idClub) throws Exception {
        List<Kegiatan> list = new ArrayList<>();
        String sql = "SELECT k.id_kegiatan, k.nama as nama_kegiatan, k.link_dokumentasi, k.kategori, k.tanggal_mulai, k.tanggal_selesai, k.publish, c.id_club, c.nama, c.deskripsi, c.tahun_berdiri, ka.id_kategori, ka.nama as nama_kategori, j.id_jenis_kegiatan, j.nama as nama_jenis_kegiatan FROM kegiatan k " +
                "JOIN club c ON k.id_club = c.id_club " +
                "JOIN kategori ka on c.id_kategori = ka.id_kategori " +
                "JOIN jenis_kegiatan j ON k.id_jenis_kegiatan = j.id_jenis_kegiatan " +
                "WHERE k.id_club = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idClub);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return list;
    }

    public List<Kegiatan> findAll() throws Exception {
        List<Kegiatan> list = new ArrayList<>();
        String sql =  "SELECT k.id_kegiatan, k.nama as nama_kegiatan, k.link_dokumentasi, k.kategori, k.tanggal_mulai, k.tanggal_selesai, k.publish, c.id_club, c.nama, c.deskripsi, c.tahun_berdiri, ka.id_kategori, ka.nama as nama_kategori, j.id_jenis_kegiatan, j.nama as nama_jenis_kegiatan FROM kegiatan k " +
                "JOIN club c ON k.id_club = c.id_club " +
                "JOIN kategori ka on c.id_kategori = ka.id_kategori " +
                "JOIN jenis_kegiatan j ON k.id_jenis_kegiatan = j.id_jenis_kegiatan " +
                "ORDER BY k.nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToKegiatan(rs));
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return list;
    }

    private Kegiatan mapResultSetToKegiatan(ResultSet rs) throws Exception {
        Kegiatan kegiatan = new Kegiatan();
        kegiatan.setIdKegiatan(rs.getObject("id_kegiatan", UUID.class));
        kegiatan.setNama(rs.getString("nama_kegiatan"));
        kegiatan.setLinkDokumentasi(rs.getString("link_dokumentasi"));
        kegiatan.setKategori(rs.getString("kategori"));
        kegiatan.setTanggalMulai(rs.getObject("tanggal_mulai", LocalDate.class));
        kegiatan.setTanggalSelesai(rs.getObject("tanggal_selesai", LocalDate.class));
        kegiatan.setPublish(rs.getBoolean("publish"));

        Kategori kategori = new Kategori(rs.getObject("id_kategori", UUID.class), rs.getString("nama_kategori"));

        Club club = new Club();
        club.setId_club(rs.getObject("id_club", UUID.class));
        club.setNama(rs.getString("nama"));
        club.setDeskripsi(rs.getString("deskripsi"));
        club.setTahun_berdiri(rs.getInt("tahun_berdiri"));
        club.setKategori(kategori);
        kegiatan.setClub(club);

        JenisKegiatan jenisKegiatan = new JenisKegiatan(rs.getObject("id_jenis_kegiatan", UUID.class), rs.getString("nama_jenis_kegiatan"));
        kegiatan.setJenisKegiatan(jenisKegiatan);
        kegiatan.setKategori(rs.getString("kategori"));
        return kegiatan;
    }
}
