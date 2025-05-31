package com.project.bd.app.projectbd.DAO;

import com.project.bd.app.projectbd.Model.Prodi;
import com.project.bd.app.projectbd.Model.Program;
import com.project.bd.app.projectbd.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProgramDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public List<Program> findAll() throws Exception {
        List<Program> list = new ArrayList<>();
        String sql = "SELECT p.id_program, p.nama AS program_nama, " +
                "pr.id_prodi, pr.nama AS prodi_nama " +
                "FROM program p " +
                "JOIN prodi pr ON p.id_prodi = pr.id_prodi " +
                "ORDER BY p.nama";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProgram(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Program findById(UUID idProgram) throws Exception {
        String sql = "SELECT p.id_program, p.nama AS program_nama, " +
                "pr.id_prodi, pr.nama AS prodi_nama " +
                "FROM program p " +
                "JOIN prodi pr ON p.id_prodi = pr.id_prodi " +
                "WHERE p.id_program = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, idProgram);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProgram(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Program mapResultSetToProgram(ResultSet rs) throws Exception {
        Prodi prodi = new Prodi();
        prodi.setIdProdi(rs.getObject("id_prodi", UUID.class));
        prodi.setNama(rs.getString("prodi_nama"));

        Program program = new Program();
        program.setIdProgram(rs.getObject("id_program", UUID.class));
        program.setNama(rs.getString("program_nama"));
        program.setProdi(prodi);

        return program;
    }
}
