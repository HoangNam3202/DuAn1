package com.example.tinnhn.taikhoan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tinnhn.R;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    boolean check = false;

    DatabaseHelper databasehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main2);

        initPreferences();

        final EditText edtTen = findViewById(R.id.edtTen);
        final EditText edtMatKhau = findViewById(R.id.edtMatKhau);
        Button btnGo = findViewById(R.id.btnGo);
        TextView txtDangKi = findViewById(R.id.txtDangKi);

        txtDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,DangKiActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initPreferences() {
        sharedPreferences = getSharedPreferences("mylogin",MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}