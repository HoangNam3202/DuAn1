package com.example.tinnhn.Call;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.tinnhn.HoiThoai;
import com.example.tinnhn.R;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GroupAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Group> hoiThoaiList;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    public GroupAdapter(Context context, int layout, List<Group> hoiThoaiList) {
        this.context = context;
        this.layout = layout;
        this.hoiThoaiList = hoiThoaiList;
    }


    @Override
    public int getCount() {
        return hoiThoaiList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);


        TextView tvUser_tin_nhan = view.findViewById(R.id.tvHoiThoai_User_tin_nhan);
        TextView tv_HoiThoaiBanCuaUser = view.findViewById(R.id.tv_HoiThoaiBanCuaUser);
        ImageView imgAnh_User_tin_nhan = view.findViewById(R.id.imgAnh_User_tin_nhan);
        ImageView imgAnh_Ban_Cua_User = view.findViewById(R.id.imgAnh_Ban_Cua_User);
        CardView card_view_Friend = view.findViewById(R.id.card_view);
        CardView card_view_User = view.findViewById(R.id.card_view1);
        sharedPreferences = context.getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        String urlHinhUser = sharedPreferences.getString("urlHinhDaiDien", "");

        Group group = hoiThoaiList.get(i);
        Intent intent = ((Activity) context).getIntent();

        sharedPreferences = context.getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String EmailNguoiGui = intent.getStringExtra("EmailNguoiGui");
        final String EmailUser = sharedPreferences.getString("tenTaiKhoan", "");

        if (group.Email.equals(EmailUser)) {
            tvUser_tin_nhan.setText(group.message);
            tv_HoiThoaiBanCuaUser.setVisibility(View.GONE);
            imgAnh_Ban_Cua_User.setVisibility(View.GONE);
            card_view_Friend.setVisibility(View.GONE);
            Glide.with(context).asBitmap().load(urlHinhUser).into(imgAnh_User_tin_nhan);
        }
        if (!group.Email.equals(EmailUser)) {
            tv_HoiThoaiBanCuaUser.setText(group.message);
            tvUser_tin_nhan.setVisibility(View.GONE);
            imgAnh_User_tin_nhan.setVisibility(View.GONE);
            card_view_User.setVisibility(View.GONE);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                    if (taiKhoan.getEmail().equals(group.Email)) {
                        Glide.with(context).asBitmap().load(taiKhoan.getHinhDaiDien()).into(imgAnh_Ban_Cua_User);
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

        return view;
    }
}