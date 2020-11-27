package com.example.tinnhn.Call;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tinnhn.R;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import java.util.List;

public class CuocGoiToi_Screen extends BaseActivity {
    private String appIDNguoiGoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuoc_goi_toi__screen);

        LottieAnimationView Nghe = findViewById(R.id.nghedien);

        LottieAnimationView Tuchoi = findViewById(R.id.tuchoi);

        // appAudioPlayer=new AudioPlayer(this);

        appIDNguoiGoi = getIntent().getStringExtra(SinchServices.CALL_ID);
        Toast.makeText(this, appIDNguoiGoi, Toast.LENGTH_SHORT).show();

        Nghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //audio
                nghe();
            }
        });
//       Nghe2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //audio
//                nghe2();
//            }
//        });
        Tuchoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuchoi();
            }
        });
    }


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

    private void nghe() {
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

    private void nghe2() {
        Call call = getGiaodiendichvu().getCall(appIDNguoiGoi);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, AudioCall.class);
            intent.putExtra(SinchServices.CALL_ID, appIDNguoiGoi);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void tuchoi() {
        // mAudioPlayer.stopRingtone();
        Call call = getGiaodiendichvu().getCall(appIDNguoiGoi);
        if (call != null) {
            call.hangup();
        }
        finish();

    }


    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            //Log.d(TAG, "Call ended, cause: " + cause.toString());
            //mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            //  Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            // Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            // Display some kind of icon showing it's a video call
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }
}