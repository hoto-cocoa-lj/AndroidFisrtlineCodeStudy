package com.slq.r1.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.slq.r1.R;
import com.slq.r1.activity.MusicWithServiceActivity;

import java.io.IOException;

public class MyMusicService extends Service {
    private static final String TAG = "MyMusicService";
    private static final int SERVICEID = 1;
    private static final String CHANNELID = "CHANNELID";
    MediaPlayer mp = new MediaPlayer();
    MusicBinder binder = new MusicBinder();

    public MyMusicService() {
        Log.e(TAG, "MyMusicService: create");
    }

    public class MusicBinder extends Binder {
        Uri uri;

        public void start() {
            Log.e(TAG, "start: " + MyMusicService.this);
            if (!mp.isPlaying()) mp.start();
        }

        public void pause() {
            if (mp.isPlaying()) mp.pause();
        }

        public void stop() {
            if (mp.isPlaying()) mp.stop();
            initMusic();
        }

        public void select(Uri uri) {
            this.uri = uri;
            initMusic();
        }

        private void initMusic() {
            if (uri == null) return;
            try {
                mp.reset();
                mp.setDataSource(getApplicationContext(), uri);
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {          //单曲循环播放
                mp.start();
            }
        });
        setForegroundService();
    }

    private void setForegroundService() {
        String CHANNEL_ONE_ID = "com.primedu.cn";
        String CHANNEL_ONE_NAME = "Channel One";
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(this, MusicWithServiceActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Bitmap chinokokoa = BitmapFactory.decodeResource(getResources(), R.mipmap.chinokokoa);
        mBuilder.setChannelId(CHANNEL_ONE_ID)
                .setContentTitle("usagi music")  //设置标题
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(chinokokoa))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())  //设置时间
                .setSmallIcon(R.mipmap.chinokokoa)  //设置小图标
                .setContentIntent(pi)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.chinokokoa));   //设置大图标
        Notification notify = mBuilder.build();
        startForeground(SERVICEID, notify);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}