package com.example.tinnhn.taikhoan;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DBFirebase {
    DatabaseReference databaseReference;

    public void ThemTaiKhoan(TaiKhoan taiKhoan) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TaiKhoan").push().setValue(taiKhoan);
    }
    public ArrayList<TaiKhoan> LayDanhSachTaiKhoan() {
        ArrayList<TaiKhoan> taiKhoans = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //
        return taiKhoans;
    }

}
