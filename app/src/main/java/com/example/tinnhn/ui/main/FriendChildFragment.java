package com.example.tinnhn.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tinnhn.Friends;
import com.example.tinnhn.FriendsAdapter;
import com.example.tinnhn.FriendsRequest;
import com.example.tinnhn.FriendsRequestAdapter;
import com.example.tinnhn.HoiThoaiActivity;
import com.example.tinnhn.Information_friend_acitivity;
import com.example.tinnhn.MainActivity;
import com.example.tinnhn.R;
import com.example.tinnhn.TrangThai;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FriendChildFragment extends Fragment {

    private View mRoot;
    private static DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static ArrayList<Friends> arrFriends;
    public static ArrayList<Friends> arrFriends_check;
    public static ArrayList<Friends> arrFriends_check1;
    public static FriendsAdapter friendsAdapter;
    static ArrayList<FriendsRequest> arrFriendsRequests;
    static ArrayList<FriendsRequest> arrFriendsRequests_check;
    static String EmailUser;
    static ListView list_friends_request_child;
    static FriendsRequestAdapter friendsRequestAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_friends_child, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getContext().getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ListView list_friends_child = mRoot.findViewById(R.id.list_friends_child);
        arrFriends = new ArrayList<>();
        arrFriends_check = new ArrayList<>();
        arrFriends_check1 = new ArrayList<>();
        friendsAdapter = new FriendsAdapter(getContext(), R.layout.list_friends_item, arrFriends);
        list_friends_child.setAdapter(friendsAdapter);
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        GoiDanhSachBanBe();
        list_friends_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Information_friend_acitivity.class);
                intent.putExtra("EmailNguoiGui", arrFriends.get(i).email);
                intent.putExtra("TenNguoiGui", arrFriends.get(i).tenTaiKhoan);
                startActivity(intent);
            }
        });


        list_friends_request_child = mRoot.findViewById(R.id.list_friends_request_child);
        arrFriendsRequests = new ArrayList<>();
        arrFriendsRequests_check = new ArrayList<>();
        friendsRequestAdapter = new FriendsRequestAdapter(getContext(), R.layout.list_loi_moi_item, arrFriendsRequests);
        list_friends_request_child.setAdapter(friendsRequestAdapter);

        if (arrFriendsRequests.size() <= 0) {
            list_friends_request_child.setVisibility(View.GONE);
        }
        GoiLoiMoiKetBan();

        mDatabase.child("TrangThai").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TrangThai trangThai123 = snapshot.getValue(TrangThai.class);
                for (int i = 0; i < arrFriends.size(); i++) {
                    if (arrFriends.get(i).email.equals(trangThai123.Email_user)) {
                        arrFriends.clear();
                        friendsAdapter.notifyDataSetChanged();
                        GoiDanhSachBanBe();
                    }
                }
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

    public static void GoiLoiMoiKetBan() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arrFriendsRequests.clear();
        friendsRequestAdapter.notifyDataSetChanged();
        mDatabase.child("LoiMoiKetBan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FriendsRequest friendsRequest = snapshot.getValue(FriendsRequest.class);
                if (friendsRequest.EmailUser.equals(EmailUser)) {
                    String key = snapshot.getKey();
                    arrFriendsRequests.add(new FriendsRequest(key, friendsRequest.idTaiKhoan, friendsRequest.tenTaiKhoan, friendsRequest.email, friendsRequest.diaChi, friendsRequest.hinhDaiDien, friendsRequest.EmailUser));
                }
                if (arrFriendsRequests.size() <= 0) {
                    list_friends_request_child.setVisibility(View.GONE);
                } else {
                    list_friends_request_child.setVisibility(View.VISIBLE);
                }
                friendsRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                FriendsRequest friendsRequest = snapshot.getValue(FriendsRequest.class);
                if (friendsRequest.EmailUser.equals(EmailUser)) {
                    for (int i = 0; i < arrFriendsRequests.size(); i++) {
                        if (arrFriendsRequests.get(i).email.equals(friendsRequest.email)) {
                            arrFriendsRequests.remove(i);
                        }
                        friendsRequestAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void GoiDanhSachBanBe() {
        arrFriends.clear();
        friendsAdapter.notifyDataSetChanged();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("BanBe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Friends friends = snapshot.getValue(Friends.class);
                if (friends.EmailUser.equals(EmailUser)) {
                    String key_Friend = snapshot.getKey();
                    arrFriends.add(new Friends(key_Friend, friends.idTaiKhoan, friends.tenTaiKhoan, friends.email, friends.diaChi, friends.hinhDaiDien, friends.EmailUser));
                }
                String key_Friend_1 = snapshot.getKey();
                arrFriends_check1.add(new Friends(key_Friend_1, friends.idTaiKhoan, friends.tenTaiKhoan, friends.email, friends.diaChi, friends.hinhDaiDien, friends.EmailUser));
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Friends friends = snapshot.getValue(Friends.class);
                if (friends.EmailUser.equals(EmailUser)) {
                    for (int i = 0; i < arrFriends.size(); i++) {
                        if (arrFriends.get(i).email.equals(friends.email)) {
                            arrFriends.remove(i);
                        }
                        friendsAdapter.notifyDataSetChanged();
                    }
                }
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
