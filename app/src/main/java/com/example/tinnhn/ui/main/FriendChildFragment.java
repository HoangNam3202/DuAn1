package com.example.tinnhn.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    public static  ArrayList<Friends> arrFriends;
    public static ArrayList<Friends> arrFriends_check;
    public static ArrayList<Friends> arrFriends_check1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_friends_child,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getContext().getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ListView list_friends_child = mRoot.findViewById(R.id.list_friends_child);
        arrFriends = new ArrayList<>();
        arrFriends_check = new ArrayList<>();
        arrFriends_check1 = new ArrayList<>();
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
                        String key_Friend = snapshot.getKey();
                        arrFriends.add(new Friends(key_Friend,arrFriends_check.get(i).idTaiKhoan,arrFriends_check.get(i).tenTaiKhoan,arrFriends_check.get(i).email,
                                arrFriends_check.get(i).diaChi,arrFriends_check.get(i).hinhDaiDien,arrFriends_check.get(i).EmailUser));
                    }
                    String key_Friend_1 = snapshot.getKey();
                    arrFriends_check1.add(new Friends(key_Friend_1,arrFriends_check.get(i).idTaiKhoan,arrFriends_check.get(i).tenTaiKhoan,arrFriends_check.get(i).email,
                            arrFriends_check.get(i).diaChi,arrFriends_check.get(i).hinhDaiDien,arrFriends_check.get(i).EmailUser));
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


        ListView list_friends_request_child = mRoot.findViewById(R.id.list_friends_request_child);
        final ArrayList<FriendsRequest> arrFriendsRequests = new ArrayList<>();
        final ArrayList<FriendsRequest> arrFriendsRequests_check = new ArrayList<>();
        final FriendsRequestAdapter friendsRequestAdapter = new FriendsRequestAdapter(getContext(),R.layout.list_loi_moi_item,arrFriendsRequests);
        list_friends_request_child.setAdapter(friendsRequestAdapter);

        if(arrFriendsRequests.size() <= 0){
            list_friends_request_child.setVisibility(View.GONE);
        }

        mDatabase.child("LoiMoiKetBan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FriendsRequest friendsRequest = snapshot.getValue(FriendsRequest.class);

                arrFriendsRequests_check.clear();
                arrFriendsRequests_check.add(friendsRequest);
                for(int i = 0 ; i < arrFriendsRequests_check.size(); i++){
                    if(arrFriendsRequests_check.get(i).EmailUser.equals(EmailUser)){
                        String key = snapshot.getKey();
                        arrFriendsRequests.add(new FriendsRequest(key,arrFriendsRequests_check.get(i).idTaiKhoan,arrFriendsRequests_check.get(i).tenTaiKhoan,arrFriendsRequests_check.get(i).email,
                                arrFriendsRequests_check.get(i).diaChi,arrFriendsRequests_check.get(i).hinhDaiDien,arrFriendsRequests_check.get(i).EmailUser));
                    }
                    if(arrFriendsRequests.size() <= 0){
                        list_friends_request_child.setVisibility(View.GONE);
                    }
                    else {
                        list_friends_request_child.setVisibility(View.VISIBLE);
                    }
                }
                friendsRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                friendsRequestAdapter.notifyDataSetChanged();
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
