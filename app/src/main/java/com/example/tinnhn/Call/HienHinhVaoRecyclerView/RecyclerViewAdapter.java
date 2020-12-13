package com.example.tinnhn.Call.HienHinhVaoRecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinnhn.Call.MicStatus;
import com.example.tinnhn.R;
import com.example.tinnhn.taikhoan.DBFirebase;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    String TAG = "RecyclerViewAdapter";
    private ArrayList<String> mNames = new ArrayList<>();
    private String tenNhomChat;
    private Context mContext;
    private String idGroup = "";
    private SharedPreferences sharedPreferences1,sharedPreferences2,sharedPreferences3;
    private SharedPreferences.Editor editor;
    private String j,emailsv;

    public RecyclerViewAdapter(Context context, ArrayList<String> mNames, String tenNhomChat) {
        this.mNames = mNames;
        this.mContext = context;
        this.tenNhomChat = tenNhomChat;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        sharedPreferences1 =mContext.getSharedPreferences("tengroup", MODE_PRIVATE);
        sharedPreferences2 = mContext.getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        emailsv = sharedPreferences2.getString("tenTaiKhoan", "");
        j = sharedPreferences1.getString("idgroup", "");
        return new ViewHolder(view);
    }
    DatabaseReference databaseReference;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onCreateViewHolder: called.");

        if (j.equals("Chat Room 1")) idGroup = "Chatroom1";
        if (j.equals("Chat Room 2")) idGroup = "Chatroom2";
        databaseReference = FirebaseDatabase.getInstance().getReference();


            databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                    String haha=taiKhoan.getEmail();
                        if (taiKhoan.getEmail().equals(mNames.get(position))) {
                                Glide.with(mContext).asBitmap().load(taiKhoan.getHinhDaiDien()).into(holder.image);

                                databaseReference.child("GroupGoiDien" + idGroup).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        MicStatus micStatus =snapshot.getValue(MicStatus.class);
                                        if(micStatus.User_Email.equals(haha)){
                                            if(micStatus.MicStatus.equals("Micon")){
                                                holder.status.setImageResource(R.drawable.ic_micon);
                                                holder.status.setBackgroundResource(R.drawable.roundcorner3);
                                            }else{
                                                holder.status.setImageResource(R.drawable.ic_micoff);
                                                holder.status.setBackgroundResource(R.drawable.roundcorner4);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




//        Glide.with(mContext).asBitmap().load(mImageUrls.get(position)).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + mNames.get(position));
                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image,status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            status = itemView.findViewById(R.id.onoff);
        }
    }
}
