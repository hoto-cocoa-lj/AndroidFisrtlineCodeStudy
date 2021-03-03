package com.slq.r1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slq.r1.pojo.Weather;
import com.slq.r1.utils.UrlUtil;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class T1 {

    @Test
    public void test111() {

        Callback callback = new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String string = response.body().string();
                System.out.println(string);
                Gson gson = new Gson();
                List<String> strings = response.request().url().pathSegments();
                String[] parents=new String[strings.size()-2];
                for(int i=0;i<strings.size()-2;i++){
                    parents[i]=strings.get(i+2);
                }
                Type type = new TypeToken<ArrayList<Weather>>() {}.getType();
                ArrayList<Weather> weathers = gson.fromJson(string, type);
                System.out.println(weathers);
                System.out.println(Arrays.toString(parents));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        };
        UrlUtil.get("http://guolin.tech/api/china/16/116", callback);
        try {
            Thread.sleep(55555);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void asd12(){
        int[] ints = new Random().ints(15, -5,22).toArray();
        System.out.println(Arrays.toString(ints));
    }
    @Test
    public void asd132(){
        SimpleDateFormat sdf=new SimpleDateFormat();
        File file=new File("F:\\centos7用redis-3.0.0.tar.gz");
        long l = file.lastModified();
        Date date = new Date(l);
        boolean b = sdf.format(date).equals(sdf.format(new Date()));

        System.out.println(b);
    }
}

