package com.example.tinnhn;

public class HoiThoai {
    public int IdTinNhan;
    public byte[] avatar_User;
    public byte[] avatar_Friend_User;
    public String message_User;
    public String message_Friend_User;
    public String emailNguoiNhan;
    public String email_User;

    public HoiThoai() {

    }

    public HoiThoai(int idTinNhan, byte[] avatar_User, byte[] avatar_Friend_User, String message_User, String message_Friend_User, String emailNguoiNhan, String email_User) {
        IdTinNhan = idTinNhan;
        this.avatar_User = avatar_User;
        this.avatar_Friend_User = avatar_Friend_User;
        this.message_User = message_User;
        this.message_Friend_User = message_Friend_User;
        this.emailNguoiNhan = emailNguoiNhan;
        this.email_User = email_User;
    }
}
