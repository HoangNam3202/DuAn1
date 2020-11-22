package com.example.tinnhn.Call;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;
import com.sinch.android.rtc.video.VideoScalingType;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SinchServices extends Service {
    private static final String APP_KEY = "16732ff2-dd27-4c5a-8301-6b693da2fef1";
    private static final String APP_SECRET = "xsw87CnBMUCqVL+bpwXAKw==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";

    public static final String CALL_ID = "77";

    private giaodiendichvu appGiaodiendichvu = new giaodiendichvu();
    private SinchClient appSinchClient;
    private String appIDNguoiDung;
    private CallClient callClient;

    private StartFailedListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (appSinchClient != null && appSinchClient.isStarted()) {
            appSinchClient.terminate();
        }
        super.onDestroy();
    }

    private void start(String userName) {
        if (appSinchClient == null) {
            appIDNguoiDung = userName;
            appSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            appSinchClient.setSupportCalling(true);
            appSinchClient.startListeningOnActiveConnection();

            appSinchClient.addSinchClientListener(new MySinchClientListener());
            appSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            appSinchClient.getVideoController().setResizeBehaviour(VideoScalingType.ASPECT_FILL);
            appSinchClient.start();
        }
    }

    private void stop() {
        if (appSinchClient != null) {
            appSinchClient.terminate();
            appSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (appSinchClient != null && appSinchClient.isStarted());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return appGiaodiendichvu;
    }

    public class giaodiendichvu extends Binder {

        public Call callUserVideo(String userId) {
            return appSinchClient.getCallClient().callUserVideo(userId);
        }

        public Call calluser(String userId){
            return appSinchClient.getCallClient().callUser(userId);
        }

        public String getUserName() {
            return appIDNguoiDung;
        }

        public boolean isStarted() {
            return SinchServices.this.isStarted();
        }

        public void startClient(String userName) {
            start(userName);
        }

        public void stopClient() {
            stop();
        }

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            return appSinchClient.getCallClient().getCall(callId);
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }
            return appSinchClient.getVideoController();
        }

        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return appSinchClient.getAudioController();
        }
    }

    public interface StartFailedListener {

        void onStartFailed(SinchError error);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            appSinchClient.terminate();
            appIDNguoiDung = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {

        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            Intent intent = new Intent(SinchServices.this, CuocGoiToi_Screen.class);
            intent.putExtra(CALL_ID, call.getCallId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SinchServices.this.startActivity(intent);
        }
    }

}