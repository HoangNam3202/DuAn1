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
public class BanBePagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.ban_be, R.string.them_ban_be};
    private final Context mContext;
    private FriendChildFragment friendChildFragment;
    private AddFriendChildFragment addFriendChildFragment;


    public BanBePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        friendChildFragment = new FriendChildFragment();
        addFriendChildFragment = new AddFriendChildFragment();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return friendChildFragment;
        } else if (position == 1) {
            return addFriendChildFragment;
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}