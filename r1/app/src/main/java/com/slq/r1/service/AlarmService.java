package com.slq.r1.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import com.slq.r1.app.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmService extends Service {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    AlarmManager alarmManager;

    public AlarmService() {
    }

    @Override
    public void onCreate() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = MyApplication.getContext();
        String format = simpleDateFormat.format(new Date());
        Toast.makeText(context, format, Toast.LENGTH_SHORT).show();
        repeat();
        return super.onStartCommand(intent, flags, startId);
    }

    void repeat() {
        long time2 = System.currentTimeMillis() + 1000 * 10;
        Intent intent1 = new Intent(this, AlarmService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent1, 0);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, time2, pi);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time2, pi);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}