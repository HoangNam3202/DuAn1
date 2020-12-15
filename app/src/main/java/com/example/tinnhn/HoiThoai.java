package com.example.tinnhn;

public class HoiThoai {
    public String message_User;
    public String emailNguoiNhan;
    public String email_User;

    public HoiThoai() {

    }

    public String getMessage_User() {
        return message_User;
    }

    public void setMessage_User(String message_User) {
        this.message_User = message_User;
    }

    public String getEmailNguoiNhan() {
        return emailNguoiNhan;
    }

    public void setEmailNguoiNhan(String emailNguoiNhan) {
        this.emailNguoiNhan = emailNguoiNhan;
    }

    public String getEmail_User() {
        return email_User;
    }

    public void setEmail_User(String email_User) {
        this.email_User = email_User;
    }

    public HoiThoai(String message_User, String emailNguoiNhan, String email_User) {
        this.message_User = message_User;
        this.emailNguoiNhan = emailNguoiNhan;
        this.email_User = email_User;
    }
}
