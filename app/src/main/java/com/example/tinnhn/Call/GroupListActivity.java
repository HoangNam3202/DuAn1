package com.example.tinnhn.Call;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tinnhn.HoiThoai;
import com.example.tinnhn.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends AppCompatActivity {
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabase;
    String TenGroup;
    int IdGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);

        final EditText tengrp=findViewById(R.id.tengroup);
        Button them=findViewById(R.id.themgrp);
        ListView grplist=findViewById(R.id.grplv);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ArrayList<String> dsgrp = new ArrayList<>();

        mDatabase.child("Group").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String nhom=snapshot.getValue().toString();
                dsgrp.add(nhom);
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

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dsgrp);
        grplist.setAdapter(adapter);



        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group group =new Group(tengrp.getText().toString(),RandomString(9));
                mDatabase.child("Group").push().setValue(group);
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