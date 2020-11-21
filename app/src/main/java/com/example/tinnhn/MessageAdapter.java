package com.example.tinnhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<TinNhanHienThi> messageList;
    private DatabaseReference mDatabase;

    public MessageAdapter(Context context, int layout, List<TinNhanHienThi> messageList) {
        this.context = context;
        this.layout = layout;
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
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
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        String formattedDate = df.format(c.getTime());

        TextView tvTenFriends = view.findViewById(R.id.tvTenFriends);
        TextView tvtinnhanFriends = view.findViewById(R.id.tvtinnhanFriends);
        TextView tvthoiGian = view.findViewById(R.id.tvthoiGian);

        TinNhanHienThi message = messageList.get(i);
        tvTenFriends.setText(message.tenNguoiGui);
        tvtinnhanFriends.setText(message.message_User);
        tvthoiGian.setText("• "+formattedDate);
        return view;
    }
}