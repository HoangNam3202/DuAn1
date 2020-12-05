package com.example.tinnhn.Call;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.tinnhn.Call.HienHinhVaoRecyclerView.RecyclerViewAdapter;
import com.example.tinnhn.Friends;
import com.example.tinnhn.HoiThoai;
import com.example.tinnhn.HoiThoaiActivity;
import com.example.tinnhn.HoiThoaiAdapter;
import com.example.tinnhn.MainActivity;
import com.example.tinnhn.MessageAdapter;
import com.example.tinnhn.R;
import com.example.tinnhn.TinNhanHienThi;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.calling.Call;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Objects;

public class GroupHoiThoaiActivity extends BaseActivity {
    public static GroupAdapter groupAdapter;
    private boolean ktraTrung = false;
    private DatabaseReference mDatabase;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ListView grplist;
    private String j;
    private String idGroup = "";
    private String emailNguoiDung;
    private String TAG = "GroupHoiThoaiActivity";
    private ArrayList<String> mNames = new ArrayList<>();
    private String idkey = "";
    private Call call;
    ImageButton endcall;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_hoi_thoai);
//ánh xạ
        mDatabase = FirebaseDatabase.getInstance().getReference();
        grplist = findViewById(R.id.list_HoithoaiGroup);
        Button send = findViewById(R.id.btbGuiGroup);
        EditText noidungtn = findViewById(R.id.edtNoiDungGroup);
        endcall=findViewById(R.id.endcall);
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String email = sharedPreferences.getString("tenTaiKhoan", "");
        endcall.setEnabled(false);
        //end ánh xạ

        //đưa list view xuống cuối
        scrollMyListViewToBottom();
        //end đưa listview xuống cuối


        //intent lấy idgroup
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            j = (String) b.get("idgroup");
        }
        setTitle(j);//đặt title toolbar
        //end intent

        //set frullscreen
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //end set full screen

        //khai báo arraylist
        final ArrayList<Group> hoiThoaiArrayList = new ArrayList<>();
        final ArrayList<Group> forArr = new ArrayList<>();
        groupAdapter = new GroupAdapter(GroupHoiThoaiActivity.this, R.layout.list_tin_nhan_item, hoiThoaiArrayList);
        grplist.setAdapter(groupAdapter);
        //end arraylist

        //hàm thêm tin nhắn từ firebase vào arraylist
        mDatabase.child("HoiThoaiGroup").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Group group = snapshot.getValue(Group.class);
                forArr.clear();
                forArr.add(new Group(group.Email, group.message, group.IdGroup));

                for (int i = 0; i < forArr.size(); i++) {
                    if (forArr.get(i).IdGroup.equals(j)) {
                        hoiThoaiArrayList.add(new Group(forArr.get(i).Email, forArr.get(i).message, forArr.get(i).IdGroup));
                    }

                }
                groupAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();

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
        //end hàm thêm tin nhắn từ firebase vào arraylist

        //nút gửi tin nhắn
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!noidungtn.getText().toString().equals("")){
                    Group group = new Group(email, noidungtn.getText().toString(), j);
                    mDatabase.child("HoiThoaiGroup").push().setValue(group);
                }

                noidungtn.setText("");
            }
        });
//end nút gửi tin nhắn

        //nút dừng cuộc gọi thoại:
        endcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call != null) call.hangup();//dừng cuộc gọi từ Sinch
                endcall.setEnabled(false);//tắt nút dừng
                endcall.setImageResource(R.drawable.ic_call_end2);//đổi icon

                //hàm lấy key từ FB để xóa
                mDatabase.child("GroupGoiDien" + idGroup).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String email = snapshot.getValue().toString();
                        if (email.equals(emailNguoiDung)) {
                            idkey = snapshot.getKey();
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

                //hàm countdown
                new CountDownTimer(1300, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        mDatabase.child("GroupGoiDien" + idGroup).child(idkey).removeValue();//xóa key từ FB
                    }
                }.start();
                ktraTrung=false;//trả kiểm tra trùng về false để tránh bug không thêm hình
            }
        });


        //ham do hinh
        initRecyclerView();
        emailNguoiDung = sharedPreferences.getString("tenTaiKhoan", "");
        if (j.equals("Chat Room 1")) idGroup = "Chatroom1";
        if (j.equals("Chat Room 2")) idGroup = "Chatroom2";
        mNames.clear();

        mDatabase.child("GroupGoiDien" + idGroup).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email = snapshot.getValue().toString();
                if (email.equals(emailNguoiDung)) ktraTrung = true;//kiểm tra trùng
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
        initImageBitmaps();

        RefreshAdapterHinhGoiDien();
    }
//end hàm do hình

    //hàm refresh hình trong 3s
    private void RefreshAdapterHinhGoiDien() {
        initImageBitmaps();
        new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                adapter.notifyDataSetChanged();
                RefreshAdapterHinhGoiDien();
            }
        }.start();
    }
    //end hàm refresh hình trong 3s

//hàm sự kiện cho menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.callgroup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.groupchat) {//nút gọi cho group

            if (getGiaodiendichvu().isStarted()) {
                endcall.setEnabled(true);
                endcall.setImageResource(R.drawable.ic_call);
                Hamchuyenidgroupquagroupcall();
            } else {
                Toast.makeText(this, "dịch vụ chưa chạy", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
//end hàm sự kiện cho menu

    //hàm gọi vào voice group+ thêm hình lên FB
    private void Hamchuyenidgroupquagroupcall() {
        mNames.clear();
        if (!ktraTrung) {
            mDatabase.child("GroupGoiDien" + idGroup).push().setValue(emailNguoiDung);//thêm hình người dùng vào list trên FB
        }else{
            Toast.makeText(this, "trùng", Toast.LENGTH_SHORT).show();
        }
        new CountDownTimer(1300, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                initImageBitmaps();
                adapter.notifyDataSetChanged();
            }
        }.start();

        call = getGiaodiendichvu().callGroup(j);//hàm gọi conference của Sinch

    }

    //hàm đổ hình vào adapter
    private void initImageBitmaps() {
        mNames.clear();
        Log.d(TAG, "initImageBitmaps: initImageBitmaps");
        mDatabase.child("GroupGoiDien" + idGroup).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email = snapshot.getValue().toString();
                mNames.add(email);
                adapter.notifyDataSetChanged();
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



    RecyclerViewAdapter adapter;

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        String temp = "GroupGoiDien" + idGroup;
        adapter = new RecyclerViewAdapter(this, mNames, temp);
        recyclerView.setAdapter(adapter);
    }
    //end hàm đổ hình vào adapter

    private void scrollMyListViewToBottom() {
        grplist.post(new Runnable() {
            @Override
            public void run() {
                grplist.setSelection(groupAdapter.getCount() - 1);
            }
        });
    }
//nút quay lại
    @Override
    public void onBackPressed() {
        if (call != null) call.hangup();
        endcall.setEnabled(false);
        mDatabase.child("GroupGoiDien" + idGroup).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email = snapshot.getValue().toString();
                if (email.equals(emailNguoiDung)) {
                    idkey = snapshot.getKey();
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
        new CountDownTimer(1300, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mDatabase.child("GroupGoiDien" + idGroup).child(idkey).removeValue();
            }
        }.start();

        startActivity(new Intent(GroupHoiThoaiActivity.this, MainActivity.class));
        finish();
    }
    //end nút quay lại

    @Override
    protected void onDestroy() {
        if (call != null) call.hangup();
        endcall.setEnabled(false);
        mDatabase.child("GroupGoiDien" + idGroup).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email = snapshot.getValue().toString();
                if (email.equals(emailNguoiDung)) {
                    idkey = snapshot.getKey();
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
        new CountDownTimer(1300, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mDatabase.child("GroupGoiDien" + idGroup).child(idkey).removeValue();
            }
        }.start();
        super.onDestroy();
    }
}