package com.example.tinnhn;

import android.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HoiThoaiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        String abc = "123";
        setTitle(abc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final EditText edtNoiDung = findViewById(R.id.edtNoiDung);
        Button btnGui = findViewById(R.id.btbGui);
        ListView list_Hoithoai = findViewById(R.id.list_Hoithoai);
        ArrayList<HoiThoai> hoiThoaiArrayList = new ArrayList<>();
        HoiThoaiAdapter hoiThoaiAdapter = new HoiThoaiAdapter(HoiThoaiActivity.this,R.layout.list_tin_nhan_item,hoiThoaiArrayList);
        list_Hoithoai.setAdapter(hoiThoaiAdapter);

        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}