package com.example.tinnhn.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tinnhn.R;
import com.google.android.material.tabs.TabLayout;


public class FriendsFragment extends Fragment {

    private View mRoot;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_friends, container, false);
        BanBePagerAdapter sectionsPagerAdapter = new BanBePagerAdapter(getContext(), getChildFragmentManager());
        ViewPager viewPager = mRoot.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = mRoot.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return mRoot;
    }
}
