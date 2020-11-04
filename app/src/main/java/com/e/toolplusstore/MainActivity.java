package com.e.toolplusstore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.e.toolplusstore.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    String currentUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        binding = ActivityMainBinding.inflate(inflater);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myanimation);
        binding.logo.startAnimation(animation);
        View v = binding.getRoot();
        setContentView(v);
        if (!isConnected(MainActivity.this)) {
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
            }, 5000);
        }
    }

    private void sendUserToHomeScreen() {
        Toast.makeText(this, "Home Screen", Toast.LENGTH_SHORT).show();
    }

    private void sendUserToLoginScreen() {
        Intent in = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(in);
    }

    private boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager manager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnect = manager.getNetworkInfo(manager.TYPE_WIFI);
        NetworkInfo mobileConnect = manager.getNetworkInfo(manager.TYPE_MOBILE);
        if ((wifiConnect != null && wifiConnect.isConnected()) || (mobileConnect != null && mobileConnect.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}