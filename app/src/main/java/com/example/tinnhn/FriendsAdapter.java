package com.example.tinnhn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Friends> friendsList;
    private DatabaseReference mDatabase;
    String idKeyXoa;

    public FriendsAdapter(Context context, int layout, List<Friends> friendsList) {
        this.context = context;
        this.layout = layout;
        this.friendsList = friendsList;
    }

    @Override
    public int getCount() {
        return friendsList.size();
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

        TextView tvTenFriends = view.findViewById(R.id.tvTenFriends);

        Friends friends = friendsList.get(i);

        tvTenFriends.setText(friends.tenTaiKhoan);
        TextView btnUnfriend = view.findViewById(R.id.btnUnfriend);
        btnUnfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you wanna unfriend "+friends.tenTaiKhoan+" ?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("BanBe").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                ArrayList<Friends> friendsArrayList = new ArrayList<>();
                                Friends friends1 = snapshot.getValue(Friends.class);
                                friendsArrayList.clear();
                                friendsArrayList.add(friends1);
                                for(int x= 0; x < friendsArrayList.size(); x++){
                                    if(friendsArrayList.get(x).EmailUser.equals(friends.email) && friendsArrayList.get(x).email.equals(friends.EmailUser))
                                    {
                                        idKeyXoa = snapshot.getKey();
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
//                        Toast.makeText(context, ""+idKeyXoa, Toast.LENGTH_SHORT).show();

//                        mDatabase.child("BanBe").child(friends.idKeyFriend).removeValue();
//                        mDatabase.child("BanBe").child(idKeyXoa).removeValue();
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.create().show();
            }
        });
        return view;
    }
}