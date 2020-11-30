package com.example.tinnhn.taikhoan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class KiemTraMang {
    public int CheckNetworkStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWF = networkInfo.isConnected();
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean is4G = networkInfo.isConnected();
        if (isWF) {
            return 1;
        } else if (is4G) {
            return 2;
        } else {
            return 0;
        }
    }
}
