package com.example.tinnhn;

public class ThongBao {
    public String IDkey;
    public String message_User;
    public String emailNguoiNhan;
    public String email_User;
    public String tenNguoiGui;
    public String tenUser;

    public ThongBao(String IDkey, String message_User, String emailNguoiNhan, String email_User, String tenNguoiGui, String tenUser) {
        this.IDkey = IDkey;
        this.message_User = message_User;
        this.emailNguoiNhan = emailNguoiNhan;
        this.email_User = email_User;
        this.tenNguoiGui = tenNguoiGui;
        this.tenUser = tenUser;
    }

    public ThongBao() {
    }
}
