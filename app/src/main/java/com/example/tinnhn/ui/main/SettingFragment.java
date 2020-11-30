package com.example.tinnhn.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tinnhn.R;
import com.example.tinnhn.taikhoan.DownloadImageTask;
import com.example.tinnhn.taikhoan.LoginActivity;
import com.example.tinnhn.taikhoan.QuenMatKhauActivity;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {

    private View mRoot;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String EmailUser, TenUser, urlHinh;
    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_setting, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Button btnDangXuat = mRoot.findViewById(R.id.btnDangXuat);
        sharedPreferences = getContext().getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        TenUser = sharedPreferences.getString("tenUser", "");
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        ImageView imgAnh_User_setting = mRoot.findViewById(R.id.imgAnh_User_setting);
        TextView TenUser_setting = mRoot.findViewById(R.id.TenUser_setting);
        TextView EmailUser_setting = mRoot.findViewById(R.id.EmailUser_setting);
        TextView DiaChiUser_setting = mRoot.findViewById(R.id.DiaChiUser_setting);
        TextView SDTUser_setting = mRoot.findViewById(R.id.SDTUser_setting);
        TextView Change_Pass_setting = mRoot.findViewById(R.id.Change_Pass_setting);

        mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getEmail().equals(EmailUser)) {
                    urlHinh = taiKhoan.getHinhDaiDien();
                    new DownloadImageTask(imgAnh_User_setting).execute(urlHinh);
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

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("tenTaiKhoan");
                editor.commit();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        Change_Pass_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), QuenMatKhauActivity.class);
                intent.putExtra("QMK", false);
                startActivity(intent);
            }
        });
        return mRoot;
    }
}
