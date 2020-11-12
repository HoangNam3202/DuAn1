package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    boolean check = false;

    database databasehelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initPreferences();
        databasehelper = new database(this,"SQL",null,2);

        final EditText edtTen = findViewById(R.id.edtTen);
        final EditText edtMatKhau = findViewById(R.id.edtMatKhau);
        Button btnGo = findViewById(R.id.btnGo);

        edtMatKhau.setTransformationMethod(PasswordTransformationMethod.getInstance());;

        String savedData = sharedPreferences.getString("DATA", "");
        edtTen.setText(savedData);

        String savedDataPass = sharedPreferences.getString("Pass", "");
        edtMatKhau.setText(savedDataPass);

        if(savedData != "" && savedDataPass != "" ){
            Intent intent = new Intent(MainActivity2.this,MainActivity.class);
            startActivity(intent);
        }


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data = edtTen.getText().toString();

                String pass = edtMatKhau.getText().toString();
                Double mk = Double.parseDouble(pass);

                if(data.equals("")){
                    edtTen.setError("Rỗng !");
                }
                else if(pass.equals(""))
                {
                    edtMatKhau.setError("Rỗng !");
                }
                else
                {
                    Cursor cursor = databasehelper.GetData("select * from SinhVien where Tensv = '"+edtTen.getText().toString()+"' AND id = '"+edtMatKhau.getText().toString()+"'");
//                        while (cursor.moveToNext()) {
//                            Double id = cursor.getDouble(0);
//                            String tenSV = cursor.getString(1);
//                            Log.d("ThongTin", id + tenSV);
//                            Log.d("ThongTin", data + mk);
                            if (cursor.getCount() > 0) {
                                editor.putString("DATA", data);
                                editor.putString("Pass", pass);
                                editor.commit();
                                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity2.this, "Tên hoặc mật khẩu không chính xác !", Toast.LENGTH_LONG).show();
                            }
//                        }

                    }

                }


        });


//        edtMatKhau.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_RIGHT = 2;
//
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    if(event.getRawX() >= (edtMatKhau.getRight() - edtMatKhau.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                       edtMatKhau.setInputType(1);
//                        return true;
//                    }
//
//                }
//                edtMatKhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                return false;
//            }
//
//        });

    }

    private void initPreferences() {
        sharedPreferences = getSharedPreferences("mylogin",MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}