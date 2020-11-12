package com.example.tinnhn.taikhoan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tinnhn.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;

public class DangKiActivity extends AppCompatActivity {
    TextInputLayout tilTenTaiKhoan, tilEmail, tilMatKhau, tilNhapLaiMatKhau, tilSoDienThoai, tilDiaChi;
    TextInputEditText edtTenTaiKhoan, edtEmail, edtMatKhau, edtNhapLaiMatKhau, edtSoDienThoai, edtDiaChi;
    ImageView ivHinhDaiDien;
    Button btnChonHinhDaiDien, btnDangKy;
    public static ArrayList<TaiKhoan> taiKhoanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DangKy();
    }

    private void DangKy() {
        tilTenTaiKhoan = findViewById(R.id.tilTenTaiKhoan);
        tilEmail = findViewById(R.id.tilEmail);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        tilNhapLaiMatKhau = findViewById(R.id.tilNhapLaiMatKhau);
        tilSoDienThoai = findViewById(R.id.tilSoDienThoai);
        tilDiaChi = findViewById(R.id.tilDiaChi);
        edtTenTaiKhoan = findViewById(R.id.edtTenTaiKhoan);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = findViewById(R.id.edtNhapLaiMatKhau);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnDangKy = findViewById(R.id.btnDangKy);
        taiKhoanArrayList = new ArrayList<>();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenTaiKhoan, email, matKhau, nhapLaiMatKhau, soDienThoai, diaChi;
                tenTaiKhoan = edtTenTaiKhoan.getText().toString().trim();
                email = edtEmail.getText().toString().trim();
                matKhau = edtMatKhau.getText().toString().trim();
                nhapLaiMatKhau = edtNhapLaiMatKhau.getText().toString().trim();
                soDienThoai = edtSoDienThoai.getText().toString().trim();
                diaChi = edtDiaChi.getText().toString().trim();
                boolean kiemTraMatKhau = matKhau.equals(nhapLaiMatKhau);
                if (kiemTraMatKhau && matKhau.length() > 0) {
                    taiKhoanArrayList.add(new TaiKhoan(RandomString(9), tenTaiKhoan, email, matKhau, soDienThoai, diaChi, 0));
                } else {
                    Toast.makeText(DangKiActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int RandomString(int n) {
//        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
        String AlphaNumericString = "0123456789";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return Integer.parseInt(sb.toString());
    }

}
//    DatabaseHelper databasehelper;
//
//        databasehelper = new DatabaseHelper(this,"sqlDangNhap",null,2);
//        final EditText edtTen = findViewById(R.id.edtTen);
//        final EditText edtMatKhau = findViewById(R.id.edtMatKhau);
//        final EditText edtNhapLai = findViewById(R.id.edtMatKhauNhapLai);
//        Button btnDangKi = findViewById(R.id.btnGo);
//        databasehelper.UpData("CREATE TABLE IF NOT EXISTS TaiKhoan(idTaiKhoan Integer primary key autoincrement, " +
//                "TenTaiKhoan varchar(20) , MatKhau varchar(100))");
//        btnDangKi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean ktratontai = false;
//                if(!edtTen.getText().toString().equals("")){
//                    if(!edtMatKhau.getText().toString().equals("")){
//                        if(edtMatKhau.getText().toString().equals(edtNhapLai.getText().toString())){
//                            Cursor cursor = databasehelper.GetData("Select * from TaiKhoan where TenTaiKhoan = '"+edtTen.getText().toString()+"'");
//                            if(cursor.getCount() > 0){
//                                Toast.makeText(DangKiActivity.this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                Toast.makeText(DangKiActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
//                                databasehelper.UpData("Insert into TaiKhoan Values(null, '"+edtTen.getText().toString().trim()+"','"+edtMatKhau.getText().toString().trim()+"')");
//                                Intent intent = new Intent(DangKiActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//                        else {
//                            edtNhapLai.setError("Mật khẩu không khớp !");
//                        }
//                    }
//                    else {
//                        edtMatKhau.setError("Chưa điền luôn nè !");
//                    }
//                }
//                else {
//                    edtTen.setError("Chưa điền nè !");
//                }
//
//            }
//        });
