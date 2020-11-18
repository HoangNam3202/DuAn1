package com.example.tinnhn.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tinnhn.GoiYKetBan;
import com.example.tinnhn.GoiYKetBanAdapter;
import com.example.tinnhn.R;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddFriendChildFragment extends Fragment {

    private View mRoot;
    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_add_friends_child,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final ArrayList<GoiYKetBan> goiYKetBanArrayList = new ArrayList<>();
        final ArrayList<TaiKhoan> goiYKetBanArrayList_check = new ArrayList<>();
        ListView list_GoiYKetBan = mRoot.findViewById(R.id.list_GoiYKetBan);
        final GoiYKetBanAdapter goiYKetBanAdapter = new GoiYKetBanAdapter(getActivity(),R.layout.list_goi_y_item,goiYKetBanArrayList);
        list_GoiYKetBan.setAdapter(goiYKetBanAdapter);

        mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan goiYKetBan = snapshot.getValue(TaiKhoan.class);
                goiYKetBanArrayList_check.clear();
                goiYKetBanArrayList_check.add(goiYKetBan);
                for(int i = 0;i < goiYKetBanArrayList_check.size(); i++){
                    if(goiYKetBanArrayList_check.get(i).diaChi.equals("daklak")){
                        goiYKetBanArrayList.add(new GoiYKetBan(goiYKetBanArrayList_check.get(i).idTaiKhoan,goiYKetBanArrayList_check.get(i).tenTaiKhoan,
                                goiYKetBanArrayList_check.get(i).email,goiYKetBanArrayList_check.get(i).diaChi,goiYKetBanArrayList_check.get(i).hinhDaiDien));
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
