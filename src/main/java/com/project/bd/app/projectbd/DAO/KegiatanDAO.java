package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.Kegiatan;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KegiatanDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public void insert(Kegiatan kegiatan) throws Exception {
        String sql = "INSERT INTO kegiatan (nama, link_dokumentasi, id_club, id_jenis_kegiatan, kategori) VALUES (?, ?, ?, ?, ?) RETURNING id_kegiatan;";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, kegiatan.getNama());
            stmt.setString(2, kegiatan.getLinkDokumentasi());
            stmt.setObject(3, kegiatan.getClub().getId_club());
            stmt.setObject(4, kegiatan.getJenisKegiatan().getIdJenisKegiatan());
            stmt.setString(5, kegiatan.getKategori());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                kegiatan.setIdKegiatan(rs.getObject("id_kegiatan", UUID.class));
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void update(Kegiatan kegiatan) throws Exception {
        String sql = "UPDATE kegiatan SET nama = ?, link_dokumentasi = ?, id_club = ?, id_jenis_kegiatan = ?, kategori = ? WHERE id_kegiatan = ?;";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, kegiatan.getNama());
            stmt.setString(2, kegiatan.getLinkDokumentasi());
            stmt.setObject(3, kegiatan.getClub().getId_club());
            stmt.setObject(4, kegiatan.getJenisKegiatan().getIdJenisKegiatan());
            stmt.setObject(5, kegiatan.getKategori());
            stmt.setObject(6, kegiatan.getIdKegiatan());
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
        String sql = "SELECT * FROM kegiatan WHERE id_kegiatan = ?";
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
        String sql = "SELECT * FROM kegiatan WHERE id_club = ?";
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
        String sql = "SELECT * FROM kegiatan ORDER BY nama";
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
        kegiatan.setNama(rs.getString("nama"));
        kegiatan.setLinkDokumentasi(rs.getString("link_dokumentasi"));
        kegiatan.setClub(new ClubDAO().findById(rs.getObject("id_club", UUID.class)));
        kegiatan.setJenisKegiatan(new JenisKegiatanDAO().findById(rs.getObject("id_jenis_kegiatan", UUID.class)));
        kegiatan.setKategori(rs.getString("kategori"));
        return kegiatan;
    }
}
