package com.example.tinnhn.taikhoan;

import java.io.File;

public class TaiKhoan {
    private int idTaiKhoan;
    private String tenTaiKhoan;
    private String email;
    private String matKhau;
    private String soDienThoai;
    private String diaChi;
    private int hinhDaiDien;

    public TaiKhoan(int idTaiKhoan, String tenTaiKhoan, String email, String matKhau, String soDienThoai, String diaChi, int hinhDaiDien) {
        this.idTaiKhoan = idTaiKhoan;
        this.tenTaiKhoan = tenTaiKhoan;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getHinhDaiDien() {
        return hinhDaiDien;
    }

    public void setHinhDaiDien(int hinhDaiDien) {
        this.hinhDaiDien = hinhDaiDien;
    }
}
