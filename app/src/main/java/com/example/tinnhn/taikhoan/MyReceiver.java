package com.example.tinnhn.taikhoan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;
import static com.example.tinnhn.taikhoan.LoginActivity.kTraMang;

import com.example.tinnhn.MainActivity;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        kTraMang = CheckInternet.getConnectivityStatusString(context);
    }
}
