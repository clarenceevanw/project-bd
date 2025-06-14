CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabel Prodi
CREATE TABLE Prodi (
    ID_Prodi UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Nama VARCHAR(255) NOT NULL
);

-- Tabel Program
CREATE TABLE Program (
    ID_Program UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Nama VARCHAR(255) NOT NULL,
    ID_Prodi UUID NULL,
    FOREIGN KEY (ID_Prodi) REFERENCES Prodi(ID_Prodi) ON DELETE CASCADE
);

-- Tabel Mahasiswa
CREATE TABLE Mahasiswa (
    ID_Mahasiswa UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    NRP VARCHAR(9) UNIQUE NOT NULL,
    Nama VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    ID_Program UUID NULL,
    ID_Prodi UUID NOT NULL,
    FOREIGN KEY (ID_Program) REFERENCES Program(ID_Program) ON DELETE SET NULL,
    FOREIGN KEY (ID_Prodi) REFERENCES Prodi(ID_Prodi) ON DELETE CASCADE
);

-- Tabel Kategori
CREATE TABLE Kategori (
    ID_Kategori UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Nama VARCHAR(255) NOT NULL
);

-- Tabel Club
CREATE TABLE Club (
    ID_Club UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ID_Kategori UUID NOT NULL,
    Nama VARCHAR(255) NOT NULL,
    Deskripsi TEXT,
    Tahun_Berdiri INTEGER,
    FOREIGN KEY (ID_Kategori) REFERENCES Kategori(ID_Kategori) ON DELETE CASCADE
    -- KOREKSI: Menghapus koma (,) yang tidak perlu di akhir definisi tabel.
);

-- Tabel Keanggotaan
CREATE TABLE Keanggotaan (
    ID_Keanggotaan UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ID_Mahasiswa UUID NOT NULL,
    ID_Club UUID NOT NULL,
    Peran VARCHAR(50) DEFAULT 'Anggota' CHECK(Peran IN ('Anggota', 'Pengurus')),
    Tanggal_Bergabung DATE NOT NULL,
    Status VARCHAR(20) DEFAULT 'Aktif' CHECK (Status IN ('Aktif', 'Tidak Aktif')),
    FOREIGN KEY (ID_Mahasiswa) REFERENCES Mahasiswa(ID_Mahasiswa) ON DELETE CASCADE,
    FOREIGN KEY (ID_Club) REFERENCES Club(ID_Club) ON DELETE CASCADE
);

-- Tabel Jenis_Kegiatan
CREATE TABLE Jenis_Kegiatan (
    ID_Jenis_Kegiatan UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Nama VARCHAR(100) NOT NULL
);

-- Tabel Kegiatan
CREATE TABLE Kegiatan (
    ID_Kegiatan UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Nama VARCHAR(255) NOT NULL,
    Link_Dokumentasi VARCHAR(500),
    ID_Club UUID NOT NULL,
    ID_Jenis_Kegiatan UUID NOT NULL,
    kategori VARCHAR(255) DEFAULT 'Rutin' NOT NULL CHECK(kategori IN ('Rutin', 'Eksternal')),
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE NOT NULL,
    publish BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (ID_Club) REFERENCES Club(ID_Club) ON DELETE CASCADE,
    FOREIGN KEY (ID_Jenis_Kegiatan) REFERENCES Jenis_Kegiatan(ID_Jenis_Kegiatan) ON DELETE CASCADE
);

-- Tabel Jadwal_Kegiatan
CREATE TABLE Jadwal_Kegiatan (
    ID_Jadwal_Kegiatan UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ID_Kegiatan UUID NOT NULL,
    Waktu_Kegiatan TIMESTAMP NOT NULL,
    Lokasi_Kegiatan VARCHAR(200),
    FOREIGN KEY (ID_Kegiatan) REFERENCES Kegiatan(ID_Kegiatan) ON DELETE CASCADE
);

-- Tabel Peserta_Kegiatan
CREATE TABLE Peserta_Kegiatan (
    ID_Peserta UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ID_Mahasiswa UUID NOT NULL,
    ID_Kegiatan UUID NOT NULL,
    Status_Sertifikat VARCHAR(20) DEFAULT 'Ada' CHECK (Status_Sertifikat IN ('Ada', 'Tidak')),
    Nomor_Sertifikat VARCHAR(50) NULL,
    Tanggal_Terbit_Sertifikat DATE NULL,
    FOREIGN KEY (ID_Mahasiswa) REFERENCES Mahasiswa(ID_Mahasiswa) ON DELETE CASCADE,
    FOREIGN KEY (ID_Kegiatan) REFERENCES Kegiatan(ID_Kegiatan) ON DELETE CASCADE
);

-- Tabel Peserta_Jadwal_Kegiatan
CREATE TABLE Peserta_Jadwal_Kegiatan (
    ID_Peserta_Jadwal_Kegiatan UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ID_Peserta UUID NOT NULL,
    ID_Jadwal_Kegiatan UUID NOT NULL,
    Status_Kehadiran VARCHAR(20) NOT NULL DEFAULT 'Tidak Hadir' CHECK (Status_Kehadiran IN ('Hadir', 'Tidak Hadir', 'Izin')),
    FOREIGN KEY (ID_Peserta) REFERENCES Peserta_Kegiatan(ID_Peserta) ON DELETE CASCADE,
    FOREIGN KEY (ID_Jadwal_Kegiatan) REFERENCES Jadwal_Kegiatan(ID_Jadwal_Kegiatan) ON DELETE CASCADE
);