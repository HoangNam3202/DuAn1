package com.example.tinnhn.Call;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private SinchServices.giaodiendichvu Giaodiendichvu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchServices.class), this,
                BIND_AUTO_CREATE);
    }

    //hàm check service connection
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchServices.class.getName().equals(componentName.getClassName())) {
            Giaodiendichvu = (SinchServices.giaodiendichvu) iBinder;
            onServiceConnected();
        }
        Giaodiendichvu = (SinchServices.giaodiendichvu) iBinder;
    }


    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchServices.class.getName().equals(componentName.getClassName())) {
            Giaodiendichvu = null;
            onServiceDisconnected();
        }
    }
    //end hàm check service connection

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchServices.giaodiendichvu getGiaodiendichvu() {
        return Giaodiendichvu;
    }

}