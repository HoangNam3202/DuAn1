package com.example.tinnhn;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.example.tinnhn.Call.Actions;
import com.example.tinnhn.Call.BaseActivity;
import com.example.tinnhn.Call.SinchServices;
import com.example.tinnhn.taikhoan.LoginActivity;
import com.example.tinnhn.taikhoan.MyReceiver;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.tinnhn.ui.main.SectionsPagerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.tinnhn.taikhoan.LoginActivity.kTraMang;

public class MainActivity extends BaseActivity {
    String TAG = "MainActivity";
    // lấy url hình từ mail người dùng
    BroadcastReceiver broadcastReceiver = new MyReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        filter.addAction(WifiManager.EXTRA_RESULTS_UPDATED);
        this.registerReceiver(broadcastReceiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.envelope);
        tabs.getTabAt(1).setIcon(R.drawable.group);
        tabs.getTabAt(2).setIcon(R.drawable.friend);
        tabs.getTabAt(3).setIcon(R.drawable.ic_baseline_dashboard_24);
        tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorItemSelected), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorItemSelected), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        break;
                    case 1:
                        tabs.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorItemSelected), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorItemSelected), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        break;
                    case 3:
                        tabs.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorItemSelected), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                chaydichvu();
                tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorItem), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        if (kTraMang == 0) {
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("No internet, check network and restart app");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
        else {
            String trangthai = "Active Now";
            HamTrangThai(trangthai);
        }
    }

    private void actionOnService(Actions actions) {

        Intent intent = new Intent(MainActivity.this, SinchServices.class);
        intent.setAction(actions.name());
        startService(intent);



    }
    @Override
    public void onDestroy() {

        String trangthai = "Not Active";
        HamTrangThai(trangthai);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        actionOnService(Actions.START);
        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onServiceDisconnected() {

        super.onServiceDisconnected();
    }

    @Override
    public void onResume() {
        actionOnService(Actions.STOP);
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mainlist, menu);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_Dangxuat:
                XoaGhiNhoDangNhap();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.menu_Thoat:

                break;
            case R.id.menu_Xoa:

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void XoaGhiNhoDangNhap() {

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove("tenTaiKhoan");
        editor.commit();
    }
    //dang xuat
    public void HamTrangThai(String trangthai){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String EmailUser = sharedPreferences.getString("tenTaiKhoan", "");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("TrangThai").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TrangThai trangThai1 = snapshot.getValue(TrangThai.class);
                if (trangThai1.Email_user.equals(EmailUser)) {
                    String key = snapshot.getKey();
                    mDatabase.child("TrangThai").child(key).child("TrangThai").setValue(trangthai);
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
    }
}