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
    // Hiện hình lên RecyclerView
    private String TAG = "GroupHoiThoaiActivity";
    private ArrayList<String> mNames = new ArrayList<>();
    private String idkey = "";
    private Call call;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_hoi_thoai);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        grplist = findViewById(R.id.list_HoithoaiGroup);
        Button send = findViewById(R.id.btbGuiGroup);
        EditText noidungtn = findViewById(R.id.edtNoiDungGroup);
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String email = sharedPreferences.getString("tenTaiKhoan", "");
        scrollMyListViewToBottom();
        //intent
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            j = (String) b.get("idgroup");
//            Toast.makeText(this, j, Toast.LENGTH_SHORT).show();
        }
        setTitle(j);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ArrayList<Group> hoiThoaiArrayList = new ArrayList<>();
        final ArrayList<Group> forArr = new ArrayList<>();
        groupAdapter = new GroupAdapter(GroupHoiThoaiActivity.this, R.layout.list_tin_nhan_item, hoiThoaiArrayList);
        grplist.setAdapter(groupAdapter);

//end intent
//        mDatabase.child("HoiThoaiGroup").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Group group = snapshot.getValue(Group.class);
//                forArr.clear();
//                forArr.add(new Group(group.Email, group.message, group.IdGroup));
//
//                for (int i = 0; i < forArr.size(); i++) {
//                    if (forArr.get(i).IdGroup.equals(j)) {
//                        hoiThoaiArrayList.add(new Group(forArr.get(i).Email, forArr.get(i).message, forArr.get(i).IdGroup));
//                    }
//
//                }
//                groupAdapter.notifyDataSetChanged();
//                scrollMyListViewToBottom();
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group group = new Group(email, noidungtn.getText().toString(), j);
                mDatabase.child("HoiThoaiGroup").push().setValue(group);
                noidungtn.setText("");
            }
        });


//ham do hinh
        initRecyclerView();
        emailNguoiDung = sharedPreferences.getString("tenTaiKhoan", "");
//        Toast.makeText(this, "" + emailNguoiDung, Toast.LENGTH_SHORT).show();
        if (j.equals("Chat Room 1")) idGroup = "Chatroom1";
        if (j.equals("Chat Room 2")) idGroup = "Chatroom2";
        mNames.clear();

        mDatabase.child("GroupGoiDien" + idGroup).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email = snapshot.getValue().toString();
                if (email.equals(emailNguoiDung)) ktraTrung = true;
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
        //end
        RefreshAdapterHinhGoiDien();
    }

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


    public void hamthemlistview() {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.callgroup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.groupchat) {
            //đang nhap sinchclient o day = UserEmail sau do goi cho EmailNguoiGui
            if (getGiaodiendichvu().isStarted()) {
                Hamchuyenidgroupquagroupcall();
            } else {
                Toast.makeText(this, "Service is not start", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void Hamchuyenidgroupquagroupcall() {
        mNames.clear();
        if (!ktraTrung) {
            mDatabase.child("GroupGoiDien" + idGroup).push().setValue(emailNguoiDung);
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
        call = getGiaodiendichvu().callGroup(j);


        // hiện hình từ FB lên RecyclerView
//        initImageBitmaps();
//        initRecyclerView();
//        đang dừng ở việc thêm hình vô chát room;
//        String username="idgroup";
//
//        Intent callScreen = new Intent(this, CuocGoi_Screen.class);
//        callScreen.putExtra(SinchServices.CALL_ID,username);
//        startActivity(callScreen);

//        Intent mainActivity = new Intent(this, Dialer.class);
//        startActivity(mainActivity);
    }

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


    private void scrollMyListViewToBottom() {
        grplist.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                grplist.setSelection(groupAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (call != null) call.hangup();
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

    @Override
    protected void onDestroy() {

//        mDatabase.child("GroupGoiDien" + idGroup).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                String email = snapshot.getValue().toString();
//                if (email.equals(emailNguoiDung)) {
//                    String idKey = snapshot.getKey();
//                    mDatabase.child("GroupGoiDien" + idGroup).child(idKey).removeValue();
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        super.onDestroy();
    }
}