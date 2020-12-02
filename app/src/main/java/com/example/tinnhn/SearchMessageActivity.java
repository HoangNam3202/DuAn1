package com.example.tinnhn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tinnhn.ui.main.PageViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchMessageActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private PageViewModel pageViewModel;
//    int vitri;
//    boolean clickcheck = false;
    private DatabaseReference mDatabase;
    public String NoiDung,TenUser;
    ArrayList<TinNhanHienThi> messageArrayList;
    MessageAdapter messageAdapter;
    String EmailUser;
    ArrayList<TinNhanHienThi> messageArrayList_Message1;
    boolean check_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_message);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        EditText tv_Search_TinNhan = findViewById(R.id.title);
        ListView list_view_Message = findViewById(R.id.list_view_Message);
        messageArrayList = new ArrayList<>();
        messageArrayList_Message1 = new ArrayList<>();
//        ArrayList<Friends> messageArrayList_check = new ArrayList<>();
//        ArrayList<HoiThoai> messageArrayList_Message = new ArrayList<>();
        messageAdapter = new MessageAdapter(SearchMessageActivity.this,R.layout.list_message_item,messageArrayList);
        list_view_Message.setAdapter(messageAdapter);
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        TenUser = sharedPreferences.getString("tenUser", "");

        list_view_Message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchMessageActivity.this, HoiThoaiActivity.class);
                if (messageArrayList.get(i).email_User.equals(EmailUser)) {
                    intent.putExtra("TenNguoiGui", messageArrayList.get(i).tenUser);
                }
                else if (messageArrayList.get(i).emailNguoiNhan.equals(EmailUser)) {
                    intent.putExtra("TenNguoiGui", messageArrayList.get(i).tenNguoiGui);
                }
                intent.putExtra("EmailNguoiGui", messageArrayList.get(i).emailNguoiNhan);
                startActivity(intent);
//                container.removeView(view);
            }
        });
        tv_Search_TinNhan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                messageArrayList.clear();
                messageAdapter.notifyDataSetChanged();
                mDatabase.child("TinNhan").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        TinNhanHienThi message = snapshot.getValue(TinNhanHienThi.class);
                        if(message.tenUser.contains(tv_Search_TinNhan.getText().toString()) && message.tenUser.equals(TenUser)
                                || message.tenUser.contains(tv_Search_TinNhan.getText().toString()) && message.tenNguoiGui.equals(TenUser)
                                || message.tenNguoiGui.contains(tv_Search_TinNhan.getText().toString()) && message.email_User.equals(EmailUser)) {
                            if(message.email_User.equals(EmailUser)){
                                String keyTinNhan = snapshot.getKey();
                                messageArrayList.add(new TinNhanHienThi(keyTinNhan,message.message_User,message.emailNguoiNhan,message.email_User,message.tenUser,message.tenNguoiGui));
                            }
                            if(message.emailNguoiNhan.equals(EmailUser)) {
                                String keyTinNhan = snapshot.getKey();
                                messageArrayList.add(new TinNhanHienThi(keyTinNhan,message.message_User,message.email_User,message.emailNguoiNhan,message.tenNguoiGui,message.tenUser));
                            }
                        }
                        check_search = true;
                        messageAdapter.notifyDataSetChanged();
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if(!check_search){
            mDatabase.child("TinNhan").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    TinNhanHienThi message = snapshot.getValue(TinNhanHienThi.class);
                    if(message.email_User.equals(EmailUser)){
                        String keyTinNhan = snapshot.getKey();
                        messageArrayList.add(new TinNhanHienThi(keyTinNhan,message.message_User,message.emailNguoiNhan,message.email_User,message.tenUser,message.tenNguoiGui));
                    }
                    if(message.emailNguoiNhan.equals(EmailUser)) {
                        String keyTinNhan = snapshot.getKey();
                        messageArrayList.add(new TinNhanHienThi(keyTinNhan,message.message_User,message.email_User,message.emailNguoiNhan,message.tenNguoiGui,message.tenUser));
                    }

                    messageAdapter.notifyDataSetChanged();
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
        }
    }
}