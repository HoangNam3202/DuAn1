package com.example.tinnhn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class HoiThoaiActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent_Friends = getIntent();
        String TenNguoiGui = intent_Friends.getStringExtra("TenNguoiGui");

        setTitle(TenNguoiGui);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user);

        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final EditText edtNoiDung = findViewById(R.id.edtNoiDung);
        Button btnGui = findViewById(R.id.btbGui);
        ListView list_Hoithoai = findViewById(R.id.list_Hoithoai);
        final ArrayList<HoiThoai> hoiThoaiArrayList = new ArrayList<>();
        final ArrayList<HoiThoai> forArr = new ArrayList<>();
        final HoiThoaiAdapter hoiThoaiAdapter = new HoiThoaiAdapter(HoiThoaiActivity.this,R.layout.list_tin_nhan_item,hoiThoaiArrayList);
        list_Hoithoai.setAdapter(hoiThoaiAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String EmailUser = sharedPreferences.getString("tenTaiKhoan","");
        final String EmailNguoiGui = intent_Friends.getStringExtra("EmailNguoiGui");
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtNoiDung.getText().toString().equals("")){
                    final HoiThoai hoiThoai = new HoiThoai(edtNoiDung.getText().toString(), EmailNguoiGui,EmailUser);
                    mDatabase.child("HoiThoai").push().setValue(hoiThoai);
                    edtNoiDung.setText("");
                }
                else {
                    Toast.makeText(HoiThoaiActivity.this, "Message is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDatabase.child("HoiThoai").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoiThoai hThoai = snapshot.getValue(HoiThoai.class);
                forArr.clear();
                forArr.add(new HoiThoai(hThoai.message_User,hThoai.emailNguoiNhan,hThoai.email_User));
                for(int i = 0; i < forArr.size(); i++){
                    if(forArr.get(i).email_User.equals(EmailUser) && forArr.get(i).emailNguoiNhan.equals(EmailNguoiGui)){
                        hoiThoaiArrayList.add(new HoiThoai(forArr.get(i).message_User,forArr.get(i).emailNguoiNhan,forArr.get(i).email_User));
                    }
                    if(forArr.get(i).email_User.equals(EmailNguoiGui) && forArr.get(i).emailNguoiNhan.equals(EmailUser)){
                        hoiThoaiArrayList.add(new HoiThoai(forArr.get(i).message_User,forArr.get(i).emailNguoiNhan,forArr.get(i).email_User));
                    }
                }
                hoiThoaiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                hoiThoaiAdapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.callbutton, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.audio) {
            Toast.makeText(this, "call ne`", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.video){
            Toast.makeText(this, "video ne`", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}