package com.example.tinnhn.Call;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.tinnhn.R;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import java.util.List;

public class CuocGoiToi_Screen extends BaseActivity {
    private String appIDNguoiGoi;
    private AudioPlayer mAudioPlayer;
    DatabaseReference databaseReference;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuoc_goi_toi__screen);
        //ánh xạ
        LottieAnimationView Nghe = findViewById(R.id.nghedien);
        LottieAnimationView Tuchoi = findViewById(R.id.tuchoi);
        ImageView ava=findViewById(R.id.avatar);



        //nhạc chuông
        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        //end nhạc chuông
        appIDNguoiGoi = getIntent().getStringExtra(SinchServices.CALL_ID);
        //end ánh xạ
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getEmail().equals(getGiaodiendichvu().getCall(appIDNguoiGoi).getRemoteUserId())) {
                    Glide.with(getBaseContext()).asBitmap().load(taiKhoan.getHinhDaiDien()).into(ava);
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

        //nút answer
        Nghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //audio
                nghe();
            }
        });

        //nút decline
        Tuchoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuchoi();
            }
        });
    }

//hàm check service connection
    @Override
    protected void onServiceConnected() {
        Call call = getGiaodiendichvu().getCall(appIDNguoiGoi);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            Toast.makeText(this, call.getRemoteUserId(), Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
    //end hàm check service connection

    //hàm bắt máy
    private void nghe() {
        mAudioPlayer.stopRingtone();
        Call call = getGiaodiendichvu().getCall(appIDNguoiGoi);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CuocGoi_Screen.class);
            intent.putExtra(SinchServices.CALL_ID, appIDNguoiGoi);
            startActivity(intent);
        } else {
            finish();
        }
    }
    //end hàm bắt máy

    //hàm từ chối
    private void tuchoi() {
        // mAudioPlayer.stopRingtone();
        mAudioPlayer.stopRingtone();
        Call call = getGiaodiendichvu().getCall(appIDNguoiGoi);
        if (call != null) {
            call.hangup();
        }
        finish();
    }
    //end hàm từ chối

//hàm bắt sự kiện Sinch
    private class SinchCallListener implements VideoCallListener {
        //hàm show chi tiết cuộc gọi khi bị end+stop nhạc chuông
        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            mAudioPlayer.stopRingtone();
            finish();
        }
        @Override
        public void onCallEstablished(Call call) {

        }

        @Override
        public void onCallProgressing(Call call) {
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
        @Override
        public void onVideoTrackAdded(Call call) {
        }

        @Override
        public void onVideoTrackPaused(Call call) {
        }
        @Override
        public void onVideoTrackResumed(Call call) {
        }
    }
}