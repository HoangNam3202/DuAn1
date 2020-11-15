package com.example.tinnhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HoiThoaiAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<HoiThoai> hoiThoaiList;
    private DatabaseReference mDatabase;

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
        CardView card_view_Friend = view.findViewById(R.id.card_view);
        CardView card_view_User = view.findViewById(R.id.card_view1);

        HoiThoai hoiThoai = hoiThoaiList.get(i);
        if(hoiThoai.email_User.equals("abc")){
            tvUser_tin_nhan.setText(hoiThoai.message_User);
            tv_HoiThoaiBanCuaUser.setVisibility(View.GONE);
            imgAnh_Ban_Cua_User.setVisibility(View.GONE);
            card_view_Friend.setVisibility(View.GONE);
        }
        if(hoiThoai.email_User.equals("HoangNam")){
            tv_HoiThoaiBanCuaUser.setText(hoiThoai.message_User);
            tvUser_tin_nhan.setVisibility(View.GONE);
            imgAnh_User_tin_nhan.setVisibility(View.GONE);
            card_view_User.setVisibility(View.GONE);
        }

        return view;
    }
}
