package com.webdealsoft.Tiara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.webdealsoft.Tiara.Activity.Home;
import com.webdealsoft.Tiara.Registeration.LoginActivity;

public class Splash extends AppCompatActivity {
   // int splashWaitingTime = 5 * 500;
    String PREFS_NAME = "auth_info";
    int splashWaitingTime = 2 * 1000;// 3 seconds

    boolean connected = false;

    private static SharedPreferences sharedPreferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(Splash.this, LoginActivity.class);
                    Splash.this.startActivity(intent);
                    Splash.this.finish();
            }
        }, splashWaitingTime);
*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getString("userid", "").equals("")) {
                    Intent localIntent2 = new Intent(Splash.this,LoginActivity.class);
                    startActivity(localIntent2);
                    finish();
                } else {
                    Intent localIntent2 = new Intent(Splash.this, Home.class);
                    startActivity(localIntent2);
                    finish();
                }
            }
        }, splashWaitingTime);
    }


}
