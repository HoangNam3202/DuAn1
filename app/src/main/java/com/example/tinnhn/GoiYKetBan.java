package com.example.tinnhn;

public class GoiYKetBan {
    public String idTaiKhoan;
    public String tenTaiKhoan;
    public String email;
    public String diaChi;
    public String hinhDaiDien;

    public GoiYKetBan(String idTaiKhoan, String tenTaiKhoan, String email, String diaChi,  String hinhDaiDien) {
        this.idTaiKhoan = idTaiKhoan;
        this.tenTaiKhoan = tenTaiKhoan;
        this.email = email;
        this.diaChi = diaChi;
        this.hinhDaiDien = hinhDaiDien;
    }

    public GoiYKetBan() {
    }
}
