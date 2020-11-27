package com.example.tinnhn.Call;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tinnhn.R;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

public class testcall extends BaseActivity implements SinchServices.StartFailedListener {

    private Button appNutLogin;
    private EditText appTenUser;
    private ProgressDialog appTienTrinh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testcall);

        //asking for permissions here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }

        //initializing UI elements
        appTenUser = (EditText) findViewById(R.id.loginName);
        appNutLogin = (Button) findViewById(R.id.loginButton);
        appNutLogin.setEnabled(false);
        appNutLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();
            }
        });
    }

    //this method is invoked when the connection is established with the SinchService
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        appNutLogin.setEnabled(true);
        getGiaodiendichvu().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (appTienTrinh != null) {
            appTienTrinh.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (appTienTrinh != null) {
            appTienTrinh.dismiss();
        }
    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    //Login is Clicked to manually to connect to the Sinch Service
    private void loginClicked() {
        String userName = appTenUser.getText().toString();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getGiaodiendichvu().isStarted()) {
            getGiaodiendichvu().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
        }
    }


    //Once the connection is made to the Sinch Service, It takes you to the next activity where you enter the name of the user to whom the call is to be placed
    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, Dialer.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
        appTienTrinh = new ProgressDialog(this);
        appTienTrinh.setTitle("Logging in");
        appTienTrinh.setMessage("Please wait...");
        appTienTrinh.show();
    }
}