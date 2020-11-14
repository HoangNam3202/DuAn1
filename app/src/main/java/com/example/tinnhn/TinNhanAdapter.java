package com.example.tinnhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class TinNhanAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<HoiThoai> hoiThoaiList;

    public TinNhanAdapter(Context context, int layout, List<HoiThoai> hoiThoaiList) {
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

//        TextView txtTen = view.findViewById(R.id.txtTen);
//        TextView txtHoiThoai = view.findViewById(R.id.txtNgay);
//        ImageView img = view.findViewById(R.id.imgAnh);
//        TextView txtNgay = view.findViewById(R.id.txtGio);
//
//        TinNhan tinNhan = tinNhanList.get(i);
//        txtTen.setText(tinNhan.getTenNguoiDung());
//        txtHoiThoai.setText(tinNhan.getHoiThoai());
//        img.setImageResource(tinNhan.getHinh());
//        txtNgay.setText(tinNhan.getNgay());

        return view;
    }
}

