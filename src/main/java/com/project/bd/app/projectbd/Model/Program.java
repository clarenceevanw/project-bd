package com.project.bd.app.projectbd.Model;

import java.util.UUID;

public class Program {
    private UUID id_program;
    private String nama;
    private Prodi prodi;

    public Program() {}

    public Program(UUID id_program, String nama, Prodi prodi) {
        this.id_program = id_program;
        this.nama = nama;
        this.prodi = prodi;
    }

    public UUID getIdProgram() {
        return id_program;
    }

    public void setIdProgram(UUID id_program) {
        this.id_program = id_program;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Prodi getProdi() {
        return prodi;
    }

    public void setProdi(Prodi prodi) {
        this.prodi = prodi;
    }
}
