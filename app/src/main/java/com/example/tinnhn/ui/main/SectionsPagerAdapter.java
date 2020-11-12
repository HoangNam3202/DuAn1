package com.example.tinnhn.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tinnhn.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tin_nhan, R.string.danh_sach, R.string.group, R.string.ca_nhan};
    private final Context mContext;
    private PlaceholderFragment placeholderFragment;
    private SecondFragment secondFragment;
    private GroupFragment groupFragment;
    private SettingFragment settingFragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        placeholderFragment = new PlaceholderFragment();
        secondFragment = new SecondFragment();
        groupFragment = new GroupFragment();
        settingFragment = new SettingFragment();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return placeholderFragment;
        } else if (position == 1) {
            return secondFragment;
        } else if (position == 2) {
            return groupFragment;
        } else if (position == 3) {
            return settingFragment;
        }
        return null;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mContext.getResources().getString(TAB_TITLES[position]);
//    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}