package com.example.tinnhn;

public class Message {
    public int idTaiKhoan;
    public String tenTaiKhoan;
    public String email;
    public String diaChi;
    public int hinhDaiDien;
    public String EmailUser;
    public String message_User;

    public Message(int idTaiKhoan, String tenTaiKhoan, String email, String diaChi, int hinhDaiDien, String emailUser, String message_User) {
        this.idTaiKhoan = idTaiKhoan;
        this.tenTaiKhoan = tenTaiKhoan;
        this.email = email;
        this.diaChi = diaChi;
        this.hinhDaiDien = hinhDaiDien;
        EmailUser = emailUser;
        this.message_User = message_User;
    }

    public Message() {
    }
}
