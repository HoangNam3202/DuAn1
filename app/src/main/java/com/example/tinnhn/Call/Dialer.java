package com.example.tinnhn.Call;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.tinnhn.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;

public class Dialer extends BaseActivity {

    private Button appNutGoi,appNutGoi2;
    private EditText appTentarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);

        //initializing UI elements
        appTentarget = (EditText) findViewById(R.id.callName);
        appNutGoi = (Button) findViewById(R.id.callButton);
        appNutGoi2 = (Button) findViewById(R.id.callButton2);
        appNutGoi.setEnabled(false);
        appNutGoi2.setEnabled(false);
        appNutGoi.setOnClickListener(buttonClickListener);
        appNutGoi2.setOnClickListener(buttonClickListener);

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);
    }

    // invoked when the connection with SinchServer is established
    @Override
    protected void onServiceConnected() {
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(getGiaodiendichvu().getUserName());
        appNutGoi.setEnabled(true);
        appNutGoi2.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        if (getGiaodiendichvu()!= null) {
            getGiaodiendichvu().stopClient();
        }
        super.onDestroy();
    }

    //to kill the current session of SinchService
    private void stopButtonClicked() {
        if (getGiaodiendichvu() != null) {
            getGiaodiendichvu().stopClient();
        }
        finish();
    }

    //to place the call to the entered name
    private void callButtonClicked() {
        String userName = appTentarget.getText().toString();
        if (userName==null) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getGiaodiendichvu().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CuocGoi_Screen.class);
        callScreen.putExtra(SinchServices.CALL_ID, callId);
        startActivity(callScreen);
    }

    private void callButtonClicked2() {
        String userName = appTentarget.getText().toString();
        if (userName==null) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getGiaodiendichvu().calluser(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, AudioCall.class);
        callScreen.putExtra(SinchServices.CALL_ID, callId);
        startActivity(callScreen);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    callButtonClicked();
                    break;
                case R.id.callButton2:
                    callButtonClicked2();
                    break;

                case R.id.stopButton:
                    stopButtonClicked();
                    break;

            }
        }
    };
}
