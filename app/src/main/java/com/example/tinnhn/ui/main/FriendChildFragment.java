package com.example.tinnhn.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tinnhn.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FriendChildFragment extends Fragment {

    private View mRoot;
    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_friends_child,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListView list_friends_child = mRoot.findViewById(R.id.list_friends_child);
        ArrayList<String> arrFriends = new ArrayList<>();


        return mRoot;
    }
}
