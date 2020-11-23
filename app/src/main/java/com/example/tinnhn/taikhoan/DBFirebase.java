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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class DBFirebase {
    public String TAG = "DBFirebase";
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

    //    public void ThemTinhThanh(TinhThanh tinhThanh) {
//        KhoiTaoFirebase();
//        databaseReference.child("TinhThanh").push().setValue(tinhThanh);
//    }
    public void ThemTinhThanh2(String tinhThanh) {
        KhoiTaoFirebase();
        databaseReference.child("TinhThanh2").push().setValue(tinhThanh);
    }

    public ArrayList<String> LayDanhSachTinhThanh2() {
        KhoiTaoFirebase();
        ArrayList<String> abc = new ArrayList<>();
        databaseReference.child("TinhThanh2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tinh = snapshot.getValue().toString();
                abc.add(tinh);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return abc;
    }

    public void DoiMatKhau(String tenTaiKhoan, String email, String matKhau) {
        KhoiTaoFirebase();
        //ok
    }

//    public ArrayList<TinhThanh> LayDanhSachTinhThanh() {
//        KhoiTaoFirebase();
//        final ArrayList<TinhThanh> tinhThanhs = new ArrayList<>();
//        databaseReference.child("TinhThanh").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                TinhThanh tinhThanh = snapshot.getValue(TinhThanh.class);
//                tinhThanhs.add(tinhThanh);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return tinhThanhs;
//    }

    public String LayKeyTaiKhoan(String email) {
        KhoiTaoFirebase();
        final String[] key = {""};
        databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        Query query = databaseReference.orderByChild("tenTaiKhoan");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d(TAG,""+snapshot.toString());
                TaiKhoan taiKhoan;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    taiKhoan = dataSnapshot.getValue(TaiKhoan.class);
                    if (taiKhoan.getTenTaiKhoan().equals(email)) {
                        key[0] = snapshot.getKey();
                        Log.d(TAG, "" + taiKhoan.getTenTaiKhoan());
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return key[0];

    }
}
