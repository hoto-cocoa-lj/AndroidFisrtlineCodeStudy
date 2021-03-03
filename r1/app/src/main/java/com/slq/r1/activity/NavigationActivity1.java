package com.slq.r1.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.slq.r1.R;
import com.slq.r1.service.AlarmService;

import java.io.Serializable;

public class NavigationActivity1 extends AppCompatActivity {
    Button alarm_service;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation1);
        Serializable fruit = getIntent().getSerializableExtra("fruit");
        System.out.println(fruit);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_service = findViewById(R.id.alarm_service);
        alarm_service.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callAlarm1();
                    }
                }
        );
    }

    void callAlarm() {
        int time1 = 1000 * 6;
        long time2 = System.currentTimeMillis();
        Intent it = new Intent(this, AlarmService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, it, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time2, time1, pi);
        startService(it);
    }

    void callAlarm1() {
        Intent it = new Intent(this, AlarmService.class);
        startService(it);
    }
}