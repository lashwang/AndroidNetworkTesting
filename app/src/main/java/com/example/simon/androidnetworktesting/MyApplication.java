package com.example.simon.androidnetworktesting;

import android.app.Application;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyApplication","onCreate");
        AndroidNetworking.initialize(getApplicationContext());

    }
}
