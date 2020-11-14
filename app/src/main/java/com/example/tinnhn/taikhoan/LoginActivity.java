package com.example.tinnhn.taikhoan;

import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.tinnhn.MainActivity;
import com.example.tinnhn.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    public static ArrayList<TaiKhoan> taiKhoanArrayList = new ArrayList<>();
    EditText edtTenTaiKhoan;
    TextInputLayout tilMatKhau;
    TextInputEditText edtMatKhau;
    CheckBox cbGhiNhoDangNhap;
    Button btnDangNhap;
    TextView txtQuenMatKhau, txtDangKy, tvTenTaiKhoan, tvMatKhau;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        taiKhoanArrayList.addo(new TaiKhoan(1, "qweqwe", "qwe@qwe.qwe", "qweqwe", "0234234234", "qwe", 0));

        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        KiemTraGhiNhoDangNhap();
        DangNhap();
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
        edtTenTaiKhoan = findViewById(R.id.edtTenTaiKhoan);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        tvTenTaiKhoan = findViewById(R.id.tvTenTaiKhoan);
        tvMatKhau = findViewById(R.id.tvMatKhau);
        //Kiểm tra hợp lệ
        final String checkTenTaiKhoan = "[a-zA-Z0-9+]{6,50}";
        final String checkMatKhau = "[a-zA-Z0-9+]{6,300}";
        final boolean[] kiemTra = new boolean[2];
        kiemTra[0] = false;
        kiemTra[1] = false;
        edtTenTaiKhoan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(checkTenTaiKhoan)) {
                    tvTenTaiKhoan.setText("OK");
                    tvTenTaiKhoan.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[0] = true;
                } else {
                    tvTenTaiKhoan.setText("NOT OK");
                    tvTenTaiKhoan.setTextColor(getResources().getColor(R.color.colorDanger));
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
                    tvMatKhau.setText("OK");
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[1] = true;
                } else {
                    tvMatKhau.setText("NOT OK");
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[1] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //
        cbGhiNhoDangNhap = findViewById(R.id.cbGhiNhoDangNhap);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
        txtDangKy = findViewById(R.id.txtDangKy);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemTra[0] && kiemTra[1]) {
                    String tenTaiKhoan = edtTenTaiKhoan.getText().toString().trim();
                    String matKhau = edtMatKhau.getText().toString().trim();
                    boolean xacNhan = false;
                    for (int i = 0; i < taiKhoanArrayList.size(); i++) {
                        if (tenTaiKhoan.equals(taiKhoanArrayList.get(i).getTenTaiKhoan()) && matKhau.equals(taiKhoanArrayList.get(i).getMatKhau())) {
                            xacNhan = true;
                            break;
                        }
                    }
                    if (xacNhan) {
                        if (cbGhiNhoDangNhap.isChecked()) {
                            editor.putString("tenTaiKhoan", tenTaiKhoan);
                        } else {
                            editor.remove("tenTaiKhoan");
                        }
                        editor.commit();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Nhập chưa hợp lệ", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
