package com.example.tinnhn.Call;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tinnhn.MainActivity;
import com.example.tinnhn.R;
import com.example.tinnhn.ThongBao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.tinnhn.taikhoan.LoginActivity;
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

public class SinchServices extends Service {
    private static final String APP_KEY = "16732ff2-dd27-4c5a-8301-6b693da2fef1";
    private static final String APP_SECRET = "xsw87CnBMUCqVL+bpwXAKw==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";

    public static final String CALL_ID = "77";

    private giaodiendichvu appGiaodiendichvu = new giaodiendichvu();
    private SinchClient appSinchClient;
    private String appIDNguoiDung;
    private CallClient callClient;
    Notification notification;

    private StartFailedListener mListener;
    private DatabaseReference mDatabase;
    String EmailUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {

        notification = createNotification();
        ThongBao();
        startForeground(11,notification);
        super.onCreate();
    }

    private Notification createNotification() {
        final String notificationChannelId = "Tin Nhắn";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            final NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, "Endless Service notifications channel", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription("Tin nhắn service");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
//        notificationIntent.putExtra(Utils.NAVPAGE, TAG);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, notificationChannelId);
        } else {
            notificationBuilder = new Notification.Builder(this);
        }
        notificationBuilder.setContentTitle("Nhắn Tin");
        notificationBuilder.setContentText("Luôn chờ cuộc gọi");
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setSmallIcon(R.drawable.logo_app);
        notificationBuilder.setTicker("what is this?");
        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setOngoing(true);
        notificationBuilder.setPriority(Notification.PRIORITY_DEFAULT); // for under android 26 compatibility
        return notificationBuilder.build();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "START":
                       startService();
                        break;
                    case "STOP":
                        stopService();
                        break;
                    case "":

                        break;
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (appSinchClient != null && appSinchClient.isStarted()) {
            appSinchClient.stopListeningOnActiveConnection();
            appSinchClient.terminate();
        }
        super.onDestroy();
    }
    private void startService() {
        notification = createNotification();
        startForeground(11,notification);
    }
    private void stopService() {
stopForeground(true);
//stopSelf();
    }

    private void start(String userName) {
//        notification = createNotification();
//        startForeground(11,notification);
        if (appSinchClient == null) {
            appIDNguoiDung = userName;
            appSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            appSinchClient.setSupportCalling(true);
            appSinchClient.startListeningOnActiveConnection();
            appSinchClient.setSupportManagedPush(true);
           appSinchClient.setSupportActiveConnectionInBackground(true);

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

        public Call calluser(String userId) {
            return appSinchClient.getCallClient().callUser(userId);
        }

        public Call callGroup(String userId) {
            return appSinchClient.getCallClient().callConference(userId);
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
            //startActivity(intent);
            SinchServices.this.startActivity(intent);
        }
    }

    public void ThongBao(){
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        Intent intent = new Intent(this,MainActivity.class);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mDatabase.child("ThongBao").addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ThongBao cVu = snapshot.getValue(ThongBao.class);
                if(cVu.emailNguoiNhan.equals(EmailUser)){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "CHANNEL_ID")
                            .setSmallIcon(R.drawable.logo_app)
                            .setContentTitle(cVu.tenNguoiGui)
                            .setContentText(cVu.message_User)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL));
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
                    notificationManager.notify(0, builder.build());
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