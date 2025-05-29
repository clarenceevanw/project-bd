package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.*;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MahasiswaDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public void insert(Mahasiswa mhs) throws Exception {
        String sql = "INSERT INTO mahasiswa (id_mahasiswa, id_prodi, id_program, nrp, nama, email, tgl_lahir) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, mhs.getIdMahasiswa());
            stmt.setObject(2, mhs.getProdi().getIdProdi());
            stmt.setObject(3, mhs.getProgram().getIdProgram());
            stmt.setString(4, mhs.getNrp());
            stmt.setString(5, mhs.getNama());
            stmt.setString(6, mhs.getEmail());
            stmt.setDate(7, Date.valueOf(mhs.getTglLahir()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void update(Mahasiswa mhs) throws Exception {
        String sql = "UPDATE mahasiswa SET id_prodi = ?, id_program = ?, nrp = ?, nama = ?, email = ?, tgl_lahir = ? " +
                "WHERE id_mahasiswa = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, mhs.getProdi().getIdProdi());
            stmt.setObject(2, mhs.getProgram().getIdProgram());
            stmt.setString(3, mhs.getNrp());
            stmt.setString(4, mhs.getNama());
            stmt.setString(5, mhs.getEmail());
            stmt.setDate(6, Date.valueOf(mhs.getTglLahir()));
            stmt.setObject(7, mhs.getIdMahasiswa());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public void delete(UUID idMahasiswa) throws Exception {
        String sql = "DELETE FROM mahasiswa WHERE id_mahasiswa = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idMahasiswa);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }

    public Mahasiswa findById(UUID idMahasiswa) throws Exception {
        String sql = "SELECT * FROM mahasiswa WHERE id_mahasiswa = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idMahasiswa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMahasiswa(rs);
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return null;
    }

    public Mahasiswa findByNrp(String nrp) throws Exception {
        String sql = "SELECT * FROM mahasiswa WHERE nrp = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nrp);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToMahasiswa(rs);
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
        return null;
    }

    public List<Mahasiswa> findAll() throws Exception{
        List<Mahasiswa> list = new ArrayList<>();
        int index = 0;
        String sql = "SELECT * FROM mahasiswa ORDER BY nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToMahasiswa(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Keanggotaan> findKeanggotaan(UUID idMahasiswa) throws Exception {
        return new KeanggotaanDAO().findKeanggotaanByMahasiswa(idMahasiswa);
    }

    private Mahasiswa mapResultSetToMahasiswa(ResultSet rs) throws SQLException, Exception {
        UUID idMahasiswa = (UUID) rs.getObject("id_mahasiswa");
        UUID idProdi = (UUID) rs.getObject("id_prodi");
        Prodi prodi = new ProdiDAO().findById(idProdi);
        UUID idProgram = (UUID) rs.getObject("id_program");
        Program program = new ProgramDAO().findById(idProgram);
        String nrp = rs.getString("nrp");
        String nama = rs.getString("nama");
        String email = rs.getString("email");
        LocalDate tglLahir = rs.getDate("tgl_lahir").toLocalDate();

        Mahasiswa mhs = new Mahasiswa(idMahasiswa, prodi, program, nrp, nama, email, tglLahir);
        List<Keanggotaan> keanggotaan = findKeanggotaan(idMahasiswa);
        mhs.setKeanggotaan(keanggotaan);
        return mhs;
    }
}
