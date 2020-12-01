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
        final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                kTraMang = 1;
                intent = new Intent(context,LoginActivity.class);
                context.sendBroadcast(intent);
            }
            else {
                kTraMang = 0;
            }
        }
    }
}
