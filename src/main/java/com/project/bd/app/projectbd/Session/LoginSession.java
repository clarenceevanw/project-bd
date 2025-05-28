package com.project.bd.app.projectbd.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoginSession {
    private static LoginSession instance;

    private UUID idMahasiswa;
    private String nrp;
    private String nama;
    private String email;
    private String role;
    private LocalDateTime waktuLogin;
    private UUID idClub;

    private LoginSession() {}

    public static LoginSession getInstance() {
        if (instance == null) {
            instance = new LoginSession();
        }
        return instance;
    }

    public void setSession(UUID idMahasiswa, String nrp, String nama, String email, String role) {
        this.idMahasiswa = idMahasiswa;
        this.nrp = nrp;
        this.nama = nama;
        this.email = email;
        this.role = role;
        this.waktuLogin = LocalDateTime.now();
    }

    public void clearSession() {
        instance = null;
    }

    // Getter
    public UUID getIdMahasiswa() { return idMahasiswa; }
    public String getNrp() { return nrp; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public LocalDateTime getWaktuLogin() { return waktuLogin; }
    public UUID getIdClub() { return idClub; }
    public void setIdClub(UUID idClub) { this.idClub = idClub; }
}

