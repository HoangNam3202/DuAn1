package com.example.tinnhn.taikhoan;

import java.io.File;

public class TaiKhoan {
    private int idTaiKhoan;
    private String tenTaiKhoan;
    private String matKhau;
    private String soDienThoai;
    private String diaChi;
    private File hinhDaiDien;

    public TaiKhoan(int idTaiKhoan, String tenTaiKhoan, String matKhau, String soDienThoai, String diaChi, File hinhDaiDien) {
        this.idTaiKhoan = idTaiKhoan;
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.hinhDaiDien = hinhDaiDien;
    }

    public int getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(int idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public File getHinhDaiDien() {
        return hinhDaiDien;
    }

    public void setHinhDaiDien(File hinhDaiDien) {
        this.hinhDaiDien = hinhDaiDien;
    }
}
