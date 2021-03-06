package com.example.tinnhn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tinnhn.Call.BaseActivity;
import com.example.tinnhn.Call.CuocGoi_Screen;
import com.example.tinnhn.Call.SinchServices;
import com.example.tinnhn.translateapi.Language;
import com.example.tinnhn.translateapi.TranslateAPI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HoiThoaiActivity extends BaseActivity implements SinchServices.StartFailedListener {
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String TenNguoiGui, EmailUser, EmailNguoiGui;
    String TenUser;
    public static HoiThoaiAdapter hoiThoaiAdapter;
    ListView list_Hoithoai;

    String key;
    boolean check_TinNhanTonTai;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent_Friends = getIntent();
        TenNguoiGui = intent_Friends.getStringExtra("TenNguoiGui");

        setTitle(TenNguoiGui);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.user);

        sharedPreferences = getSharedPreferences("GhiNhoDangNhap", MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final EditText edtNoiDung = findViewById(R.id.edtNoiDung);
        Button btnGui = findViewById(R.id.btbGui);
        list_Hoithoai = findViewById(R.id.list_Hoithoai);

        final ArrayList<HoiThoai> hoiThoaiArrayList = new ArrayList<>();
        final ArrayList<HoiThoai> forArr = new ArrayList<>();
        ArrayList<TinNhanHienThi> messageArrayList_Message1 = new ArrayList<>();
        ArrayList<String> StringKey = new ArrayList<>();
        hoiThoaiAdapter = new HoiThoaiAdapter(HoiThoaiActivity.this, R.layout.list_tin_nhan_item, hoiThoaiArrayList);
        list_Hoithoai.setAdapter(hoiThoaiAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        EmailUser = sharedPreferences.getString("tenTaiKhoan", "");
        EmailNguoiGui = intent_Friends.getStringExtra("EmailNguoiGui");
        TenUser = sharedPreferences.getString("tenUser", "");



        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        String formattedDate = df.format(c.getTime());
//        Toast.makeText(this, ""+formattedDate, Toast.LENGTH_SHORT).show();
        scrollMyListViewToBottom();

        mDatabase.child("TinNhan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TinNhanHienThi message = snapshot.getValue(TinNhanHienThi.class);
                messageArrayList_Message1.clear();
                messageArrayList_Message1.add(message);
                for (int i = 0; i < messageArrayList_Message1.size(); i++) {
                    if (messageArrayList_Message1.get(i).email_User.equals(EmailUser) && messageArrayList_Message1.get(i).emailNguoiNhan.equals(EmailNguoiGui) || messageArrayList_Message1.get(i).email_User.equals(EmailNguoiGui) && messageArrayList_Message1.get(i).emailNguoiNhan.equals(EmailUser)) {
                        check_TinNhanTonTai = true;
                        key = snapshot.getKey();
                        break;
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
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtNoiDung.getText().toString().equals("")) {
                    final HoiThoai hoiThoai = new HoiThoai(edtNoiDung.getText().toString(), EmailNguoiGui, EmailUser);
                    mDatabase.child("HoiThoai").push().setValue(hoiThoai);
                    if (check_TinNhanTonTai == true) {
                        mDatabase.child("TinNhan").child(key).child("message_User").setValue(edtNoiDung.getText().toString());
                    } else {
                        TinNhanHienThi tinNhanHienThi = new TinNhanHienThi(null, edtNoiDung.getText().toString(), EmailNguoiGui, EmailUser, TenNguoiGui, TenUser);
                        mDatabase.child("TinNhan").push().setValue(tinNhanHienThi);
                    }
                    TinNhanHienThi tinNhanHienThi = new TinNhanHienThi(null, edtNoiDung.getText().toString(), EmailNguoiGui, EmailUser, TenNguoiGui, TenUser);
                    mDatabase.child("ThongBao").push().setValue(tinNhanHienThi);
                    edtNoiDung.setText("");
                } else {
                    Toast.makeText(HoiThoaiActivity.this, "Message is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mDatabase.child("HoiThoai").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoiThoai hThoai = snapshot.getValue(HoiThoai.class);
                forArr.clear();
                forArr.add(new HoiThoai(hThoai.message_User, hThoai.emailNguoiNhan, hThoai.email_User));
                for (int i = 0; i < forArr.size(); i++) {
                    if (forArr.get(i).email_User.equals(EmailUser) && forArr.get(i).emailNguoiNhan.equals(EmailNguoiGui)) {
                        hoiThoaiArrayList.add(new HoiThoai(forArr.get(i).message_User, forArr.get(i).emailNguoiNhan, forArr.get(i).email_User));
                    }
                    if (forArr.get(i).email_User.equals(EmailNguoiGui) && forArr.get(i).emailNguoiNhan.equals(EmailUser)) {
                        hoiThoaiArrayList.add(new HoiThoai(forArr.get(i).message_User, forArr.get(i).emailNguoiNhan, forArr.get(i).email_User));
                    }
                }
                hoiThoaiAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                hoiThoaiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        list_Hoithoai.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(hoiThoaiArrayList.get(position).getEmailNguoiNhan().equals(EmailUser)){
                    View popupView = getLayoutInflater().inflate(R.layout.list_popup, null);
                    final PopupWindow popup = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

                    TextView tv_dich=popupView.findViewById(R.id.tv_dich);
                    TranslateAPI translateAPI = new TranslateAPI(
                            Language.AUTO_DETECT,
                            Language.VIETNAMESE, hoiThoaiArrayList.get(position).getMessage_User());

                    translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                        @Override
                        public void onSuccess(String translatedText) {
                            tv_dich.setText(translatedText);
                        }

                        @Override
                        public void onFailure(String ErrorText) {

                        }
                    });

                    popup.setBackgroundDrawable(new BitmapDrawable());
                    popup.setOutsideTouchable(true);
                    popup.update(0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    if(hoiThoaiArrayList.get(position).getEmailNguoiNhan().equals(EmailUser)){
                        popup.setAnimationStyle(R.style.popup_window_animationright);
                        popup.showAsDropDown(view,0,-200,Gravity.RIGHT);
//                    }else{
//                        popup.setAnimationStyle(R.style.popup_window_animationleft);
//                        popup.showAsDropDown(view,0,-200,Gravity.LEFT);
//                    }


//                    if(hoiThoaiArrayList.get(position+1).getEmailNguoiNhan().equals(EmailUser)){
//                        popup.showAsDropDown(view,0,0,Gravity.RIGHT);
//                    }else{
//                        popup.showAsDropDown(view,0,0,Gravity.LEFT);
//                    }
                    popup.update();
                }else{
                    //donothing
                }

                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.callbutton, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.audio) {
            //đang nhap sinchclient o day = UserEmail sau do goi cho EmailNguoiGui

            if (getGiaodiendichvu().isStarted()) {

                openPlaceCallActivity();

            } else {
                Toast.makeText(this, "chua chay dich vu", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.video) {
            if (getGiaodiendichvu().isStarted()) {

                openPlaceCallVideoActivity();

            } else {
                Toast.makeText(this, "chua chay dich vu", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPlaceCallActivity() {
        String username = EmailNguoiGui;
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();

        Call call = getGiaodiendichvu().calluser(username);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CuocGoi_Screen.class);
        callScreen.putExtra(SinchServices.CALL_ID, callId);
        startActivity(callScreen);

//        Intent mainActivity = new Intent(this, Dialer.class);
//        startActivity(mainActivity);
    }

    private void openPlaceCallVideoActivity() {
        String username = EmailNguoiGui;
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();

        Call call = getGiaodiendichvu().callUserVideo(username);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CuocGoi_Screen.class);
        callScreen.putExtra(SinchServices.CALL_ID, callId);
        startActivity(callScreen);

//        Intent mainActivity = new Intent(this, Dialer.class);
//        startActivity(mainActivity);
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }

    private void scrollMyListViewToBottom() {
        list_Hoithoai.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                list_Hoithoai.setSelection(hoiThoaiAdapter.getCount() - 1);
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(HoiThoaiActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}