package com.example.tinnhn.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tinnhn.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.tinnhn.taikhoan.LoginActivity.taiKhoanArrayList;

public class DangKiActivity extends AppCompatActivity {
    TextInputLayout tilTenTaiKhoan, tilEmail, tilMatKhau, tilNhapLaiMatKhau, tilSoDienThoai;
    TextInputEditText edtTenTaiKhoan, edtEmail, edtMatKhau, edtNhapLaiMatKhau, edtSoDienThoai;
    TextView tvTenTaiKhoan, tvEmail, tvMatKhau, tvNhapLaiMatKhau, tvSoDienThoai;
    ImageView ivHinhDaiDien;
    Spinner spnTinhThanh;
    Button btnChonHinhDaiDien, btnDangKy;
    DBFirebase dbFirebase = new DBFirebase();
    DatabaseReference databaseReference;
    String tinhThanhDaChon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        databaseReference = FirebaseDatabase.getInstance().getReference();
        ChonTinhThanh();
        DangKy();
    }

    private void DangKy() {
        tilTenTaiKhoan = findViewById(R.id.tilTenTaiKhoan);
        tilEmail = findViewById(R.id.tilEmail);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        tilNhapLaiMatKhau = findViewById(R.id.tilNhapLaiMatKhau);
        tilSoDienThoai = findViewById(R.id.tilSoDienThoai);
        spnTinhThanh = findViewById(R.id.spnTinhThanh);
        edtTenTaiKhoan = findViewById(R.id.edtTenTaiKhoan);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = findViewById(R.id.edtNhapLaiMatKhau);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        tvTenTaiKhoan = findViewById(R.id.tvTenTaiKhoan);
        tvEmail = findViewById(R.id.tvEmail);
        tvMatKhau = findViewById(R.id.tvMatKhau);
        tvNhapLaiMatKhau = findViewById(R.id.tvNhapLaiMatKhau);
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        btnDangKy = findViewById(R.id.btnDangKy);
        //Kiểm tra nhập hợp lệ
        final String checkTenTaiKhoan = "[a-zA-Z0-9+]{6,50}";
        final String checkSoDienThoai = "0[2-9]\\d{8}";
        final String checkEmail = "[a-zA-Z0-9.]+@[a-z]+(\\.+[a-z]+){1,2}";
        final String checkMatKhau = "[a-zA-Z0-9+]{6,300}";
        final boolean[] kiemTra = new boolean[5];
        int i = 0;
        while (i < 5) {
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
                    tvTenTaiKhoan.setText("");
                    tvTenTaiKhoan.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[0] = true;
                } else {
                    tvTenTaiKhoan.setText(getResources().getString(R.string.err_ten_tai_khoan));
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
                    tvEmail.setText("");
                    tvEmail.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[1] = true;
                } else {
                    tvEmail.setText("Email chưa hợp lệ");
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
                    tvMatKhau.setText("");
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[2] = true;
                } else {
                    tvMatKhau.setText(getResources().getString(R.string.err_mat_khau));
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
                    tvNhapLaiMatKhau.setText("");
                    tvNhapLaiMatKhau.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[3] = true;
                } else {
                    tvNhapLaiMatKhau.setText("Mật khẩu chưa khớp");
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
                    tvSoDienThoai.setText("");
                    tvSoDienThoai.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[4] = true;
                } else {
                    tvSoDienThoai.setText("Số điện thoại chưa hợp lệ");
                    tvSoDienThoai.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[4] = false;
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
                if (kiemTra[0] && kiemTra[1] && kiemTra[2] && kiemTra[3] && kiemTra[4]) {
                    String tenTaiKhoan, email, matKhau, nhapLaiMatKhau, soDienThoai, diaChi;
                    tenTaiKhoan = edtTenTaiKhoan.getText().toString().trim();
                    email = edtEmail.getText().toString().trim();
                    matKhau = edtMatKhau.getText().toString().trim();
                    nhapLaiMatKhau = edtNhapLaiMatKhau.getText().toString().trim();
                    soDienThoai = edtSoDienThoai.getText().toString().trim();
                    boolean kiemTraMatKhau = matKhau.equals(nhapLaiMatKhau);
                    boolean kiemTraTrungTenTaiKhoan = false;
                    boolean kiemTraTrungEmail = false;
                    boolean kiemTraTrungSoDienThoai = false;
                    // test tentaikhoan trung lap
                    int h = 0;
                    while (h < taiKhoanArrayList.size()) {
                        if (tenTaiKhoan.equals(taiKhoanArrayList.get(h).getTenTaiKhoan())) {
                            kiemTraTrungTenTaiKhoan = true;
                            break;
                        }
                        h++;
                    }
                    //
                    //test email trung lap
                    int i = 0;
                    while (i < taiKhoanArrayList.size()) {
                        if (email.equals(taiKhoanArrayList.get(i).getEmail())) {
                            kiemTraTrungEmail = true;
                            break;
                        }
                        i++;
                    }
                    // test sdt trung lap
                    int j = 0;
                    while (j < taiKhoanArrayList.size()) {
                        if (soDienThoai.equals(taiKhoanArrayList.get(j).getSoDienThoai())) {
                            kiemTraTrungSoDienThoai = true;
                            break;
                        }
                        j++;
                    }
                    if (kiemTraMatKhau && !kiemTraTrungEmail && !kiemTraTrungSoDienThoai && !kiemTraTrungTenTaiKhoan) {
                        TaiKhoan taiKhoan = new TaiKhoan(RandomString(9), tenTaiKhoan, email, matKhau, soDienThoai, tinhThanhDaChon, 0);
                        // thêm tài khoản vào DB
                        dbFirebase.ThemTaiKhoan(taiKhoan);
                        //
                        Toast.makeText(DangKiActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (!kiemTraMatKhau) {
                            Toast.makeText(DangKiActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                        }
                        if (kiemTraTrungTenTaiKhoan) {
                            tvTenTaiKhoan.setText("Tên tài khoản bị trùng");
                            tvTenTaiKhoan.setTextColor(getResources().getColor(R.color.colorDanger));
                        }
                        if (kiemTraTrungEmail) {
                            tvEmail.setText("Email bị trùng");
                            tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
                        }

                        if (kiemTraTrungSoDienThoai) {
                            tvSoDienThoai.setText("Số điện thoại bị trùng");
                            tvSoDienThoai.setTextColor(getResources().getColor(R.color.colorDanger));
                        }
                    }
                } else {
                    Toast.makeText(DangKiActivity.this, "Nhập chưa hợp lệ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void ChonTinhThanh() {
        List<String> tinhThanhs = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, tinhThanhs);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TinhThanh2").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String tinh = snapshot.getValue().toString();
                tinhThanhs.add(tinh);
                adapter.notifyDataSetChanged();
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
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_2);
        spnTinhThanh = findViewById(R.id.spnTinhThanh);
        spnTinhThanh.setAdapter(adapter);
        spnTinhThanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tinhThanhDaChon = tinhThanhs.get(position);
//                Toast.makeText(DangKiActivity.this, ""+tinhThanhDaChon, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
