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

import static com.example.tinnhn.ui.main.FriendChildFragment.GoiDanhSachBanBe;
import static com.example.tinnhn.ui.main.FriendChildFragment.arrFriends_check;
import static com.example.tinnhn.ui.main.FriendChildFragment.arrFriends_check1;

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
        view = inflater.inflate(layout, null);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView tvTenFriends = view.findViewById(R.id.tvTenFriends);

        Friends friends = friendsList.get(i);

        tvTenFriends.setText(friends.tenTaiKhoan);
        TextView btnUnfriend = view.findViewById(R.id.btnUnfriend);
        btnUnfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you wanna unfriend " + friends.tenTaiKhoan + " ?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int x = 0; x < arrFriends_check1.size(); x++) {
                            if (arrFriends_check1.get(x).EmailUser.equals(friends.email) && arrFriends_check1.get(x).email.equals(friends.EmailUser)) {
                                idKeyXoa = arrFriends_check1.get(x).idKeyFriend;
                                mDatabase.child("BanBe").child(idKeyXoa).removeValue();
                            }
                        }
                        mDatabase.child("BanBe").child(friends.idKeyFriend).removeValue();
                        GoiDanhSachBanBe();
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