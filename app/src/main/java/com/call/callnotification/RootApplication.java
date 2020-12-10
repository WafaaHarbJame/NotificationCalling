package com.call.callnotification;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;
import com.call.callnotification.Classes.SharedPManger;



public class RootApplication extends Application {

    private static RootApplication rootApplication;

    SharedPManger sharedPManger;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized RootApplication getInstance() {
        return rootApplication;
    }

    public  SharedPManger getSharedPManger() {
        return sharedPManger;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        rootApplication = this;
        sharedPManger = new SharedPManger(this);
        AndroidNetworking.initialize(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, new Intent(this,MessageService.class));
        } else {
            this.startService( new Intent(this,MessageService.class));
        }


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



}

