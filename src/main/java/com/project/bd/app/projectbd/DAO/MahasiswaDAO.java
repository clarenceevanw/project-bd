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

    public List<Mahasiswa> findAll() throws Exception {
        List<Mahasiswa> list = new ArrayList<>();
        String sql = "SELECT m.id_mahasiswa, m.nrp, m.nama, m.email, m.tgl_lahir, " +
                "p.id_prodi, p.nama AS prodi_nama, " +
                "pr.id_program, pr.nama AS program_nama " +
                "FROM mahasiswa m " +
                "LEFT JOIN prodi p ON m.id_prodi = p.id_prodi " +
                "LEFT JOIN program pr ON m.id_program = pr.id_program " +
                "ORDER BY m.nama";

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

    public Mahasiswa findById(UUID idMahasiswa) throws Exception {
        String sql = "SELECT m.id_mahasiswa, m.nrp, m.nama, m.email, m.tgl_lahir, " +
                "p.id_prodi, p.nama AS prodi_nama, " +
                "pr.id_program, pr.nama AS program_nama " +
                "FROM mahasiswa m " +
                "LEFT JOIN prodi p ON m.id_prodi = p.id_prodi " +
                "LEFT JOIN program pr ON m.id_program = pr.id_program " +
                "WHERE m.id_mahasiswa = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idMahasiswa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Mahasiswa mhs = mapResultSetToMahasiswa(rs);
                    mhs.setKeanggotaan(findKeanggotaan(idMahasiswa)); // Opsional
                    return mhs;
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }

        return null;
    }

    public Mahasiswa findByNrp(String nrp) throws Exception {
        String sql = "SELECT m.id_mahasiswa, m.nrp, m.nama, m.email, m.tgl_lahir, " +
                "p.id_prodi, p.nama AS prodi_nama, " +
                "pr.id_program, pr.nama AS program_nama " +
                "FROM mahasiswa m " +
                "LEFT JOIN prodi p ON m.id_prodi = p.id_prodi " +
                "LEFT JOIN program pr ON m.id_program = pr.id_program " +
                "WHERE m.nrp = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nrp);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Mahasiswa mhs = mapResultSetToMahasiswa(rs);
                mhs.setKeanggotaan(findKeanggotaan(mhs.getIdMahasiswa())); // Opsional
                return mhs;
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }

        return null;
    }

    public Mahasiswa findByNrpEmail(String nrp, String email) throws Exception {
        String sql = "SELECT m.id_mahasiswa, m.nrp, m.nama, m.email, m.tgl_lahir, " +
                "p.id_prodi, p.nama AS prodi_nama, " +
                "pr.id_program, pr.nama AS program_nama " +
                "FROM mahasiswa m " +
                "LEFT JOIN prodi p ON m.id_prodi = p.id_prodi " +
                "LEFT JOIN program pr ON m.id_program = pr.id_program " +
                "WHERE m.nrp = ? AND m.email = ? ";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nrp);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Mahasiswa mhs = mapResultSetToMahasiswa(rs);
                mhs.setKeanggotaan(findKeanggotaan(mhs.getIdMahasiswa())); // Opsional
                return mhs;
            }
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }

        return null;
    }

    private Mahasiswa mapResultSetToMahasiswa(ResultSet rs) throws SQLException {
        Prodi prodi = new Prodi();
        prodi.setIdProdi(rs.getObject("id_prodi", UUID.class));
        prodi.setNama(rs.getString("prodi_nama"));

        Program program = new Program();
        program.setIdProgram(rs.getObject("id_program", UUID.class));
        program.setNama(rs.getString("program_nama"));

        UUID idMahasiswa = rs.getObject("id_mahasiswa", UUID.class);
        String nrp = rs.getString("nrp");
        String nama = rs.getString("nama");
        String email = rs.getString("email");
        LocalDate tglLahir = rs.getDate("tgl_lahir").toLocalDate();

        return new Mahasiswa(idMahasiswa, prodi, program, nrp, nama, email, tglLahir);
    }

    public List<Keanggotaan> findKeanggotaan(UUID idMahasiswa) throws Exception {
        return new KeanggotaanDAO().findKeanggotaanByMahasiswa(idMahasiswa);
    }

}
