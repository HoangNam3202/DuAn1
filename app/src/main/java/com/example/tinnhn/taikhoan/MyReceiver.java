package com.example.tinnhn.taikhoan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import static com.example.tinnhn.taikhoan.LoginActivity.kTraMang;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        kTraMang = CheckInternet.getConnectivityStatusString(context);
    }
}
