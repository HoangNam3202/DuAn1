package com.example.tinnhn;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FriendsRequestAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<FriendsRequest> friendsRequestsList;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String TenUser,DiaChiUser;
    int idUser , hinhUser ;
    public FriendsRequestAdapter(Context context, int layout, List<FriendsRequest> friendsRequestsList) {
        this.context = context;
        this.layout = layout;
        this.friendsRequestsList = friendsRequestsList;
    }

    @Override
    public int getCount() {
        return friendsRequestsList.size();
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

        TextView tvTenLoi_Moi = view.findViewById(R.id.tvTenLoi_Moi);
        TextView btnAddFriend_Loi_Moi = view.findViewById(R.id.btnAddFriend_Loi_Moi);
        TextView btndeleteFriend_Loi_Moi = view.findViewById(R.id.btndeleteFriend_Loi_Moi);

        FriendsRequest friendsRequest = friendsRequestsList.get(i);
        tvTenLoi_Moi.setText(friendsRequest.tenTaiKhoan);
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
        btnAddFriend_Loi_Moi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friends friends = new Friends(friendsRequest.idTaiKhoan,friendsRequest.tenTaiKhoan,friendsRequest.email,friendsRequest.diaChi,friendsRequest.hinhDaiDien,EmailUser);
                mDatabase.child("BanBe").push().setValue(friends);
                Friends friends1 = new Friends(idUser,TenUser,EmailUser,DiaChiUser,hinhUser,friendsRequest.email);
                mDatabase.child("BanBe").push().setValue(friends1);
                mDatabase.child("LoiMoiKetBan").child(friendsRequest.idKey).removeValue();
            }
        });
        btndeleteFriend_Loi_Moi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("LoiMoiKetBan").child(friendsRequest.idKey).removeValue();
            }
        });

        return view;
    }
}