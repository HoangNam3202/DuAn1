package com.example.tinnhn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DangKiActivity extends AppCompatActivity {
    DatabaseHelper databasehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dang_ki);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databasehelper = new DatabaseHelper(this,"sqlDangNhap",null,2);
        final EditText edtTen = findViewById(R.id.edtTen);
        final EditText edtMatKhau = findViewById(R.id.edtMatKhau);
        final EditText edtNhapLai = findViewById(R.id.edtMatKhauNhapLai);
        Button btnDangKi = findViewById(R.id.btnGo);
        databasehelper.UpData("CREATE TABLE IF NOT EXISTS TaiKhoan(idTaiKhoan Integer primary key autoincrement, " +
                "TenTaiKhoan varchar(20) , MatKhau varchar(100))");
        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ktratontai = false;
                if(!edtTen.getText().toString().equals("")){
                    if(!edtMatKhau.getText().toString().equals("")){
                        if(edtMatKhau.getText().toString().equals(edtNhapLai.getText().toString())){
                            Cursor cursor = databasehelper.GetData("Select * from TaiKhoan where TenTaiKhoan = '"+edtTen.getText().toString()+"'");
                            if(cursor.getCount() > 0){
                                Toast.makeText(DangKiActivity.this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(DangKiActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                databasehelper.UpData("Insert into TaiKhoan Values(null, '"+edtTen.getText().toString().trim()+"','"+edtMatKhau.getText().toString().trim()+"')");
                                Intent intent = new Intent(DangKiActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            edtNhapLai.setError("Mật khẩu không khớp !");
                        }
                    }
                    else {
                        edtMatKhau.setError("Chưa điền luôn nè !");
                    }
                }
                else {
                    edtTen.setError("Chưa điền nè !");
                }

            }
        });


    }
}