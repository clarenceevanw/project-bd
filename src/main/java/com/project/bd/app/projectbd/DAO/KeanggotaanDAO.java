package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.Keanggotaan;
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
        String sql = "SELECT * " +
                "FROM keanggotaan " +
                "WHERE id_mahasiswa = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idMahasiswa);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UUID idKeanggotaan = rs.getObject("id_keanggotaan", UUID.class);
                UUID id_mahasiswa = rs.getObject("id_mahasiswa", UUID.class);
                UUID idClub = rs.getObject("id_club", UUID.class);
                list.add(new Keanggotaan(idKeanggotaan, id_mahasiswa, idClub, rs.getString("peran"), rs.getDate("tanggal_bergabung").toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Keanggotaan> findKeanggotaanByClub(UUID idClub) {
        List<Keanggotaan> list = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM keanggotaan " +
                "WHERE id_club = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idClub);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UUID idKeanggotaan = rs.getObject("id_keanggotaan", UUID.class);
                UUID id_mahasiswa = rs.getObject("id_mahasiswa", UUID.class);
                UUID id_club = rs.getObject("id_club", UUID.class);
                list.add(new Keanggotaan(idKeanggotaan, id_mahasiswa, id_club, rs.getString("peran"), rs.getDate("tanggal_bergabung").toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
