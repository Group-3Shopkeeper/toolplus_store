package com.e.toolplusstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {
    public static  ConnectivityReceiverListener connectivityReceiverListener;
    public  ConnectivityReceiver(){
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwrk = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwrk != null && activeNetwrk.isConnectedOrConnecting();
        if(connectivityReceiverListener!=null){
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }
    public static boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) MyApp.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwrk = cm.getActiveNetworkInfo();
        return  activeNetwrk != null && activeNetwrk.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener{
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
