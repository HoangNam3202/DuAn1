package com.example.tinnhn;

public class FriendsRequest {
    public String idKey;
    public int idTaiKhoan;
    public String tenTaiKhoan;
    public String email;
    public String diaChi;
    public int hinhDaiDien;
    public String EmailUser;

    public FriendsRequest(String idKey, int idTaiKhoan, String tenTaiKhoan, String email, String diaChi, int hinhDaiDien, String emailUser) {
        this.idKey = idKey;
        this.idTaiKhoan = idTaiKhoan;
        this.tenTaiKhoan = tenTaiKhoan;
        this.email = email;
        this.diaChi = diaChi;
        this.hinhDaiDien = hinhDaiDien;
        EmailUser = emailUser;
    }

    public FriendsRequest() {
    }
}