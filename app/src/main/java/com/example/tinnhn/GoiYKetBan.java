package com.example.tinnhn;

public class GoiYKetBan {
    public int idTaiKhoan;
    public String tenTaiKhoan;
    public String email;
    public String diaChi;
    public int hinhDaiDien;

    public GoiYKetBan(int idTaiKhoan, String tenTaiKhoan, String email, String diaChi, int hinhDaiDien) {
        this.idTaiKhoan = idTaiKhoan;
        this.tenTaiKhoan = tenTaiKhoan;
        this.email = email;
        this.diaChi = diaChi;
        this.hinhDaiDien = hinhDaiDien;
    }

    public GoiYKetBan() {
    }
}
