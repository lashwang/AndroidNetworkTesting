package com.example.simon.androidnetworktesting;

import android.support.annotation.Nullable;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Socket;

import socks5lib.client.Socks5;
import socks5lib.client.SocksProxy;
import socks5lib.client.SocksSocket;

import static socks5lib.utils.ResourceUtil.close;

public class Socks5ConnectivityChecker {


    private static final String url = "http://seven-china.com/health";
    private static final String TAG = "Socks5";
    private static final String proxyHost = "lashwang.top";
    private static final int proxyPort = 2016;
    private static final String user = "seven";
    private static final String password = "seven";


    public void run(String url){
        int length = 0;
        byte[] buffer = new byte[2048];
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Socket socket = null;

        try {
            SocksProxy proxy = new Socks5(
                    new InetSocketAddress(proxyHost, proxyPort), user, password);
            socket = new SocksSocket(proxy, new InetSocketAddress(url, 80));
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            while ((length = inputStream.read(buffer)) > 0) {
                Log.d(TAG, new String(buffer, 0, length));
            }
        }catch (Exception e){
            Log.e(TAG,"get error",e);
        }finally {
            close(inputStream, outputStream, socket);
        }
    }

    public static void check(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    new Socks5ConnectivityChecker().run(url);
                }catch (Exception e){
                    Log.d(TAG,"get error:",e);
                }
            }
        }).start();

    }


}
