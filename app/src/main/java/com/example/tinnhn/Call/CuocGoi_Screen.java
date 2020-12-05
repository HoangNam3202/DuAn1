package com.example.tinnhn.Call;

import android.media.AudioManager;
import android.os.Bundle;
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

import com.example.tinnhn.R;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CuocGoi_Screen extends BaseActivity {

    static final String TAG = CuocGoi_Screen.class.getSimpleName();
    static final String CALL_START_TIME = "callStartTime";
    static final String ADDED_LISTENER = "addedListener";
    private Timer appTimer;
    int check = 1;
    private UpdateCallDurationTask mDurationTask;
    private String appCallId;
    private long mCallStart = 0;
    private boolean mAddedListener = false;
    private boolean mVideoViewsAdded = false;
    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    ImageButton camoffButton;

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

   //ánh xạ
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        mCallState = (TextView) findViewById(R.id.callState);
        ImageButton endCallButton = (ImageButton) findViewById(R.id.hangupButton);
        ImageButton flipButton = (ImageButton) findViewById(R.id.flipcamera);
       camoffButton = (ImageButton) findViewById(R.id.offcam);
//end ánh xạ

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
                endCall();
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
            mCallState.setText(call.getState().toString());
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
        Call call = getGiaodiendichvu().getCall(appCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }
    //end hàn ngừng cuộc gọi

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
            String endMsg = "Call ended: " + call.getDetails().toString();
            Toast.makeText(CuocGoi_Screen.this, endMsg, Toast.LENGTH_LONG).show();

            endCall();
        }

//lúc bắt đầu cuộc gọi
        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            // mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = getGiaodiendichvu().getAudioController();
            audioController.enableSpeaker();
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