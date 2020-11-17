package com.example.tinnhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Friends> friendsList;
    private DatabaseReference mDatabase;

    public FriendsAdapter(Context context, int layout, List<Friends> friendsList) {
        this.context = context;
        this.layout = layout;
        this.friendsList = friendsList;
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView tvTenFriends = view.findViewById(R.id.tvTenFriends);

        Friends friends = friendsList.get(i);
        tvTenFriends.setText(friends.tenTaiKhoan);

        return view;
    }
}