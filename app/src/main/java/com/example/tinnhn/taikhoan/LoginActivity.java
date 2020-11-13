package com.example.tinnhn.taikhoan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import static com.example.tinnhn.taikhoan.DangKiActivity.taiKhoanArrayList;


public class LoginActivity extends AppCompatActivity {
    EditText edtTenTaiKhoan;
    TextInputLayout tilMatKhau;
    TextInputEditText edtMatKhau;
    CheckBox cbGhiNhoDangNhap;
    Button btnDangNhap;
    TextView txtQuenMatKhau, txtDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        DangNhap();
    }

    private void DangNhap() {
        edtTenTaiKhoan = findViewById(R.id.edtTenTaiKhoan);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        cbGhiNhoDangNhap = findViewById(R.id.cbGhiNhoDangNhap);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
        txtDangKy = findViewById(R.id.txtDangKy);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
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
