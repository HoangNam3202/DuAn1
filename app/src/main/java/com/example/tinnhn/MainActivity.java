package com.example.tinnhn;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.example.tinnhn.Call.BaseActivity;
import com.example.tinnhn.Call.SinchServices;
import com.example.tinnhn.taikhoan.HihNgNhanTrogMessArrLst;
import com.example.tinnhn.taikhoan.LoginActivity;
import com.example.tinnhn.taikhoan.MyReceiver;
import com.example.tinnhn.ui.main.PlaceholderFragment;
import com.google.android.gms.common.api.internal.SignInConnectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tinnhn.ui.main.SectionsPagerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;

import static com.example.tinnhn.taikhoan.LoginActivity.dbFirebase;
import static com.example.tinnhn.taikhoan.LoginActivity.kTraMang;
import static com.example.tinnhn.taikhoan.LoginActivity.urlHinhDaiDien;

public class MainActivity extends BaseActivity {
    String TAG = "MainActivity";
    // lấy url hình từ mail người dùng
    public static ArrayList<HihNgNhanTrogMessArrLst> hihNgNhanTrogMessArrLsts = new ArrayList<>();
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

}