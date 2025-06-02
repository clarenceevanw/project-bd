package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.*;
import com.project.bd.app.projectbd.utils.AlertNotification;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PesertaKegiatanDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public UUID insert(PesertaKegiatan pesertaKegiatan) throws Exception {
        // Menggunakan 'id_peserta' sebagai primary key yang dikembalikan
        String sql = "INSERT INTO peserta_kegiatan (id_mahasiswa, id_kegiatan, status_sertifikat, nomor_sertifikat, tanggal_terbit_sertifikat) VALUES (?, ?, ?, ?, ?) RETURNING id_peserta;";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, pesertaKegiatan.getMahasiswa().getIdMahasiswa());
            stmt.setObject(2, pesertaKegiatan.getKegiatan().getIdKegiatan());

            if ("Rutin".equals(pesertaKegiatan.getKegiatan().getKategori())) {
                stmt.setString(3, "Tidak");
                stmt.setNull(4, Types.VARCHAR);
                stmt.setNull(5, Types.DATE);
            } else {
                stmt.setString(3, "Ada");
                stmt.setString(4, pesertaKegiatan.getNomorSertifikat());
                if (pesertaKegiatan.getTglSertifikat() != null) {
                    stmt.setDate(5, Date.valueOf(pesertaKegiatan.getTglSertifikat()));
                } else {
                    stmt.setNull(5, Types.DATE);
                }
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Mengambil 'id_peserta' sesuai RETURNING clause
                // Pastikan model PesertaKegiatan memiliki setIdPeserta(UUID id)
                pesertaKegiatan.setIdPesertaKegiatan(rs.getObject("id_peserta", UUID.class));
                return pesertaKegiatan.getIdPesertaKegiatan();
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat memasukkan data peserta kegiatan: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public void update(PesertaKegiatan pesertaKegiatan) throws Exception {
        // Menggunakan 'id_peserta' pada WHERE clause
        String sql = "UPDATE peserta_kegiatan SET id_mahasiswa = ?, id_kegiatan = ?, status_sertifikat = ?, nomor_sertifikat = ?, tanggal_terbit_sertifikat = ? WHERE id_peserta = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, pesertaKegiatan.getMahasiswa().getIdMahasiswa());
            stmt.setObject(2, pesertaKegiatan.getKegiatan().getIdKegiatan());

            if ("Rutin".equals(pesertaKegiatan.getKegiatan().getKategori())) {
                stmt.setString(3, "Tidak");
                stmt.setNull(4, Types.VARCHAR);
                stmt.setNull(5, Types.DATE);
            } else {
                stmt.setString(3, pesertaKegiatan.getStatusSertifikat());
                stmt.setString(4, pesertaKegiatan.getNomorSertifikat());
                if (pesertaKegiatan.getTglSertifikat() != null) {
                    stmt.setDate(5, Date.valueOf(pesertaKegiatan.getTglSertifikat()));
                } else {
                    stmt.setNull(5, Types.DATE);
                }
            }
            stmt.setObject(6, pesertaKegiatan.getIdPesertaKegiatan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat memperbarui data peserta kegiatan: " + e.getMessage());
            throw e;
        }
    }

    public void updateSertifikatByKegiatan(PesertaKegiatan pesertaKegiatan) throws Exception {
        String sql = "UPDATE peserta_kegiatan SET status_sertifikat = ? , nomor_sertifikat = ?, tanggal_terbit_sertifikat = ? WHERE id_kegiatan = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, pesertaKegiatan.getStatusSertifikat());
            stmt.setString(2, pesertaKegiatan.getNomorSertifikat());
            if (pesertaKegiatan.getTglSertifikat() != null) {
                stmt.setDate(3, Date.valueOf(pesertaKegiatan.getTglSertifikat()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setObject(4, pesertaKegiatan.getKegiatan().getIdKegiatan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat memperbarui data peserta kegiatan: " + e.getMessage());
            throw e;
        }
    }

    public void delete(UUID idPeserta) throws Exception {
        String sql = "DELETE FROM peserta_kegiatan WHERE id_peserta = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idPeserta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat menghapus data peserta kegiatan: " + e.getMessage());
            throw e;
        }
    }

    private PesertaKegiatan mapRowToPesertaKegiatan(ResultSet rs) throws SQLException {
        PesertaKegiatan pesertaKegiatan = new PesertaKegiatan();
        pesertaKegiatan.setIdPesertaKegiatan(rs.getObject("id_peserta", UUID.class));
        pesertaKegiatan.setStatusSertifikat(rs.getString("status_sertifikat"));
        pesertaKegiatan.setNomorSertifikat(rs.getString("nomor_sertifikat"));
        Date tglSertifikatDb = rs.getDate("tanggal_terbit_sertifikat");
        if (tglSertifikatDb != null) {
            pesertaKegiatan.setTglSertifikat(tglSertifikatDb.toLocalDate());
        } else {
            pesertaKegiatan.setTglSertifikat(null);
        }

        Prodi prodi = new Prodi();
        if (rs.getObject("id_prodi") != null) {
            prodi.setIdProdi(rs.getObject("id_prodi", UUID.class));
            prodi.setNama(rs.getString("prodi_nama"));
        } else {
            prodi = null;
        }

        Program program = new Program();
        if (rs.getObject("id_program") != null) {
            program.setIdProgram(rs.getObject("id_program", UUID.class));
            program.setNama(rs.getString("program_nama"));
        } else {
            program = null;
        }

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setIdMahasiswa(rs.getObject("id_mahasiswa", UUID.class));
        mahasiswa.setNrp(rs.getString("nrp"));
        mahasiswa.setNama(rs.getString("m_nama"));
        mahasiswa.setEmail(rs.getString("email"));
        Date tglLahirDb = rs.getDate("tgl_lahir");
        if(tglLahirDb != null) {
            mahasiswa.setTglLahir(tglLahirDb.toLocalDate());
        } else {
            mahasiswa.setTglLahir(null);
        }
        mahasiswa.setProgram(program);
        mahasiswa.setProdi(prodi);

        Club club = null;
        if (rs.getObject("id_club") != null) {
            club = new Club();
            club.setId_club(rs.getObject("id_club", UUID.class));
            club.setNama(rs.getString("club_nama"));
        }

        Kegiatan kegiatan = new Kegiatan();
        kegiatan.setIdKegiatan(rs.getObject("id_kegiatan", UUID.class));
        kegiatan.setNama(rs.getString("kegiatan_nama_asli"));
        kegiatan.setKategori(rs.getString("kegiatan_kategori"));
        kegiatan.setClub(club);

        pesertaKegiatan.setMahasiswa(mahasiswa);
        pesertaKegiatan.setKegiatan(kegiatan);
        return pesertaKegiatan;
    }

    public List<PesertaKegiatan> findByKegiatan(Kegiatan kegiatanObj) throws Exception {
        // Memilih 'p.id_peserta'
        String sql = "SELECT p.id_peserta, p.status_sertifikat, p.nomor_sertifikat, p.tanggal_terbit_sertifikat, " +
                "m.id_mahasiswa, m.nrp, m.nama AS m_nama, m.email, m.tgl_lahir, " +
                "pr.id_program, pr.nama AS program_nama, " +
                "pro.id_prodi, pro.nama AS prodi_nama, " +
                "k.id_kegiatan, k.nama AS kegiatan_nama_asli, k.kategori AS kegiatan_kategori, " +
                "c.id_club, c.nama AS club_nama " +
                "FROM peserta_kegiatan p " +
                "JOIN mahasiswa m ON p.id_mahasiswa = m.id_mahasiswa " +
                "LEFT JOIN program pr ON m.id_program = pr.id_program " +
                "LEFT JOIN prodi pro ON m.id_prodi = pro.id_prodi " +
                "JOIN kegiatan k ON p.id_kegiatan = k.id_kegiatan " +
                "LEFT JOIN club c ON k.id_club = c.id_club " +
                "WHERE p.id_kegiatan = ? " +
                "ORDER BY m.nama";
        List<PesertaKegiatan> list = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, kegiatanObj.getIdKegiatan());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPesertaKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat mencari peserta berdasarkan kegiatan: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<PesertaKegiatan> findByMahasiswa(Mahasiswa mahasiswaObj) throws Exception {
        // Memilih 'p.id_peserta'
        String sql = "SELECT p.id_peserta, p.status_sertifikat, p.nomor_sertifikat, p.tanggal_terbit_sertifikat, " +
                "m.id_mahasiswa, m.nrp, m.nama AS m_nama, m.email, m.tgl_lahir, " +
                "pr.id_program, pr.nama AS program_nama, " +
                "pro.id_prodi, pro.nama AS prodi_nama, " +
                "k.id_kegiatan, k.nama AS kegiatan_nama_asli, k.kategori AS kegiatan_kategori, " +
                "c.id_club, c.nama AS club_nama " +
                "FROM peserta_kegiatan p " +
                "JOIN mahasiswa m ON p.id_mahasiswa = m.id_mahasiswa " +
                "LEFT JOIN program pr ON m.id_program = pr.id_program " +
                "LEFT JOIN prodi pro ON m.id_prodi = pro.id_prodi " +
                "JOIN kegiatan k ON p.id_kegiatan = k.id_kegiatan " +
                "LEFT JOIN club c ON k.id_club = c.id_club " +
                "WHERE p.id_mahasiswa = ? " +
                "ORDER BY k.nama";
        List<PesertaKegiatan> list = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, mahasiswaObj.getIdMahasiswa());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPesertaKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat mencari peserta berdasarkan mahasiswa: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public PesertaKegiatan findByMahasiswaAndKegiatan(Mahasiswa mahasiswaObj, Kegiatan kegiatanObj) throws Exception {
        // Memilih 'p.id_peserta'
        String sql = "SELECT p.id_peserta, p.status_sertifikat, p.nomor_sertifikat, p.tanggal_terbit_sertifikat, " +
                "m.id_mahasiswa, m.nrp, m.nama AS m_nama, m.email, m.tgl_lahir, " +
                "pr.id_program, pr.nama AS program_nama, " +
                "pro.id_prodi, pro.nama AS prodi_nama, " +
                "k.id_kegiatan, k.nama AS kegiatan_nama_asli, k.kategori AS kegiatan_kategori, " +
                "c.id_club, c.nama AS club_nama " +
                "FROM peserta_kegiatan p " +
                "JOIN mahasiswa m ON p.id_mahasiswa = m.id_mahasiswa " +
                "LEFT JOIN program pr ON m.id_program = pr.id_program " +
                "LEFT JOIN prodi pro ON m.id_prodi = pro.id_prodi " +
                "JOIN kegiatan k ON p.id_kegiatan = k.id_kegiatan " +
                "LEFT JOIN club c ON k.id_club = c.id_club " +
                "WHERE p.id_mahasiswa = ? AND p.id_kegiatan = ? " +
                "ORDER BY k.nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, mahasiswaObj.getIdMahasiswa());
            stmt.setObject(2, kegiatanObj.getIdKegiatan());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return (mapRowToPesertaKegiatan(rs));
                }
            }
        } catch (SQLException e) {
            AlertNotification.showError("Kesalahan saat mencari peserta berdasarkan mahasiswa dan kegiatan: " + e.getMessage());
            throw e;
        }
        return null;
    }
}
