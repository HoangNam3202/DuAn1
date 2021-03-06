package com.example.tinnhn.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tinnhn.Friends;
import com.example.tinnhn.GoiYKetBan;
import com.example.tinnhn.GoiYKetBanAdapter;
import com.example.tinnhn.R;
import com.example.tinnhn.SearchFriendsActivity;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AddFriendChildFragment extends Fragment {

    private View mRoot;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String EmailDaKetBan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_add_friends_child, container, false);
        final ArrayList<GoiYKetBan> goiYKetBanArrayList = new ArrayList<>();
        final ArrayList<TaiKhoan> goiYKetBanArrayList_check = new ArrayList<>();
        final ArrayList<Friends> arrFriended_check = new ArrayList<>();

        ListView list_GoiYKetBan = mRoot.findViewById(R.id.list_GoiYKetBan);
        final GoiYKetBanAdapter goiYKetBanAdapter = new GoiYKetBanAdapter(getActivity(), R.layout.list_goi_y_item, goiYKetBanArrayList);
        list_GoiYKetBan.setAdapter(goiYKetBanAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getContext().getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FloatingActionButton btnThemBanBangEmail = mRoot.findViewById(R.id.fab);

        btnThemBanBangEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchFriendsActivity.class);
                startActivity(intent);
            }
        });
        String EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        String DiaChiUser = sharedPreferences.getString("DiaChiUser", "");

        mDatabase.child("BanBe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Friends friends1 = snapshot.getValue(Friends.class);
                arrFriended_check.clear();
                arrFriended_check.add(friends1);
                for (int i = 0; i < arrFriended_check.size(); i++) {
                    if (arrFriended_check.get(i).EmailUser.equals(EmailUser)) {
                        EmailDaKetBan = arrFriended_check.get(i).email;
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Friends friends = snapshot.getValue(Friends.class);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan goiYKetBan = snapshot.getValue(TaiKhoan.class);
                goiYKetBanArrayList_check.clear();
                goiYKetBanArrayList_check.add(goiYKetBan);
                for (int i = 0; i < goiYKetBanArrayList_check.size(); i++) {
                    if (goiYKetBanArrayList_check.get(i).diaChi.contains(DiaChiUser) && !goiYKetBanArrayList_check.get(i).email.equals(EmailUser) && !goiYKetBanArrayList_check.get(i).email.equals(EmailDaKetBan)) {
                        goiYKetBanArrayList.add(new GoiYKetBan(goiYKetBanArrayList_check.get(i).idTaiKhoan, goiYKetBanArrayList_check.get(i).tenTaiKhoan, goiYKetBanArrayList_check.get(i).email, goiYKetBanArrayList_check.get(i).diaChi, goiYKetBanArrayList_check.get(i).hinhDaiDien));
                    }
                }
                goiYKetBanAdapter.notifyDataSetChanged();
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

        return mRoot;
    }
}
