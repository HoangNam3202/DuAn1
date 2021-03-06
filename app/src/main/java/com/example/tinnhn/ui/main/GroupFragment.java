package com.example.tinnhn.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tinnhn.Call.GroupHoiThoaiActivity;
import com.example.tinnhn.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GroupFragment extends Fragment {

    private View mRoot;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String TenGroup;
    int IdGroup;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.group_list, container, false);

        ListView grplist = mRoot.findViewById(R.id.grplv);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = this.getContext().getSharedPreferences("tengroup", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ArrayList<String> dsgrp = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, dsgrp);
        grplist.setAdapter(adapter);
        mDatabase.child("Group").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String nhom = snapshot.getValue().toString();
                dsgrp.add(nhom);
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
        grplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tenkey = dsgrp.get(position);
                Intent i = new Intent(getActivity(), GroupHoiThoaiActivity.class);
                i.putExtra("idgroup", tenkey);

                editor.putString("idgroup", tenkey);
                editor.commit();

                startActivity(i);
//                Toast.makeText(getActivity(), tenkey, Toast.LENGTH_SHORT).show();
//                mDatabase.child("Group").addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        String key = snapshot.getValue().toString();
//                        for (int i=0;i<=dsgrp.size();i++){
//                            if(dsgrp.get(position).equals(key)){
//                                String key2=snapshot.getKey();
//                                Toast.makeText(getActivity(), key2, Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
////
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        });

        return mRoot;
    }

}
