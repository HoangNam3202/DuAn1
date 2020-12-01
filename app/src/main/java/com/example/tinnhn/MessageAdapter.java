package com.example.tinnhn;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tinnhn.taikhoan.DownloadImageTask;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<TinNhanHienThi> messageList;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String EmailUser, EmailNguoiGui;
    String TAG = "MessageAdapter";
    String urlHinh = "";


    public MessageAdapter(Context context, int layout, List<TinNhanHienThi> messageList) {
        this.context = context;
        this.layout = layout;
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
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
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        String formattedDate = df.format(c.getTime());
        sharedPreferences = context.getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");

        TextView tvTenFriends = view.findViewById(R.id.tvTenFriends);
        TextView tvtinnhanFriends = view.findViewById(R.id.tvtinnhanFriends);
        TextView tvthoiGian = view.findViewById(R.id.tvthoiGian);
        ImageView imgAnh_Message = view.findViewById(R.id.imgAnh_Message);

        TinNhanHienThi message = messageList.get(i);
        //
        mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getEmail().equals(message.emailNguoiNhan)) {
                    urlHinh = taiKhoan.getHinhDaiDien();
                    new DownloadImageTask(imgAnh_Message).execute(urlHinh);
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
//        Glide.with(context).load(urlHinh).into(imgAnh_Message);
        //


        tvtinnhanFriends.setText(message.message_User);
        tvthoiGian.setText("• " + formattedDate);

        if (message.email_User.equals(EmailUser)) {
            tvTenFriends.setText(message.tenUser);
        } else if (message.emailNguoiNhan.equals(EmailUser)) {
            tvTenFriends.setText(message.tenNguoiGui);
        }
        return view;
    }
}