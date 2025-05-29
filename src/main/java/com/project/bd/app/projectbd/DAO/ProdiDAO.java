package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.Prodi;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProdiDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public List<Prodi> findAll() throws Exception {
        List<Prodi> list = new ArrayList<>();
        String sql = "SELECT * FROM prodi ORDER BY nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProdi(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Prodi findById(UUID idProdi) throws Exception {
        String sql = "SELECT * FROM prodi WHERE id_prodi = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idProdi);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProdi(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Prodi mapResultSetToProdi(ResultSet rs) throws SQLException {
        UUID idProdi = rs.getObject("id_prodi", UUID.class);
        String nama = rs.getString("nama");
        return new Prodi(idProdi, nama);
    }
}
