package com.example.tinnhn;

public class LoadBanBe {
    public String idKeyFriend;
    public String idTaiKhoan;
    public String tenTaiKhoan;
    public String email;
    public String diaChi;
    public String hinhDaiDien;
    public String EmailUser;

    public LoadBanBe(String idKeyFriend, String idTaiKhoan, String tenTaiKhoan, String email, String diaChi, String hinhDaiDien, String emailUser) {
        this.idKeyFriend = idKeyFriend;
        this.idTaiKhoan = idTaiKhoan;
        this.tenTaiKhoan = tenTaiKhoan;
        this.email = email;
        this.diaChi = diaChi;
        this.hinhDaiDien = hinhDaiDien;
        EmailUser = emailUser;
    }

    public LoadBanBe() {
    }
}
