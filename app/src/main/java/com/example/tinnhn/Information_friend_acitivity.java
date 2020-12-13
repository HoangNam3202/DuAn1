package com.example.tinnhn;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.tinnhn.Call.BaseActivity;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Information_friend_acitivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_friend);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView TenUser_setting = findViewById(R.id.TenUser_setting);
        TextView EmailUser_setting = findViewById(R.id.EmailUser_setting);
        TextView DiaChiUser_setting = findViewById(R.id.DiaChiUser_setting);
        TextView SDTUser_setting = findViewById(R.id.SDTUser_setting);
        ImageView imgAnh_User_setting = findViewById(R.id.imgAnh_User_setting);

        Intent intent = getIntent();
        String EmailFriend = intent.getStringExtra("EmailNguoiGui");

        mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getEmail().equals(EmailFriend)) {
                    String urlHinh = taiKhoan.getHinhDaiDien();
//                    new DownloadImageTask(imgAnh_User_setting).execute(urlHinh);
                    Glide.with(Information_friend_acitivity.this).asBitmap().load(urlHinh).into(imgAnh_User_setting);
                    TenUser_setting.setText(taiKhoan.tenTaiKhoan);
                    EmailUser_setting.setText(taiKhoan.email);
                    DiaChiUser_setting.setText(taiKhoan.diaChi);
                    SDTUser_setting.setText(taiKhoan.soDienThoai);
                }
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
    }
}
