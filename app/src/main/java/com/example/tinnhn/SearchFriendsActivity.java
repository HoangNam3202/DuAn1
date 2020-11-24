package com.example.tinnhn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchFriendsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView tv_Search = findViewById(R.id.tv_Search);
        ListView List_TimKiem = findViewById(R.id.List_TimKiem);
        ArrayList<TaiKhoan> TimKiemarrayList = new ArrayList<>();
        SearchFriendsAdapter searchFriendsAdapter = new SearchFriendsAdapter(SearchFriendsActivity.this,R.layout.list_tim_kiem_item,TimKiemarrayList);
        List_TimKiem.setAdapter(searchFriendsAdapter);
        final ArrayList<TaiKhoan> goiYKetBanArrayList_check = new ArrayList<>();


        tv_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                TimKiemarrayList.clear();
                searchFriendsAdapter.notifyDataSetChanged();
                mDatabase.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                        goiYKetBanArrayList_check.clear();
                        goiYKetBanArrayList_check.add(taiKhoan);
                        for (int x = 0; x < goiYKetBanArrayList_check.size(); x++) {
                            if(tv_Search.getText().toString().contains(goiYKetBanArrayList_check.get(x).email)){
                                TimKiemarrayList.add(new TaiKhoan(goiYKetBanArrayList_check.get(x).idTaiKhoan,goiYKetBanArrayList_check.get(x).tenTaiKhoan,goiYKetBanArrayList_check.get(x).email,
                                        goiYKetBanArrayList_check.get(x).matKhau,goiYKetBanArrayList_check.get(x).soDienThoai,goiYKetBanArrayList_check.get(x).diaChi,
                                        goiYKetBanArrayList_check.get(x).hinhDaiDien));
                            }
                        }
                        searchFriendsAdapter.notifyDataSetChanged();
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
    }
}