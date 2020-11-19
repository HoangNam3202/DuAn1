package com.example.tinnhn;

import android.content.Context;
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

import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GoiYKetBanAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<GoiYKetBan> goiYKetBanList;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String TenUser,DiaChiUser;
    int idUser , hinhUser ;
    public GoiYKetBanAdapter(Context context, int layout, List<GoiYKetBan> goiYKetBanList) {
        this.context = context;
        this.layout = layout;
        this.goiYKetBanList = goiYKetBanList;
    }

    @Override
    public int getCount() {
        return goiYKetBanList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = context.getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        final String EmailUser = sharedPreferences.getString("tenTaiKhoan","");
        editor = sharedPreferences.edit();

        TextView tvTenGoiY = view.findViewById(R.id.tvTenGoiY);
        TextView btnAddFriend_Goi_Y = view.findViewById(R.id.btnAddFriend_Goi_Y);
        final GoiYKetBan goiYKetBan = goiYKetBanList.get(i);

        final ArrayList<TaiKhoan> goiYKetBanArrayList_check = new ArrayList<>();

        mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                goiYKetBanArrayList_check.clear();
                goiYKetBanArrayList_check.add(taiKhoan);

                for(int i = 0;i < goiYKetBanArrayList_check.size(); i++){
                    if(goiYKetBanArrayList_check.get(i).email.equals(EmailUser)){
                        TenUser  = goiYKetBanArrayList_check.get(i).tenTaiKhoan;
                        DiaChiUser = goiYKetBanArrayList_check.get(i).diaChi;
                        idUser = goiYKetBanArrayList_check.get(i).idTaiKhoan;
                        hinhUser = goiYKetBanArrayList_check.get(i).hinhDaiDien;
                    }
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

        tvTenGoiY.setText(goiYKetBan.tenTaiKhoan);
        btnAddFriend_Goi_Y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friends friends = new Friends(goiYKetBan.idTaiKhoan,goiYKetBan.tenTaiKhoan,goiYKetBan.email,goiYKetBan.diaChi,goiYKetBan.hinhDaiDien,EmailUser);
                mDatabase.child("BanBe").push().setValue(friends);
                Friends friends1 = new Friends(idUser,TenUser,EmailUser,DiaChiUser,hinhUser,goiYKetBan.email);
                mDatabase.child("BanBe").push().setValue(friends1);
            }
        });
        return view;
    }
}