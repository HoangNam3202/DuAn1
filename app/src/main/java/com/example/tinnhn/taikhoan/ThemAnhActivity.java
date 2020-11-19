package com.example.tinnhn.taikhoan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tinnhn.R;
import com.google.firebase.storage.FirebaseStorage;

public class ThemAnhActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_anh);
    }
}