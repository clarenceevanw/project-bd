package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.*;
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

    public List<Keanggotaan> findKeanggotaanByMahasiswa(UUID idMahasiswa) throws Exception {
        List<Keanggotaan> list = new ArrayList<>();
        String sql = """
        SELECT k.*, m.nama AS nama_mahasiswa, m.nrp as nrp, m.email as email, m.tgl_lahir as tgl_lahir, m.id_prodi as id_prodi, m.id_program as id_program, c.*, k2.nama AS nama_kategori
        FROM keanggotaan k
        JOIN mahasiswa m ON k.id_mahasiswa = m.id_mahasiswa
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

                // Buat Mahasiswa
                Mahasiswa mhs = new Mahasiswa();
                mhs.setIdMahasiswa(idMahasiswa);
                mhs.setNrp(rs.getString("nrp"));
                mhs.setNama(rs.getString("nama_mahasiswa"));
                mhs.setEmail(rs.getString("email"));
                mhs.setTglLahir(rs.getDate("tgl_lahir").toLocalDate());
                Prodi prodi = new ProdiDAO().findById(rs.getObject("id_prodi", UUID.class));
                Program program = new ProgramDAO().findById(rs.getObject("id_program", UUID.class));
                mhs.setProdi(prodi);
                mhs.setProgram(program);

                // Buat Club
                Club club = new Club();
                club.setId_club(idClub);
                club.setNama(rs.getString("nama"));
                club.setDeskripsi(rs.getString("deskripsi"));
                club.setTahun_berdiri(rs.getInt("tahun_berdiri"));
                club.setKategori(new Kategori(rs.getObject("id_kategori", UUID.class), rs.getString("nama_kategori")));
                Keanggotaan keanggotaan = new Keanggotaan(
                        idKeanggotaan,
                        mhs,
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


    public List<Keanggotaan> findKeanggotaanByClub(UUID idClub) throws Exception {
        List<Keanggotaan> list = new ArrayList<>();
        String sql = """
        SELECT k.*, m.id_mahasiswa as id_mahasiswa, m.nama AS nama_mahasiswa, m.nrp as nrp, m.email as email, m.tgl_lahir as tgl_lahir, m.id_prodi as id_prodi, m.id_program as id_program, c.*, k2.nama AS nama_kategori
        FROM keanggotaan k
        JOIN mahasiswa m ON k.id_mahasiswa = m.id_mahasiswa
        JOIN club c ON k.id_club = c.id_club
        JOIN kategori k2 ON c.id_kategori = k2.id_kategori
        WHERE k.id_prodi = ?
    """;
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idClub);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UUID idKeanggotaan = rs.getObject("id_keanggotaan", UUID.class);
                UUID idMahasiswaResult = rs.getObject("id_mahasiswa", UUID.class);

                // Buat Mahasiswa
                Mahasiswa mhs = new Mahasiswa();
                mhs.setIdMahasiswa(idMahasiswaResult);
                mhs.setNrp(rs.getString("nrp"));
                mhs.setNama(rs.getString("nama_mahasiswa"));
                mhs.setEmail(rs.getString("email"));
                mhs.setTglLahir(rs.getDate("tgl_lahir").toLocalDate());
                Prodi prodi = new ProdiDAO().findById(rs.getObject("id_prodi", UUID.class));
                Program program = new ProgramDAO().findById(rs.getObject("id_program", UUID.class));
                mhs.setProdi(prodi);
                mhs.setProgram(program);

                // Buat Club
                Club club = new Club();
                club.setId_club(idClub);
                club.setNama(rs.getString("nama"));
                club.setDeskripsi(rs.getString("deskripsi"));
                club.setTahun_berdiri(rs.getInt("tahun_berdiri"));
                club.setKategori(new Kategori(rs.getObject("id_kategori", UUID.class), rs.getString("nama_kategori")));
                Keanggotaan keanggotaan = new Keanggotaan(
                        idKeanggotaan,
                        mhs,
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
            stmt.setObject(1, keanggotaan.getMahasiswa().getIdMahasiswa());
            stmt.setObject(2, keanggotaan.getClub().getId_club());
            stmt.setString(3, keanggotaan.getPeran());
            stmt.setDate(4, java.sql.Date.valueOf(keanggotaan.getTanggal_bergabung()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError(e.getMessage());
        }
    }
}
