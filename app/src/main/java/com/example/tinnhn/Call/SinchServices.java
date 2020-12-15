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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.tinnhn.FriendsRequest;
import com.example.tinnhn.HoiThoaiActivity;
import com.example.tinnhn.MainActivity;
import com.example.tinnhn.R;
import com.example.tinnhn.ThongBao;
import com.example.tinnhn.ui.main.FriendChildFragment;
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
    private static final String APP_KEY = "16732ff2-dd27-4c5a-8301-6b693da2fef1";//key client của tài khoản Sinch
    private static final String APP_SECRET = "xsw87CnBMUCqVL+bpwXAKw==";//key client của tài khoản Sinch
    private static final String ENVIRONMENT = "clientapi.sinch.com";
    public static final String CALL_ID = "77";
    private giaodiendichvu appGiaodiendichvu = new giaodiendichvu();
    private SinchClient appSinchClient;
    private String appIDNguoiDung;
    Notification notification;

    private StartFailedListener mListener;
    private DatabaseReference mDatabase;
    String EmailUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        notification = createNotification();//tạo noti
        ThongBao();//noti tin nhắn
        LoiMoi();
        startForeground(11,notification);//chạy foreground
        super.onCreate();
    }
//hàm tạo noti
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
        Intent notificationIntent = new Intent(this, LoginActivity.class);
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
    //end hàm tạo noti

    //hàm hứng trạng thái của foreground
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
                    case "DISS":
                        stop();
                        break;
                }
            }
        }
        return START_STICKY;
    }
    //end hàm hứng trạng thái của foreground


    @Override
    public void onDestroy() {
        if (appSinchClient != null && appSinchClient.isStarted()) {
            appSinchClient.stopListeningOnActiveConnection();
            appSinchClient.terminate();
        }
        super.onDestroy();
    }

    //hàm bắt đầu chạy fore ground+ kết thức foreground
    private void startService() {
        notification = createNotification();
        startForeground(11,notification);
    }
    private void stopService() {
        stopForeground(true);

    }
    //end hàm bắt đầu chạy fore ground+ kết thức foreground

    //hàm chạy Sinch Client :dùng để bắt đầu tạo service cho các cuộc gọi, hàm này nếu không chạy=>không gọi được
    private void start(String userName) {
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
    //end hàm chạy client

    private void stop() {
        if (appSinchClient != null) {
            appSinchClient.terminate();
            appSinchClient = null;
        }
    }
//hàm check trạng thái của service
    private boolean isStarted() {
        return (appSinchClient != null && appSinchClient.isStarted());
    }
    //end hàm check trạng thái của service


    @Override
    public IBinder onBind(Intent intent) {
        return appGiaodiendichvu;
    }

    public class giaodiendichvu extends Binder {

        public Call callUserVideo(String userId) {
            return appSinchClient.getCallClient().callUserVideo(userId);//dùng chạy service gọi video
        }

        public Call calluser(String userId) {
            return appSinchClient.getCallClient().callUser(userId);//dùng chạy service gọi audio
        }

        public Call callGroup(String userId) {
            return appSinchClient.getCallClient().callConference(userId);//dùng chạy service gọi nhóm
        }

        public String getUserName() {
            return appIDNguoiDung;
        }//dùng chạy service lấy tên ng gọi

        public boolean isStarted() {
            return SinchServices.this.isStarted();
        }//dùng chạy service lấy trạng thái service

        public void startClient(String userName) {
            start(userName);
        }//dùng chạy service lấy tên ng dùng

        public void stopClient() {
            stop();
        }//dùng chạy service dùng để dừng client

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            return appSinchClient.getCallClient().getCall(callId);//dùng chạy service dùng để lấy ID cuộc gọi thường dùng để dừng cuộc gọi 1 vs 1
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }
            return appSinchClient.getVideoController();//dùng chạy service dùng để lấy info từ video:hình ảnh,tắt cam, mở cam , xoay cam...
        }

        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return appSinchClient.getAudioController();//dùng chạy service dùng để lấy info từ audio:tắt tiếng mở tiếng vv.v.v
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

    //hàm hứng sự kiện của Sinch service
    private class SinchCallClientListener implements CallClientListener {
        //hàm hứng cuộc gọi tới
        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            Intent intent = new Intent(SinchServices.this, CuocGoiToi_Screen.class);
            intent.putExtra(CALL_ID, call.getCallId());//lấy thông tin ng gọi đưa vào màn hình cuộc gọi tới: lấy ID, usernamevv.vv
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SinchServices.this.startActivity(intent);
        }
    }
    //end hàm hứng sự kiện của Sinch service

    //hàm thông báo tin nhắn tới vào noti
    public void ThongBao(){
//        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        Intent intent = new Intent(this, HoiThoaiActivity.class);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        mDatabase.child("ThongBao").addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ThongBao cVu = snapshot.getValue(ThongBao.class);
                if(cVu.emailNguoiNhan.equals(EmailUser)){
                    intent.putExtra("EmailNguoiGui",cVu.email_User);
                    intent.putExtra("TenNguoiGui",cVu.tenUser);
                    final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "Tin Nhắn")
                            .setSmallIcon(R.drawable.logo_app)
                            .setContentTitle(cVu.tenUser)
                            .setContentText(cVu.message_User)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
                    notificationManager.notify(11, builder.build());
                    AudioPlayer mAudioPlayer = new AudioPlayer(getBaseContext());
                    mAudioPlayer.playmestone();
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                    finally {
                        mAudioPlayer.stopmess();
                    }
                    String key = snapshot.getKey();
                    mDatabase.child("ThongBao").child(key).removeValue();
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
    //end hàm thông báo tin nhắn tới vào noti

    public void LoiMoi(){
//        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        Intent intent = new Intent(this, MainActivity.class);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
        Bitmap mIcon = BitmapFactory.decodeResource(getBaseContext().getResources(),R.drawable.logo_app);
        mDatabase.child("LoiMoiKetBan").addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FriendsRequest friendsRequest = snapshot.getValue(FriendsRequest.class);
                if(friendsRequest.EmailUser.equals(EmailUser)){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "Tin Nhắn")
                            .setSmallIcon(R.drawable.logo_app)
                            .setLargeIcon(mIcon)
                            .setContentTitle("Friends Request")
                            .setContentText(friendsRequest.tenTaiKhoan + " send a request for you !")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
                    notificationManager.notify(11, builder.build());
                    AudioPlayer mAudioPlayer = new AudioPlayer(getBaseContext());
                    mAudioPlayer.playmestone();
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                    finally {
                        mAudioPlayer.stopmess();
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