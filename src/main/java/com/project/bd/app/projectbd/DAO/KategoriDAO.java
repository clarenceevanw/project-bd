package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.Kategori;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KategoriDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public Kategori findById(UUID id_kategori) throws Exception {
        String sql = "SELECT * FROM kategori WHERE id_kategori = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, id_kategori);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKategori(rs);
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return null;
    }

    public List<Kategori> findAll() throws Exception {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori ORDER BY nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToKategori(rs));
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return list;
    }

    public void insert(String nama) throws Exception {
        String sql = "INSERT INTO kategori (nama) VALUES (?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nama);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void update(Kategori kategori) throws Exception {
        String sql = "UPDATE kategori SET nama = ? WHERE id_kategori = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, kategori.getNama());
            stmt.setObject(2, kategori.getId_kategori());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void delete(UUID id_kategori) throws Exception {
        String sql = "DELETE FROM kategori WHERE id_kategori = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, id_kategori);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public Kategori mapResultSetToKategori(ResultSet rs) throws SQLException {
        Kategori kategori = new Kategori();
        kategori.setId_kategori(rs.getObject("id_kategori", UUID.class));
        kategori.setNama(rs.getString("nama"));
        return kategori;
    }
}
