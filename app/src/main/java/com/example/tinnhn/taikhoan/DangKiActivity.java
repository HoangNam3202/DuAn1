package com.example.tinnhn.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tinnhn.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.tinnhn.taikhoan.LoginActivity.taiKhoanArrayList;

public class DangKiActivity extends AppCompatActivity {
    TextInputLayout tilTenTaiKhoan, tilEmail, tilMatKhau, tilNhapLaiMatKhau, tilSoDienThoai, tilDiaChi;
    TextInputEditText edtTenTaiKhoan, edtEmail, edtMatKhau, edtNhapLaiMatKhau, edtSoDienThoai, edtDiaChi;
    TextView tvTenTaiKhoan, tvEmail, tvMatKhau, tvNhapLaiMatKhau, tvSoDienThoai, tvDiaChi;
    ImageView ivHinhDaiDien;
    Button btnChonHinhDaiDien, btnDangKy;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
        tvTenTaiKhoan = findViewById(R.id.tvTenTaiKhoan);
        tvEmail = findViewById(R.id.tvEmail);
        tvMatKhau = findViewById(R.id.tvMatKhau);
        tvNhapLaiMatKhau = findViewById(R.id.tvNhapLaiMatKhau);
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        btnDangKy = findViewById(R.id.btnDangKy);
        //Kiểm tra nhập hợp lệ
        final String checkTenTaiKhoan = "[a-zA-Z0-9+]{6,50}";
        final String checkSoDienThoai = "0[2-9]\\d{8}";
        final String checkEmail = "[a-zA-Z0-9.]+@[a-z]+(\\.+[a-z]+){1,2}";
        final String checkMatKhau = "[a-zA-Z0-9+]{6,300}";
        final boolean[] kiemTra = new boolean[6];
        int i = 0;
        while (i < 6) {
            kiemTra[i] = false;
            i++;
        }
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
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(checkEmail)) {
                    tvEmail.setText("OK");
                    tvEmail.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[1] = true;
                } else {
                    tvEmail.setText("NOT OK");
                    tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[1] = false;
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
                    kiemTra[2] = true;
                } else {
                    tvMatKhau.setText("NOT OK");
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[2] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtNhapLaiMatKhau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(checkMatKhau) && s.toString().equals(edtMatKhau.getText().toString())) {
                    tvNhapLaiMatKhau.setText("OK");
                    tvNhapLaiMatKhau.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[3] = true;
                } else {
                    tvNhapLaiMatKhau.setText("NOT OK");
                    tvNhapLaiMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[3] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSoDienThoai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(checkSoDienThoai)) {
                    tvSoDienThoai.setText("OK");
                    tvSoDienThoai.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[4] = true;
                } else {
                    tvSoDienThoai.setText("NOT OK");
                    tvSoDienThoai.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[4] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDiaChi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != 0) {
                    tvDiaChi.setText("OK");
                    tvDiaChi.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[5] = true;
                } else {
                    tvDiaChi.setText("NOT OK");
                    tvDiaChi.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[5] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemTra[0] && kiemTra[1] && kiemTra[2] && kiemTra[3] && kiemTra[4] && kiemTra[5]) {
                    String tenTaiKhoan, email, matKhau, nhapLaiMatKhau, soDienThoai, diaChi;
                    tenTaiKhoan = edtTenTaiKhoan.getText().toString().trim();
                    email = edtEmail.getText().toString().trim();
                    matKhau = edtMatKhau.getText().toString().trim();
                    nhapLaiMatKhau = edtNhapLaiMatKhau.getText().toString().trim();
                    soDienThoai = edtSoDienThoai.getText().toString().trim();
                    diaChi = edtDiaChi.getText().toString().trim();
                    boolean kiemTraMatKhau = matKhau.equals(nhapLaiMatKhau);
                    if (kiemTraMatKhau) {
                        TaiKhoan taiKhoan = new TaiKhoan(RandomString(9), tenTaiKhoan, email, matKhau, soDienThoai, diaChi, 0);
                        taiKhoanArrayList.add(taiKhoan);
                        // thêm tài khoản vào DB
                        databaseReference.child("TaiKhoan").push().setValue(taiKhoan);
                        //
                        Toast.makeText(DangKiActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DangKiActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(DangKiActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DangKiActivity.this, "Nhập chưa hợp lệ", Toast.LENGTH_SHORT).show();
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
