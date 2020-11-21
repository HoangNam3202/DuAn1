package com.example.tinnhn.taikhoan;

public class TinhThanh {
    int stt;
    String tenTinh;

    public TinhThanh(int stt, String tenTinh) {
        this.stt = stt;
        this.tenTinh = tenTinh;
    }

    public TinhThanh() {
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getTenTinh() {
        return tenTinh;
    }

    public void setTenTinh(String tenTinh) {
        this.tenTinh = tenTinh;
    }
}
