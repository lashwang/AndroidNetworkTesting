package com.example.simon.androidnetworktesting;

import android.util.Log;


import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static sockslib.utils.ResourceUtil.close;

public class Socks5ConnectivityChecker {


    private static final String url = "http://seven-china.com/health";
    private static final String TAG = "Socks5";
    private static final String proxyHost = "lashwang.top";
    private static final int proxyPort = 2016;
    private static final String user = "seven";
    private static final String password = "seven";


    public void run(String url) throws Exception{
        URL netUrl = new URL(url);

        OkHttpClient client = new OkHttpClient.Builder()
                .socketFactory(new ProxySocketFactory(proxyHost,proxyPort,user,password))
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }
            }
        });
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
