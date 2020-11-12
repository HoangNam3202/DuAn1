package com.example.tinnhn;

import android.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText edtNguoiNhan = findViewById(R.id.edtNguoiNhan);
        final EditText edtNoiDung = findViewById(R.id.edtNoiDung);
        final TextView txtChange = findViewById(R.id.txtChange);
        Button btnGui = findViewById(R.id.btbGui);

        Intent intent = getIntent();
        final String TenDanhBa = intent.getStringExtra("TenDanhBa");
        if(TenDanhBa != null){
            edtNguoiNhan.setText(TenDanhBa);
        }
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NoiDung = edtNoiDung.getText().toString().trim();
                String TenNguoiNhan = edtNguoiNhan.getText().toString().trim();
                if(TenDanhBa != null){
                    TenNguoiNhan = TenDanhBa;
                }
                if(TenNguoiNhan.equals("")){
                    edtNguoiNhan.setError("");
                    Toast.makeText(AddActivity.this, "Thêm người dùng", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(NoiDung.equals("")){
                        Toast.makeText(AddActivity.this, "Thêm nội dung", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        edtNoiDung.setError(null);
                        Intent intent = new Intent(AddActivity.this,MainActivity.class);
                        intent.putExtra("Name", TenNguoiNhan);
                        intent.putExtra("Comment", NoiDung);

                        startActivity(intent);
                    }
                }

            }
        });

        edtNoiDung.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String noiDung = edtNoiDung.getText().toString();
                txtChange.setText(noiDung);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}