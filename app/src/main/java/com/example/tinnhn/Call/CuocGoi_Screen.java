package com.example.tinnhn.Call;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.tinnhn.HoiThoai;
import com.example.tinnhn.R;
import com.example.tinnhn.ThongBao;
import com.example.tinnhn.TinNhanHienThi;
import com.example.tinnhn.taikhoan.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CuocGoi_Screen extends BaseActivity {

    static final String TAG = CuocGoi_Screen.class.getSimpleName();
    static final String CALL_START_TIME = "callStartTime";
    static final String ADDED_LISTENER = "addedListener";
    private Timer appTimer;
    int check, check2 = 1;
    private UpdateCallDurationTask mDurationTask;
    private String appCallId, tennguoigoi;
    private long mCallStart = 0;
    private boolean mAddedListener = false;
    private boolean mVideoViewsAdded = false;
    private TextView mCallDuration;
    private TextView mCallState, calling;
    private TextView mCallerName;
    ImageButton camoffButton, mute;
    DatabaseReference databaseReference;
    ImageView hinh;
    LottieAnimationView anima;
    AudioManager audioManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String EmailUser;

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CuocGoi_Screen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(CALL_START_TIME, mCallStart);
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCallStart = savedInstanceState.getLong(CALL_START_TIME);
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuoc_goi__screen);
        appCallId = getIntent().getStringExtra(SinchServices.CALL_ID);
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setMicrophoneMute(false);

        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TaiKhoan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                if (taiKhoan.getEmail().equals(getGiaodiendichvu().getCall(appCallId).getRemoteUserId())) {
                    Glide.with(getBaseContext()).asBitmap().load(taiKhoan.getHinhDaiDien()).into(hinh);

                }
                if (taiKhoan.getEmail().equals(getGiaodiendichvu().getUserName())) {
                    tennguoigoi = taiKhoan.getTenTaiKhoan();
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


        new CountDownTimer(20000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Call call = getGiaodiendichvu().getCall(appCallId);

                if (call.getState() == CallState.PROGRESSING) {
                        String calldetail =
                                "You have a missed call from " + tennguoigoi + ".";
                        final HoiThoai hoiThoai = new HoiThoai(calldetail, EmailUser, call.getRemoteUserId());
                        databaseReference.child("HoiThoai").push().setValue(hoiThoai);
                        ThongBao thongBao = new ThongBao(null,calldetail,call.getRemoteUserId(),EmailUser,"null",tennguoigoi);
                        databaseReference.child("ThongBao").push().setValue(thongBao);
                        NotiCall notiCall = new NotiCall(call.getRemoteUserId(),EmailUser);
                        databaseReference.child("NotiCall").push().setValue(notiCall);
                        endCall();
                    }


            }
        }.start();


        //ánh xạ
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        hinh = findViewById(R.id.nghedien);
        calling = findViewById(R.id.callingtext);
        anima = findViewById(R.id.animation);
        mute = findViewById(R.id.mute);
        //mCallState = (TextView) findViewById(R.id.callState);
        ImageButton endCallButton = (ImageButton) findViewById(R.id.hangupButton);
        ImageButton flipButton = (ImageButton) findViewById(R.id.flipcamera);
        camoffButton = (ImageButton) findViewById(R.id.offcam);

//end ánh xạ

        anima.setAnimation(R.raw.dots);

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mute();
            }
        });

        //hàm quay camera
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipcam();
            }
        });
        //end hàm quay camera

        //hàm tắt camera
        camoffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camoff();
            }
        });
//end hàm tắt camera

        //Nút hàm dừng cuộc goi
        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = getGiaodiendichvu().getCall(appCallId);
//                CallEndCause cause = call.getDetails().getEndCause();
                if (call.getState() == CallState.ESTABLISHED) {
                    String calldetail =
                            "Your call have ended. \n"
                                    + "duration: " + call.getDetails().getDuration() + " sec.";
                    final HoiThoai hoiThoai = new HoiThoai(calldetail, call.getRemoteUserId(), EmailUser);
                    databaseReference.child("HoiThoai").push().setValue(hoiThoai);
                    endCall();
                } else {
                    String calldetail =
                            "You have a missed call from " + tennguoigoi + ".";
                    final HoiThoai hoiThoai = new HoiThoai(calldetail, EmailUser, call.getRemoteUserId());
                    databaseReference.child("HoiThoai").push().setValue(hoiThoai);
                    ThongBao thongBao = new ThongBao(null,calldetail,call.getRemoteUserId(),EmailUser,"null",tennguoigoi);
                    databaseReference.child("ThongBao").push().setValue(thongBao);
                    NotiCall notiCall = new NotiCall(call.getRemoteUserId(),EmailUser);
                    databaseReference.child("NotiCall").push().setValue(notiCall);
                    endCall();
                }


            }
        });

        appCallId = getIntent().getStringExtra(SinchServices.CALL_ID);
        if (savedInstanceState == null) {
            mCallStart = System.currentTimeMillis();
        }
    }
    //end hàm dừng cuộc goi

    //hàm check service connection
    @Override
    public void onServiceConnected() {
        Call call = getGiaodiendichvu().getCall(appCallId);
        if (call != null) {
            if (!mAddedListener) {
                call.addCallListener(new SinchCallListener());
                mAddedListener = true;
            }
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }

        updateUI();
    }
    //end hàm check service connection

    //hàm update UI của màn hình gọi
    private void updateUI() {
        if (getGiaodiendichvu() == null) {
            return; // early
        }

        Call call = getGiaodiendichvu().getCall(appCallId);
        if (call != null) {
            mCallerName.setText(call.getRemoteUserId());
            // mCallState.setText(call.getState().toString());
            calling.setText(call.getState().toString());
            if (call.getState() == CallState.ESTABLISHED) {
                //when the call is established, addVideoViews configures the video to  be shown
                addVideoViews();
            }
        }
    }

    //end hàm update UI của màn hình gọi

    //hàm bắt đầutính time,tắt camera view
    @Override
    public void onStart() {
        super.onStart();
        appTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        appTimer.schedule(mDurationTask, 0, 500);

        updateUI();
    }
    //end hàm bắt đầutính time,tắt camera view

    //hàm dừng tính time,tắt camera view
    @Override
    public void onStop() {
        super.onStop();
        mDurationTask.cancel();
        appTimer.cancel();
        removeVideoViews();
    }
//end hàm dừng tính time,tắt camera view


    //hàn ngừng cuộc gọi
    private void endCall() {
        //mAudioPlayer.stopProgressTone();
//        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss a",Locale.ENGLISH);
        Call call = getGiaodiendichvu().getCall(appCallId);
//        long starttimestamp=call.getDetails().getEstablishedTime();
//        Date starttime=new Date((starttimestamp));
//        long endtimestamp=call.getDetails().getEndedTime();
//        Date endtime=new Date((endtimestamp));
//        String starttime2=df.format(starttime);
//        String endtime2=df.format(endtime);


//                +"started at: "+starttime2+"\nended at: "
//+endtime2;
        if (call != null) {
            call.hangup();
//            Toast.makeText(this, call.getRemoteUserId(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this,EmailUser, Toast.LENGTH_SHORT).show();

        }
        finish();
    }
    //end hàn ngừng cuộc gọi
//    private void actionOnService(Actions actions) {
//
//        Intent intent = new Intent(this, SinchServices.class);
//        intent.setAction(actions.name());
//        this.startService(intent);
//
//
//
//    }
    //hàm đổi thời gian
    private String formatTimespan(long timespan) {
        long totalSeconds = timespan / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }
    //end hàm đổi thời gian

    //hàm chạy tính thời gian khi gọi
    private void updateCallDuration() {
        if (mCallStart > 0) {
            mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
        }
    }

    //end hàm chạy tính thời gian khi gọi

    //hàm thêm video view
    private void addVideoViews() {
        if (mVideoViewsAdded || getGiaodiendichvu() == null) {
            return; //early
        }

        final VideoController vc = getGiaodiendichvu().getVideoController();
        if (vc != null) {
            RelativeLayout localView = (RelativeLayout) findViewById(R.id.localVideo);
            localView.addView(vc.getLocalView());

            localView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //this toggles the front camera to rear camera and vice versa
                    vc.toggleCaptureDevicePosition();
                }
            });

            RelativeLayout view = (RelativeLayout) findViewById(R.id.remoteVideo);
//
            view.addView(vc.getRemoteView());

            mVideoViewsAdded = true;
        }
    }

    //end hàm thêm video view
    //hàm tắt tiếng
    private void mute() {

        if (check2 == 1) {
            mute.setImageResource(R.drawable.ic_volume);
            audioManager.setMicrophoneMute(true);
            check2 = 2;
        } else if (check2 == 2) {
            mute.setImageResource(R.drawable.ic_mute);
//            AudioController audioController = getGiaodiendichvu().getAudioController();
//            audioController.unmute();
            audioManager.setMicrophoneMute(false);
            check2 = 1;
        }

    }
    //end hàm tắt tiếng


    //chức năng quay cam
    private void flipcam() {
        final VideoController vc = getGiaodiendichvu().getVideoController();
        vc.toggleCaptureDevicePosition();
    }

    //chức năng tắt cam
    private void camoff() {

        final VideoController vc = getGiaodiendichvu().getVideoController();
        RelativeLayout view = (RelativeLayout) findViewById(R.id.localVideo);
        Call call = getGiaodiendichvu().getCall(appCallId);
        if (check == 1) {
            view.removeView(vc.getLocalView());
            call.pauseVideo();
            camoffButton.setImageResource(R.drawable.ic_videocam_off);
            check = 2;
        } else if (check == 2) {
            view.addView(vc.getLocalView());
            camoffButton.setImageResource(R.drawable.ic_videocall);
            call.resumeVideo();
            check = 1;
        }


        ;
    }

    //chức năng nỏ video view
    private void removeVideoViews() {
        if (getGiaodiendichvu() == null) {
            return; // early
        }

        VideoController vc = getGiaodiendichvu().getVideoController();
        if (vc != null) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.remoteVideo);
            view.removeView(vc.getRemoteView());

            RelativeLayout localView = (RelativeLayout) findViewById(R.id.localVideo);
            localView.removeView(vc.getLocalView());
            mVideoViewsAdded = false;
        }
    }

    //hàm sự kiện của Sinch client
    private class SinchCallListener implements VideoCallListener {
        // lúc end cuộc gọi,hàm toast các thông số về cuộc gọi
        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            // mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

            endCall();
        }

        //lúc bắt đầu cuộc gọi
        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            // mAudioPlayer.stopProgressTone();
            //mCallState.setText(call.getState().toString());
            calling.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = getGiaodiendichvu().getAudioController();
            //audioController.enableSpeaker();
            mCallStart = System.currentTimeMillis();
            Log.d(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
        }

        //lúc cuộc gọi đang tiến hành
        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");

            //mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            Log.d(TAG, "Video track added");
            addVideoViews();
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }
    //end hàm sự kiện Sinch
}