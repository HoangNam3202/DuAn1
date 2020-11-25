package com.example.tinnhn;

public class FriendsRequest {
    public String idKey;
    public String idTaiKhoan;
    public String tenTaiKhoan;
    public String email;
    public String diaChi;
    public String hinhDaiDien;
    public String EmailUser;

    public FriendsRequest(String idKey, String idTaiKhoan, String tenTaiKhoan, String email, String diaChi, String hinhDaiDien, String emailUser) {
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
