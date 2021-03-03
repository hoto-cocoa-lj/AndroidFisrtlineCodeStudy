package com.slq.r1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.slq.r1.R;

import java.util.Calendar;
import java.util.Random;

public class WeatherInfoActivity extends AppCompatActivity {
    DrawerLayout myDrawerLayout1;
    TextView textView;
    SwipeRefreshLayout mySwipeRefreshLayout1;
    String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        textView = findViewById(R.id.weather_info);
        initTextView();

        mySwipeRefreshLayout1 = findViewById(R.id.mySwipeRefreshLayoutLeftSide);
        myDrawerLayout1 = findViewById(R.id.weatherInfoDrawerLayout1);
        //设置SwipeRefreshLayout，给recyclerView实现下拉刷新效果
        mySwipeRefreshLayout1.setColorSchemeResources(R.color.design_default_color_primary);
        mySwipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1111);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        initTextView();
                        mySwipeRefreshLayout1.setRefreshing(false);
                    }
                }).start();
            }
        });
    }

    void initTextView() {
        Calendar now = Calendar.getInstance();
        int a = now.get(Calendar.YEAR);
        int b = now.get(Calendar.MONTH) + 1;
        int c = now.get(Calendar.DAY_OF_MONTH);
        StringBuffer sb = new StringBuffer(name).append("\n\n");
        int[] ints = new Random().ints(30, -5, 22).toArray();
        for (int i = 0; i < 15; i++) {
            int max = Math.max(ints[i * 2], ints[i * 2 + 1]);
            int min = Math.min(ints[i * 2], ints[i * 2 + 1]);
            sb.append("\t\t").append(a).append("/").append(b).append("/").append(c + i).append("\t\t");
            sb.append(min).append(" ^C~ ").append(max).append(" ^C\n");
        }
        textView.setText(sb.toString());
    }

    public void fix(String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setName(name);
                initTextView();
                //mySwipeRefreshLayout1.setRefreshing(false);
                myDrawerLayout1.closeDrawer(Gravity.START);
            }
        });

    }
}