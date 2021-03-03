package com.slq.r1.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
public class UrlUtil  {
    public static void get(String url, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

}
