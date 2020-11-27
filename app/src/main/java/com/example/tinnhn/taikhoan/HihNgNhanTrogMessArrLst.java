package com.example.tinnhn.taikhoan;

public class HihNgNhanTrogMessArrLst {
    public String emailNguoiNhan;
    public String urlHinhNguoiNhan;

    public HihNgNhanTrogMessArrLst() {
    }

    public HihNgNhanTrogMessArrLst(String emailNguoiNhan, String urlHinhNguoiNhan) {
        this.emailNguoiNhan = emailNguoiNhan;
        this.urlHinhNguoiNhan = urlHinhNguoiNhan;
    }

    public String getEmailNguoiNhan() {
        return emailNguoiNhan;
    }

    public void setEmailNguoiNhan(String emailNguoiNhan) {
        this.emailNguoiNhan = emailNguoiNhan;
    }

    public String getUrlHinhNguoiNhan() {
        return urlHinhNguoiNhan;
    }

    public void setUrlHinhNguoiNhan(String urlHinhNguoiNhan) {
        this.urlHinhNguoiNhan = urlHinhNguoiNhan;
    }
}
