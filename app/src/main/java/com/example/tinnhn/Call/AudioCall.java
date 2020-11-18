package com.example.tinnhn.Call;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.tinnhn.R;
import com.sinch.android.rtc.calling.Call;

public class AudioCall extends BaseActivity {
    private String appCallId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_call);
        appCallId= getIntent().getStringExtra(SinchServices.CALL_ID);

    }
    @Override
    public void onServiceConnected() {
        Call call = getGiaodiendichvu().getCall(appCallId);

    }
}