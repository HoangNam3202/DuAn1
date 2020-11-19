package com.example.tinnhn.taikhoan;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class DBFirebase {
    DatabaseReference databaseReference;

    public void KhoiTaoFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void ThemTaiKhoan(TaiKhoan taiKhoan) {
        KhoiTaoFirebase();
        databaseReference.child("TaiKhoan").push().setValue(taiKhoan);
    }

    public ArrayList<TaiKhoan> LayDanhSachTaiKhoan() {
        KhoiTaoFirebase();
        final ArrayList<TaiKhoan> taiKhoans = new ArrayList<>();
        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                taiKhoans.add(taiKhoan);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return taiKhoans;
    }

    public void ThemTinhThanh(TinhThanh tinhThanh) {
        KhoiTaoFirebase();
        databaseReference.child("TinhThanh").push().setValue(tinhThanh);
    }


    public void DoiMatKhau(String tenTaiKhoan, String email, String matKhau) {
        KhoiTaoFirebase();
        //ok
    }

    public ArrayList<TinhThanh> LayDanhSachTinhThanh() {
        KhoiTaoFirebase();
        final ArrayList<TinhThanh> tinhThanhs = new ArrayList<>();
        databaseReference.child("TinhThanh").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TinhThanh tinhThanh = snapshot.getValue(TinhThanh.class);
                tinhThanhs.add(tinhThanh);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return tinhThanhs;
    }
}
