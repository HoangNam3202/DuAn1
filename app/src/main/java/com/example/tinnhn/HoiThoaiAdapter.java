package com.example.tinnhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HoiThoaiAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<HoiThoai> hoiThoaiList;

    public HoiThoaiAdapter(Context context, int layout, List<HoiThoai> hoiThoaiList) {
        this.context = context;
        this.layout = layout;
        this.hoiThoaiList = hoiThoaiList;
    }

    @Override
    public int getCount() {
        return hoiThoaiList.size();
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

        HoiThoai hoiThoai = hoiThoaiList.get(i);


        return view;
    }
}
