package com.e.toolplusstore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;


public class InternetConnectivity  {

    public static boolean isConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnect = manager.getNetworkInfo(manager.TYPE_WIFI);
        NetworkInfo mobileConnect = manager.getNetworkInfo(manager.TYPE_MOBILE);
        if ((wifiConnect != null && wifiConnect.isConnected()) || (mobileConnect != null && mobileConnect.isConnected())) {

            return true;
        } else {

            return false;
        }
    }

}



