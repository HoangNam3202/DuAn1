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
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class DBFirebase {
    DatabaseReference databaseReference;

    public void ThemTaiKhoan(TaiKhoan taiKhoan) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TaiKhoan").push().setValue(taiKhoan);
    }

    public ArrayList<TaiKhoan> LayDanhSachTaiKhoan() {
        final ArrayList<TaiKhoan> taiKhoans = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
        //
        return taiKhoans;
    }




//    public int KiemTraTrungEmail(String email) {
//        ArrayList<TaiKhoan> taiKhoans2 = new ArrayList<>();
//        taiKhoans2 = LayDanhSachTaiKhoan();
//        int ktra = 1;
//        int i = 0;
//                Log.e("qwe","qwe");
//        while (i < taiKhoans2.size()) {
//            if (email.equals(taiKhoans2.get(i).getEmail())) {
//                ktra = 2;
//            }
//            i++;
//        }
//        return ktra;
//    }
//
//    public boolean KiemTraTrungSoDienThoai(String soDienThoai) {
//        ArrayList<TaiKhoan> taiKhoans2 = new ArrayList<>();
//        taiKhoans2 = LayDanhSachTaiKhoan();
//        boolean ktra = false;
//        int i = 0;
//        while (i < taiKhoans2.size()) {
//            if (soDienThoai.equalsIgnoreCase(taiKhoans2.get(i).getSoDienThoai())) {
//                ktra = true;
//                break;
//            }
//            i++;
//        }
//        return ktra;
//    }

    public void DoiMatKhau(String tenTaiKhoan, String email, String matKhau) {
        //ok
    }
}
