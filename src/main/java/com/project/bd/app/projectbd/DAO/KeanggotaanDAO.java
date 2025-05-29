package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.Club;
import com.project.bd.app.projectbd.Model.Kategori;
import com.project.bd.app.projectbd.Model.Keanggotaan;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KeanggotaanDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public List<Keanggotaan> findKeanggotaanByMahasiswa(UUID idMahasiswa) {
        List<Keanggotaan> list = new ArrayList<>();
        String sql = """
        SELECT k.*, c.*, k2.nama AS nama_kategori
        FROM keanggotaan k
        JOIN club c ON k.id_club = c.id_club
        JOIN kategori k2 ON c.id_kategori = k2.id_kategori
        WHERE k.id_mahasiswa = ?
    """;
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idMahasiswa);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UUID idKeanggotaan = rs.getObject("id_keanggotaan", UUID.class);
                UUID idClub = rs.getObject("id_club", UUID.class);

                // Buat Club
                Club club = new Club();
                club.setId_club(idClub);
                club.setNama(rs.getString("nama"));
                club.setDeskripsi(rs.getString("deskripsi"));
                club.setTahun_berdiri(rs.getInt("tahun_berdiri"));
                club.setKategori(new Kategori(rs.getObject("id_kategori", UUID.class), rs.getString("nama_kategori")));
                Keanggotaan keanggotaan = new Keanggotaan(
                        idKeanggotaan,
                        idMahasiswa,
                        club,
                        rs.getString("peran"),
                        rs.getDate("tanggal_bergabung").toLocalDate()
                );
                list.add(keanggotaan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<Keanggotaan> findKeanggotaanByClub(UUID idClub) {
        List<Keanggotaan> list = new ArrayList<>();
        String sql = """
        SELECT k.*, c.*, k2.nama AS nama_kategori
        FROM keanggotaan k
        JOIN club c ON k.id_club = c.id_club
        JOIN kategori k2 ON c.id_kategori = k2.id_kategori
        WHERE k.id_club = ?
    """;
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idClub);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UUID idKeanggotaan = rs.getObject("id_keanggotaan", UUID.class);
                UUID idMahasiswaResult = rs.getObject("id_mahasiswa", UUID.class);

                // Buat Club
                Club club = new Club();
                club.setId_club(idClub);
                club.setNama(rs.getString("nama"));
                club.setDeskripsi(rs.getString("deskripsi"));
                club.setTahun_berdiri(rs.getInt("tahun_berdiri"));
                club.setKategori(new Kategori(rs.getObject("id_kategori", UUID.class), rs.getString("nama_kategori")));
                Keanggotaan keanggotaan = new Keanggotaan(
                        idKeanggotaan,
                        idMahasiswaResult,
                        club,
                        rs.getString("peran"),
                        rs.getDate("tanggal_bergabung").toLocalDate()
                );

                list.add(keanggotaan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Keanggotaan keanggotaan) throws Exception {
        String sql = "INSERT INTO keanggotaan (id_mahasiswa, id_club, peran, tanggal_bergabung) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, keanggotaan.getId_mahasiswa());
            stmt.setObject(2, keanggotaan.getClub().getId_club());
            stmt.setString(3, keanggotaan.getPeran());
            stmt.setDate(4, java.sql.Date.valueOf(keanggotaan.getTanggal_bergabung()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }
}
