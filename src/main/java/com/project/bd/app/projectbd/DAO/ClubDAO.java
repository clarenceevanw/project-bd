package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.Club;
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

public class ClubDAO {
    private Connection getConnection() throws SQLException{
        return DatabaseConnection.getConnection();
    }

    public void insert(Club club) throws Exception {
        String sql = "INSERT INTO club (id_kategori, nama, deskripsi, tahun_berdiri) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, club.getId_kategori());
            stmt.setString(2, club.getNama());
            stmt.setString(3, club.getDeskripsi());
            stmt.setInt(4, club.getTahun_berdiri());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void update(Club club) throws Exception{
        String sql = "UPDATE club SET id_kategori = ?, nama = ?, deskripsi = ?, tahun_berdiri = ? WHERE id_club = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, club.getId_kategori());
            stmt.setString(2, club.getNama());
            stmt.setString(3, club.getDeskripsi());
            stmt.setInt(4, club.getTahun_berdiri());
            stmt.setObject(5, club.getId_club());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void delete(UUID id_club) throws Exception{
        String sql = "DELETE FROM club WHERE id_club = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, id_club);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public Club findById(UUID id_club) throws Exception{
        String sql = "SELECT * FROM club WHERE id_club = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, id_club);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClub(rs);
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return null;
    }

    public List<Club> findAll() throws Exception{
        List<Club> list = new ArrayList<>();
        String sql = "SELECT * FROM club ORDER BY nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToClub(rs));
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return list;
    }

    public List<Keanggotaan> findKeanggotaan(UUID id_club) throws Exception{
        return new KeanggotaanDAO().findKeanggotaanByClub(id_club);
    }

    private Club mapResultSetToClub(ResultSet rs) throws SQLException {
        Club club = new Club();
        club.setId_club(rs.getObject("id_club", UUID.class));
        club.setNama(rs.getString("nama"));
        club.setDeskripsi(rs.getString("deskripsi"));
        club.setTahun_berdiri(rs.getInt("tahun_berdiri"));
        club.setId_kategori(rs.getObject("id_kategori", UUID.class));
        List<Keanggotaan> keanggotaan = new KeanggotaanDAO().findKeanggotaanByClub(club.getId_club());
        club.setAnggota(keanggotaan);
        return club;
    }
}
