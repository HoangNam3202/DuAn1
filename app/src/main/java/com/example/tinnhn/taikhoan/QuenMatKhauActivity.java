package com.example.tinnhn.taikhoan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.tinnhn.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.tinnhn.taikhoan.LoginActivity.dbFirebase;
import static com.example.tinnhn.taikhoan.LoginActivity.kiemTraDangNhap;

public class QuenMatKhauActivity extends AppCompatActivity {
    public static int xacNhanTaiKhoan = -1;
    public static String idTaiKhoanQMK = "";
    EditText edtTenTaiKhoan, edtEmail, edtSoDienThoai;
    TextInputLayout tilMatKhauHienTai;
    TextInputEditText edtMatKhau, edtNhapLaiMatKhau, edtMatKhauHienTai;
    TextView tvTenTaiKhoan, tvEmail, tvSoDienThoai, tvMatKhau, tvNhapLaiMatKhau, tvTieuDe, tvMatKhauHienTai;
    Button btnQuenMatKhau;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quen_mat_khau);
        Intent intent = getIntent();
        boolean qMK = false;
        qMK = intent.getExtras().getBoolean("QMK");
//        Toast.makeText(this, "" + qMK, Toast.LENGTH_SHORT).show();
        if (qMK) {
            QuenMatKhau();
        } else {
            sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            DoiMatKhau();
        }
    }

    private void DoiMatKhau() {
        edtMatKhauHienTai = findViewById(R.id.edtMatKhauHienTai);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = findViewById(R.id.edtNhapLaiMatKhau);
        btnQuenMatKhau = findViewById(R.id.btnQuenMatKhau);
        tvMatKhauHienTai = findViewById(R.id.tvMatKhauHienTai);
        tvMatKhau = findViewById(R.id.tvMatKhau);
        tvNhapLaiMatKhau = findViewById(R.id.tvNhapLaiMatKhau);
        // kiểm tra nhập hợp lệ
        final String checkMatKhau = "[a-zA-Z0-9+]{6,300}";
        final boolean[] kiemTra = new boolean[3];
        int i = 0;
        while (i < 3) {
            kiemTra[i] = false;
            i++;
        }
        edtMatKhauHienTai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(checkMatKhau)) {
                    tvMatKhauHienTai.setText("");
                    tvMatKhauHienTai.setTextColor(getResources().getColor(R.color.colorSuccess));
                    kiemTra[0] = true;
                } else {
                    tvMatKhauHienTai.setText(getResources().getString(R.string.err_mat_khau));
                    tvMatKhauHienTai.setTextColor(getResources().getColor(R.color.colorDanger));
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
                    tvMatKhau.setText(getResources().getString(R.string.err_mat_khau));
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[1] = false;
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
                    kiemTra[2] = true;
                } else {
                    tvNhapLaiMatKhau.setText("Password not matched");
                    tvNhapLaiMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[2] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        btnQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemTra[0] && kiemTra[1] && kiemTra[2]) {
                    String email, matKhauHienTai, matKhau, nhapLaiMatKhau;
                    matKhauHienTai = edtMatKhauHienTai.getText().toString().trim();
                    matKhau = edtMatKhau.getText().toString().trim();
                    nhapLaiMatKhau = edtNhapLaiMatKhau.getText().toString().trim();
                    email = sharedPreferences.getString("tenTaiKhoan", "");
                    dbFirebase.KiemTraDangNhap(email, matKhauHienTai);
                    //
                    Dialog dialog = new Dialog(QuenMatKhauActivity.this);
                    dialog.setContentView(R.layout.dialog_loading);
                    TextView tvTinhTrang = dialog.findViewById(R.id.tvTinhTrang);
                    tvTinhTrang.setText("Processing...");
                    new CountDownTimer(1300, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            dialog.show();
                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            if (kiemTraDangNhap == 0) {
                                if (matKhau.equals(nhapLaiMatKhau)) {
                                    Toast.makeText(QuenMatKhauActivity.this, "Success", Toast.LENGTH_LONG).show();
                                    dbFirebase.DoiMatKhau(idTaiKhoanQMK, matKhau);
                                    finish();
                                } else {
                                    Toast.makeText(QuenMatKhauActivity.this, "New password does not match", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                tvMatKhauHienTai.setText("Wrong password, try again");
                                tvMatKhauHienTai.setTextColor(getResources().getColor(R.color.colorDanger));
                                kiemTra[0] = false;
                            }
                        }
                    }.start();

                } else
                    Toast.makeText(QuenMatKhauActivity.this, "Enter invalid information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void QuenMatKhau() {
        tvTieuDe = findViewById(R.id.tvTieuDe);
        edtTenTaiKhoan = findViewById(R.id.edtTenTaiKhoan);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        tilMatKhauHienTai = findViewById(R.id.tilMatKhauHienTai);
        tvMatKhauHienTai = findViewById(R.id.tvMatKhauHienTai);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = findViewById(R.id.edtNhapLaiMatKhau);
        btnQuenMatKhau = findViewById(R.id.btnQuenMatKhau);
        tvTenTaiKhoan = findViewById(R.id.tvTenTaiKhoan);
        tvEmail = findViewById(R.id.tvEmail);
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        tvMatKhau = findViewById(R.id.tvMatKhau);
        tvNhapLaiMatKhau = findViewById(R.id.tvNhapLaiMatKhau);
        tvTieuDe.setText("Forgot password");
        tilMatKhauHienTai.setVisibility(View.GONE);
        tvMatKhauHienTai.setVisibility(View.GONE);
        edtTenTaiKhoan.setVisibility(View.VISIBLE);
        tvTenTaiKhoan.setVisibility(View.VISIBLE);
        edtEmail.setVisibility(View.VISIBLE);
        tvEmail.setVisibility(View.VISIBLE);
        edtSoDienThoai.setVisibility(View.VISIBLE);
        tvSoDienThoai.setVisibility(View.VISIBLE);

        // kiểm tra nhập hợp lệ
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
                    tvEmail.setText("Invalid email");
                    tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[1] = false;
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
                    kiemTra[2] = true;
                } else {
                    tvSoDienThoai.setText("Invalid phone number");
                    tvSoDienThoai.setTextColor(getResources().getColor(R.color.colorOrange));
                    kiemTra[2] = false;
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
                    kiemTra[3] = true;
                } else {
                    tvMatKhau.setText(getResources().getString(R.string.err_mat_khau));
                    tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[3] = false;
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
                    kiemTra[4] = true;
                } else {
                    tvNhapLaiMatKhau.setText("Chưa khớp mật khẩu");
                    tvNhapLaiMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
                    kiemTra[4] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        btnQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemTra[0] && kiemTra[1] && kiemTra[2] && kiemTra[3] && kiemTra[4]) {
                    String tenTaiKhoan, email, soDienThoai, matKhau, nhapLaiMatKhau;
                    tenTaiKhoan = edtTenTaiKhoan.getText().toString().trim();
                    email = edtEmail.getText().toString().trim();
                    soDienThoai = edtSoDienThoai.getText().toString().trim();
                    matKhau = edtMatKhau.getText().toString().trim();
                    nhapLaiMatKhau = edtNhapLaiMatKhau.getText().toString().trim();
                    if (matKhau.equals(nhapLaiMatKhau)) {
                        dbFirebase.KiemTraTaiKhoan(tenTaiKhoan, email, soDienThoai);
                        //
                        final Dialog dialog = new Dialog(QuenMatKhauActivity.this);
                        dialog.setContentView(R.layout.dialog_loading);
                        TextView tvTinhTrang = dialog.findViewById(R.id.tvTinhTrang);
                        tvTinhTrang.setText("Processing...");
                        dialog.show();
                        new CountDownTimer(1300, 100) {
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
                                if (xacNhanTaiKhoan == 0) {
                                    Toast.makeText(QuenMatKhauActivity.this, "OK men", Toast.LENGTH_SHORT).show();
                                    edtTenTaiKhoan.setEnabled(false);
                                    edtEmail.setEnabled(false);
                                    edtSoDienThoai.setEnabled(false);
                                    dbFirebase.DoiMatKhau(idTaiKhoanQMK, matKhau);
                                    finish();
                                } else {
                                    if (xacNhanTaiKhoan == 1) {
                                        tvTenTaiKhoan.setText("Sai tên tài khoản");
                                        tvTenTaiKhoan.setTextColor(getResources().getColor(R.color.colorDanger));
                                    }
                                    if (xacNhanTaiKhoan == 2) {
                                        tvEmail.setText("Sai email");
                                        tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
                                    }
                                    if (xacNhanTaiKhoan == 3) {
                                        tvSoDienThoai.setText("Sai số điện thoại");
                                        tvSoDienThoai.setTextColor(getResources().getColor(R.color.colorOrange));
                                    }
                                    if (xacNhanTaiKhoan == -1) {
                                        tvTenTaiKhoan.setText("Kiểm tra lại thông tin");
                                        tvTenTaiKhoan.setTextColor(getResources().getColor(R.color.colorDanger));
                                        tvEmail.setText("Kiểm tra lại thông tin");
                                        tvEmail.setTextColor(getResources().getColor(R.color.colorDanger));
                                        tvSoDienThoai.setText("Kiểm tra lại thông tin");
                                        tvSoDienThoai.setTextColor(getResources().getColor(R.color.colorOrange));
//                                        Toast.makeText(QuenMatKhauActivity.this, "Không tìm thấy tài khoản :(", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, 1300);
                        //
                    } else {
                        Toast.makeText(QuenMatKhauActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    }


                } else
                    Toast.makeText(QuenMatKhauActivity.this, "Nhập thông tin chưa hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

//    private boolean KiemTraXacThucTaiKhoan(String tenTaiKhoan, String email, String soDienThoai) {
//        int i = 0;
//        while (i < taiKhoanArrayList.size()) {
//            if (tenTaiKhoan.equals(taiKhoanArrayList.get(i).getTenTaiKhoan()) && email.equals(taiKhoanArrayList.get(i).getEmail()) && soDienThoai.equals(taiKhoanArrayList.get(i).getSoDienThoai())) {
//                return true;
//            }
//            i++;
//        }
//        return false;
//    }


//                    if (xacNhan) {
//                        if (matKhau.equals(nhapLaiMatKhau)) {
//                            Toast.makeText(QuenMatKhauActivity.this, "Thông tin xác nhận OK", Toast.LENGTH_SHORT).show();
//                            dbFirebase.DoiMatKhau(tenTaiKhoan, email, matKhau);
//                            // tạm ngưng tại chỗ này, đổi pass
////                        asd;
//                            //
//                        } else {
//                            tvMatKhau.setText("NOT OK");
//                            tvMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
//                            tvNhapLaiMatKhau.setText("NOT OK");
//                            tvNhapLaiMatKhau.setTextColor(getResources().getColor(R.color.colorDanger));
//                            kiemTra[3] = false;
//                            kiemTra[4] = false;
//                        }
//
//                    } else
//                        Toast.makeText(QuenMatKhauActivity.this, "Thông tin xác nhận chưa đúng", Toast.LENGTH_SHORT).show();