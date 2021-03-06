package com.e.toolplusstore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import com.e.toolplusstore.databinding.ActivityMainBinding;

import com.e.toolplusstore.databinding.OfflineActivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    ActivityMainBinding binding;
    OfflineActivityBinding offlineActivityBinding;
    FirebaseUser currentUser;
    InternetConnectivity connectivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        binding = ActivityMainBinding.inflate(inflater);
        View v = binding.getRoot();
        setContentView(v);
        connectivity = new InternetConnectivity();
    }
    /*@Override
    protected void onStart() {
        super.onStart();
        if (!connectivity.isConnected(MainActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Please connect to the Internet to Proceed Further").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    startActivity(in);
                }
            });
            builder.show();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentUser != null) {
                        sendUserToHomeScreen();
                    } else {
                        sendUserToLoginScreen();
                    }
                }
            }, 3000);
         }
     }
    */

    @Override
    protected void onStart() {
        super.onStart();
        checkInternetConnection();
    }

    private void checkInternetConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        changeActivity(isConnected);
    }

    private void changeActivity(boolean isConnected) {
        if(isConnected){
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            setContentView(binding.getRoot());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentUser != null) {
                        sendUserToHomeScreen();
                    } else {
                        sendUserToLoginScreen();
                    }
                }
            }, 3000);
        }
        else {
            offlineActivityBinding = OfflineActivityBinding.inflate(LayoutInflater.from(this));
            setContentView(offlineActivityBinding.getRoot());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver,intentFilter);
        MyApp.getInstance().setConnectivityListener(this);
    }
    private void sendUserToHomeScreen() {
        startActivity(new Intent(MainActivity.this,HomeActivity.class));
        finish();
    }
    private void sendUserToLoginScreen() {
        Intent in = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        changeActivity(isConnected);
    }
}