package com.example.tinnhn.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tinnhn.Friends;
import com.example.tinnhn.FriendsAdapter;
import com.example.tinnhn.HoiThoaiActivity;
import com.example.tinnhn.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FriendChildFragment extends Fragment {

    private View mRoot;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_friends_child,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getContext().getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ListView list_friends_child = mRoot.findViewById(R.id.list_friends_child);
        final ArrayList<Friends> arrFriends = new ArrayList<>();
        final ArrayList<Friends> arrFriends_check = new ArrayList<>();
        final FriendsAdapter friendsAdapter = new FriendsAdapter(getContext(),R.layout.list_friends_item,arrFriends);
        list_friends_child.setAdapter(friendsAdapter);
        String EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("BanBe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Friends friends = snapshot.getValue(Friends.class);
                arrFriends_check.clear();
                arrFriends_check.add(friends);
                for(int i = 0 ; i < arrFriends_check.size(); i++){
                    if(arrFriends_check.get(i).EmailUser.equals(EmailUser)){
                        arrFriends.add(new Friends(arrFriends_check.get(i).idTaiKhoan,arrFriends_check.get(i).tenTaiKhoan,arrFriends_check.get(i).email,
                                arrFriends_check.get(i).diaChi,arrFriends_check.get(i).hinhDaiDien,arrFriends_check.get(i).EmailUser));
                    }
                }
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Friends friends = snapshot.getValue(Friends.class);
                arrFriends.remove(friends);
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list_friends_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), HoiThoaiActivity.class);
                intent.putExtra("EmailNguoiGui", arrFriends.get(i).email);
                intent.putExtra("TenNguoiGui", arrFriends.get(i).tenTaiKhoan);
                startActivity(intent);
            }
        });
        return mRoot;
    }
}
