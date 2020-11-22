package com.example.tinnhn.taikhoan;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinnhn.Call.BaseActivity;
import com.example.tinnhn.MainActivity;
import com.example.tinnhn.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class LoginActivity extends BaseActivity {
    public static ArrayList<TaiKhoan> taiKhoanArrayList = new ArrayList<>();
    EditText edtEmail;
    TextInputLayout tilMatKhau;
    TextInputEditText edtMatKhau;
    CheckBox cbGhiNhoDangNhap;
    Button btnDangNhap;
    TextView txtQuenMatKhau, txtDangKy, tvEmail, tvMatKhau;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DBFirebase dbFirebase = new DBFirebase();
    String emailsv;
    String TenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE},100);
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        taiKhoanArrayList.addo(new TaiKhoan(1, "qweqwe", "qwe@qwe.qwe", "qweqwe", "0234234234", "qwe", 0));
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        KiemTraGhiNhoDangNhap();
        taiKhoanArrayList = dbFirebase.LayDanhSachTaiKhoan();
        DangNhap();
        emailsv=sharedPreferences.getString("tenTaiKhoan", "");
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
                startActivity(new Intent(LoginActivity.this, QuenMatKhauActivity.class));
            }
        });
    }

    private void KiemTraGhiNhoDangNhap() {
        String tenTaiKhoan = sharedPreferences.getString("tenTaiKhoan", "");
        if (tenTaiKhoan.length() != 0) {
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
                    tvEmail.setText("Email chưa hợp lệ");
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
                    tvMatKhau.setText("Mật khẩu chưa hợp lệ");
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[1] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //
//        cbGhiNhoDangNhap = findViewById(R.id.cbGhiNhoDangNhap);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
        txtDangKy = findViewById(R.id.txtDangKy);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemTra[0] && kiemTra[1]) {
                    String email = edtEmail.getText().toString().trim();
                    String matKhau = edtMatKhau.getText().toString().trim();
                    boolean xacNhan = false;
                    for (int i = 0; i < taiKhoanArrayList.size(); i++) {
                        if (email.equals(taiKhoanArrayList.get(i).getEmail()) && matKhau.equals(taiKhoanArrayList.get(i).getMatKhau())) {
                            xacNhan = true;
                            TenUser = taiKhoanArrayList.get(i).tenTaiKhoan;
                            break;
                        }
                    }
                    if (xacNhan) {
                        editor.putString("tenTaiKhoan", email);
                        editor.putString("tenUser", TenUser);
                        editor.commit();
                        getGiaodiendichvu().startClient(email);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        taiKhoanArrayList.clear();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Nhập chưa hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        getGiaodiendichvu().startClient(emailsv);
        super.onDestroy();
    }

}
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
