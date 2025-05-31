package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.JenisKegiatan;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JenisKegiatanDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public List<JenisKegiatan> findAll() throws Exception {
        List<JenisKegiatan> list = new ArrayList<>();
        String sql = "SELECT * FROM jenis_kegiatan ORDER BY nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToJenisKegiatan(rs));
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return list;
    }

    public JenisKegiatan findById(UUID idJenisKegiatan) throws Exception {
        String sql = "SELECT * FROM jenis_kegiatan WHERE id_jenis_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idJenisKegiatan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToJenisKegiatan(rs);
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return null;
    }

    public void insert(String nama) throws Exception {
        String sql = "INSERT INTO jenis_kegiatan (nama) VALUES (?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nama);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void update(JenisKegiatan jenisKegiatan) throws Exception {
        String sql = "UPDATE jenis_kegiatan SET nama = ? WHERE id_jenis_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, jenisKegiatan.getNama());
            stmt.setObject(2, jenisKegiatan.getIdJenisKegiatan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void delete(UUID idJenisKegiatan) throws Exception {
        String sql = "DELETE FROM jenis_kegiatan WHERE id_jenis_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idJenisKegiatan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    private JenisKegiatan mapResultSetToJenisKegiatan(ResultSet rs) throws SQLException {
        JenisKegiatan jenisKegiatan = new JenisKegiatan();
        jenisKegiatan.setIdJenisKegiatan(rs.getObject("id_jenis_kegiatan", UUID.class));
        jenisKegiatan.setNama(rs.getString("nama"));
        return jenisKegiatan;
    }
}
