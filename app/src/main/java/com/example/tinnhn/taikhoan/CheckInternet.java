package com.example.tinnhn.taikhoan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternet {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = CheckInternet.getConnectivityStatus(context);
        int status = 0 ;
        if (conn == CheckInternet.TYPE_WIFI) {
            status = 1;
        } else if (conn == CheckInternet.TYPE_MOBILE) {
            status = 1;
        } else if (conn == CheckInternet.TYPE_NOT_CONNECTED) {
            status = 0;
        }
        return status;
    }
}
