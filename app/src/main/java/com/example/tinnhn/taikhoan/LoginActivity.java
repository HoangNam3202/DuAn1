package com.example.tinnhn.taikhoan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tinnhn.Call.Actions;
import com.example.tinnhn.Call.BaseActivity;
import com.example.tinnhn.Call.GroupHoiThoaiActivity;
import com.example.tinnhn.Call.SinchServices;
import com.example.tinnhn.MainActivity;
import com.example.tinnhn.R;
import com.example.tinnhn.TrangThai;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity {
    public static int kiemTraDangNhap;
    public static DBFirebase dbFirebase;
    public static String tenUser = "";
    public static String DiaChiUser = "";
    public static String urlHinhDaiDien = "";
    public static KiemTraMang kiemTraMang = new KiemTraMang();
    public static int kTraMang;
    EditText edtEmail;
    TextInputLayout tilMatKhau;
    TextInputEditText edtMatKhau;
    Button btnDangNhap;
    TextView txtQuenMatKhau, txtDangKy, tvEmail, tvMatKhau;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String emailsv;
    BroadcastReceiver broadcastReceiver = new MyReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        filter.addAction(WifiManager.EXTRA_RESULTS_UPDATED);
        this.registerReceiver(broadcastReceiver, filter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }

        dbFirebase = new DBFirebase();
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        emailsv = sharedPreferences.getString("tenTaiKhoan", "");

        Toast.makeText(this, emailsv, Toast.LENGTH_SHORT).show();
        kTraMang = kiemTraMang.CheckNetworkStatus(LoginActivity.this);
//        Toast.makeText(this, "" + kTraMang, Toast.LENGTH_SHORT).show();

        KiemTraGhiNhoDangNhap();
        DangNhap();
        actionOnService(Actions.STOP);
        if (kTraMang != 0) {
            txtDangKy = findViewById(R.id.txtDangKy);
            txtDangKy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, DangKiActivity.class));
                }
            });
            txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
            txtQuenMatKhau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, QuenMatKhauActivity.class);
                    intent.putExtra("QMK", true);
                    startActivity(intent);
                }
            });
        } else {
//            Toast.makeText(this, "No internet, check network and restart app", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("No internet, check network and restart app");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }

    }

    private void KiemTraGhiNhoDangNhap() {
        String tenTaiKhoan = sharedPreferences.getString("tenTaiKhoan", "");
        if (tenTaiKhoan.length() != 0) {
            urlHinhDaiDien = sharedPreferences.getString("urlHinhDaiDien", "");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void DangNhap() {
        edtEmail = findViewById(R.id.edtEmail);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        tvEmail = findViewById(R.id.tvEmail);
        tvMatKhau = findViewById(R.id.tvMatKhau);
        //Kiểm tra hợp lệ
        final String checkEmail = "[a-zA-Z0-9.]+@[a-z]+(\\.+[a-z]+){1,2}";
        final String checkMatKhau = "[a-zA-Z0-9+]{6,300}";
        final boolean[] kiemTra = new boolean[2];
        kiemTra[0] = false;
        kiemTra[1] = false;
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(checkEmail)) {
                    tvEmail.setText("");
                    tvEmail.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[0] = true;
                } else {
                    tvEmail.setText("Invalid email");
                    tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[0] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtMatKhau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(checkMatKhau)) {
                    tvMatKhau.setText("");
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[1] = true;
                } else {
                    tvMatKhau.setText("Invalid password");
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[1] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
        txtDangKy = findViewById(R.id.txtDangKy);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kTraMang != 0) {
                    if (kiemTra[0] && kiemTra[1]) {
                        String email = edtEmail.getText().toString().trim();
                        String matKhau = edtMatKhau.getText().toString().trim();
                        dbFirebase.KiemTraDangNhap(email, matKhau);
                        //
                        final Dialog dialog = new Dialog(LoginActivity.this);
                        dialog.setContentView(R.layout.dialog_loading);
                        dialog.show();
                        new CountDownTimer(1500, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                dialog.dismiss();
                            }
                        }.start();
                        //
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (kiemTraDangNhap == 0) {
                                    editor.putString("tenTaiKhoan", email);
                                    editor.putString("tenUser", tenUser);
                                    editor.putString("DiaChiUser", DiaChiUser);
                                    editor.putString("urlHinhDaiDien", urlHinhDaiDien);
                                    editor.commit();
                                    getGiaodiendichvu().startClient(email);
                                    HamTrangThai(email);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                                if (kiemTraDangNhap == 1) {
                                    tvEmail.setText("Wrong email, check again");
                                    tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
                                }
                                if (kiemTraDangNhap == 2) {
                                    tvMatKhau.setText("Wrong password, check again");
                                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                                }
                                if (kiemTraDangNhap == -1) {
                                    Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 1300);
                    } else {
                        if (!kiemTra[0]) {
                            tvEmail.setText("Invalid email");
                            tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
                        }
                        if (!kiemTra[1]) {
                            tvMatKhau.setText("Invalid password");
                            tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                        }

                    }
                } else
                    Toast.makeText(LoginActivity.this, "No internet, check network and restart app", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        actionOnService(Actions.STOP);
        super.onStart();
    }

    @Override
    protected void onResume() {
        actionOnService(Actions.STOP);
        super.onResume();
    }

    private void actionOnService(Actions actions) {

        Intent intent = new Intent(LoginActivity.this, SinchServices.class);
        intent.setAction(actions.name());
        startService(intent);
    }

    public void HamTrangThai(String email){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("TrangThai").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TrangThai trangThai1 = snapshot.getValue(TrangThai.class);
                if (trangThai1.Email_user.equals(email)) {
                    String key = snapshot.getKey();
                    mDatabase.child("TrangThai").child(key).child("TrangThai").setValue("Active Now");
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

    @Override
    public void onDestroy() {
//        actionOnService(Actions.START);
        getGiaodiendichvu().startClient(emailsv);
        super.onDestroy();
    }

}

//                    AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
//                            .setMessage("Đang đăng nhập...")
//                            .create();
//                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                        private static final int AUTO_DISMISS_MILLIS = 1200;
//                        @Override
//                        public void onShow(final DialogInterface dialog) {
//                            new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
//                                @Override
//                                public void onTick(long millisUntilFinished) {
////                                    dbFirebase.KiemTraTaiKhoan(email, matKhau);
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                    if (kiemTraTaiKhoan == 1) {
//                                        tvEmail.setText("01 Email chưa đúng, kiểm tra lại");
//                                        tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
//                                        kiemTra[0] = false;
//                                    } else if (kiemTraTaiKhoan == 2) {
//                                        tvMatKhau.setText("01 Mật khẩu chưa đúng, kiểm tra lại");
//                                        tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
//                                        kiemTra[1] = false;
//                                    } else if (kiemTraTaiKhoan == 0) {
//                                        Toast.makeText(LoginActivity.this, "OK!", Toast.LENGTH_SHORT).show();
//                                        editor.putString("tenTaiKhoan", email);
//                                        editor.putString("tenUser", TenUser);
//                                        editor.commit();
//                                        getGiaodiendichvu().startClient(email);
//                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                        finish();
//                                    }
//                                }
//                            }.start();
//                        }
//                    });
//                    dialog.show();


//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    boolean check = false;
//    DatabaseHelper databasehelper;
//        initPreferences();
//        final EditText edtTen = findViewById(R.id.edtTen);
//        final EditText edtMatKhau = findViewById(R.id.edtMatKhau);
//        Button btnGo = findViewById(R.id.btnGo);
//        TextView txtDangKi = findViewById(R.id.txtDangKi);
//        txtDangKi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, DangKiActivity.class);
//                startActivity(intent);
//            }
//        });
//    private void initPreferences() {
//        sharedPreferences = getSharedPreferences("mylogin", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//    }
