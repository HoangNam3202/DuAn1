package com.example.tinnhn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.tinnhn.taikhoan.DownloadImageTask;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.example.tinnhn.ui.main.FriendChildFragment.GoiDanhSachBanBe;
import static com.example.tinnhn.ui.main.FriendChildFragment.arrFriends_check1;

public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Friends> friendsList;
    private DatabaseReference mDatabase;
    String idKeyXoa;
    String urlHinh;

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
        TextView tvDiaChiFriend = view.findViewById(R.id.tvDiaChiFriend);
        ImageView imgAnh_Friends = view.findViewById(R.id.imgAnh_Friends);
        Friends friends = friendsList.get(i);

        tvTenFriends.setText(friends.tenTaiKhoan);
        tvDiaChiFriend.setText(friends.diaChi);
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
        //
        mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getEmail().equals(friends.email)) {
                    urlHinh = taiKhoan.getHinhDaiDien();
//                    new DownloadImageTask(imgAnh_Friends).execute(urlHinh);
                    Glide.with(context).asBitmap().load(urlHinh).into(imgAnh_Friends);
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
        //

        return view;
    }

}