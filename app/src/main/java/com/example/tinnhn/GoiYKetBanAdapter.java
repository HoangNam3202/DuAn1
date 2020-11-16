package com.example.tinnhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class GoiYKetBanAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<GoiYKetBan> goiYKetBanList;
    private DatabaseReference mDatabase;

    public GoiYKetBanAdapter(Context context, int layout, List<GoiYKetBan> goiYKetBanList) {
        this.context = context;
        this.layout = layout;
        this.goiYKetBanList = goiYKetBanList;
    }

    @Override
    public int getCount() {
        return goiYKetBanList.size();
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

        TextView tvTenGoiY = view.findViewById(R.id.tvTenGoiY);
        GoiYKetBan goiYKetBan = goiYKetBanList.get(i);

        tvTenGoiY.setText(goiYKetBan.tenTaiKhoan);

        return view;
    }
}