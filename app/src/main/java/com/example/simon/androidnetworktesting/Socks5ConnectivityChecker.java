package com.example.simon.androidnetworktesting;

import android.support.annotation.Nullable;
import android.util.Log;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Socket;

public class Socks5ConnectivityChecker {


    private static final String url = "http://seven-china.com/health";
    private static final String TAG = "Socks5";
    private static final String proxyHost = "lashwang.top";
    private static final int proxyPort = 2016;
    private static final String user = "seven";
    private static final String password = "seven";

    public void run(String url) throws Exception{
    }

    public static void check(){
        try {
            new Socks5ConnectivityChecker().run(url);
        }catch (Exception e){
            Log.d(TAG,"get error:",e);
        }

    }


}
