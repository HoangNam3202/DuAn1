package com.example.tinnhn.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tinnhn.AddActivity;
import com.example.tinnhn.DatabaseHelper;
import com.example.tinnhn.R;
import com.example.tinnhn.TinNhan;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private View mRoot;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_main2,container,false);

        return mRoot;
    }
}
