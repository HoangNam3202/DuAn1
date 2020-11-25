package com.example.tinnhn.taikhoan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tinnhn.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ThemTinhThanhActivity extends AppCompatActivity {

    EditText edtTinhThanh;
    Button btnThemTinhThanh;
    DBFirebase dbFirebase = new DBFirebase();
    Spinner spnTinhThanh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tinh_thanh);
        edtTinhThanh = findViewById(R.id.edtTinhThanh);
        btnThemTinhThanh = findViewById(R.id.btnThemTinhThanh);
//        btnThemTinhThanh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                TinhThanh tinhThanh = new TinhThanh(RandomString(4), edtTinhThanh.getText().toString().trim());
//                String tinhThanh = edtTinhThanh.getText().toString().trim();
//                dbFirebase.ThemTinhThanh(tinhThanh);
//                edtTinhThanh.setText("");
//            }
//        });
        ChonTinhThanh();

    }

    private void ChonTinhThanh() {
        spnTinhThanh = findViewById(R.id.spnTinhThanh);
//        stringArrayList.add("abc");
//        stringArrayList.add("ert");
//        stringArrayList.add("dfg");
//        ArrayList<String> strings = dbFirebase.LayDanhSachTinhThanh2();
//        Toast.makeText(this, "" + strings.size(), Toast.LENGTH_SHORT).show();
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strings);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnTinhThanh.setAdapter(adapter);
        spnTinhThanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btnThemTinhThanh.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        tinhThanhs = dbFirebase.LayDanhSachTinhThanh();
//        Toast.makeText(this, ""+tinhThanhs.size(), Toast.LENGTH_SHORT).show();
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