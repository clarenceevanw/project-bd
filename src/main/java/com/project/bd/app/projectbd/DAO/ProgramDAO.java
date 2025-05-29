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
        String sql = "SELECT * FROM program ORDER BY nama";
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
        String sql = "SELECT * FROM program WHERE id_program = ?";
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
        Program program = new Program();
        UUID idProdi = rs.getObject("id_prodi", UUID.class);
        Prodi prodi = new ProdiDAO().findById(idProdi);
        program.setIdProgram(rs.getObject("id_program", UUID.class));
        program.setProdi(prodi);
        program.setNama(rs.getString("nama"));
        return program;
    }
}
