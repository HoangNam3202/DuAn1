package com.example.tinnhn;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ArrayAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<TinNhan> tinNhanList;

    public ArrayAdapter(Context context, int layout, List<TinNhan> tinNhanList) {
        this.context = context;
        this.layout = layout;
        this.tinNhanList = tinNhanList;
    }

    @Override
    public int getCount() {
        return tinNhanList.size();
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



        TextView tvUser_tin_nhan = view.findViewById(R.id.tvHoiThoai_User_tin_nhan);
        TextView tv_HoiThoaiBanCuaUser = view.findViewById(R.id.tv_HoiThoaiBanCuaUser);
        ImageView imgAnh_User_tin_nhan = view.findViewById(R.id.imgAnh_User_tin_nhan);
        ImageView imgAnh_Ban_Cua_User = view.findViewById(R.id.imgAnh_Ban_Cua_User);

        TinNhan tinNhan = tinNhanList.get(i);
        tvUser_tin_nhan.setText(tinNhan.HoiThoai);
        tv_HoiThoaiBanCuaUser.setText(tinNhan.HoiThoai);

        return view;
    }
}
