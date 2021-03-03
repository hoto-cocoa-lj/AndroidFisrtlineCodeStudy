package com.slq.r1.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.slq.r1.interfaces.DownloaderListener;
import com.slq.r1.R;
import com.slq.r1.activity.DownloaderActivity;
import com.slq.r1.utils.DownloaderTask;

public class DownloaderService extends Service {
    private static final String TAG = "DownloaderService";
    private DownloaderBinder binder = new DownloaderBinder();
    DownloaderTask downloaderTask;
    DownloaderListener downloaderListener = new MyDownloadListener();
    final int DownloaderServiceNotificationId = 1;
    NotificationManager manager;
    NotificationCompat.Builder mBuilder;
    String channelId = "id1";

    public class MyDownloadListener implements DownloaderListener {
        @Override
        public void onSucess() {
            downloaderTask = null;
            //stopForeground(true);      //todo
            Notification notification = getNotificationPart2("下载完毕", 1, 1);
            //int importance = manager.getNotificationChannel(channelId).getImportance();
            //Log.e(TAG, "importance: " + importance);
            manager.notify(DownloaderServiceNotificationId, notification);
            Toast.makeText(getApplicationContext(), "下载完毕", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPause() {
            downloaderTask = null;
            Toast.makeText(getApplicationContext(), "你已经暂停下载", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStop() {
            downloaderTask = null;
            manager.cancel(DownloaderServiceNotificationId);
            //stopForeground(true);      //todo
            Toast.makeText(getApplicationContext(), "你停止了下载", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFail() {
            downloaderTask = null;
            Notification notification = getNotificationPart2("下载失败", 0, 0);
            //int importance = manager.getNotificationChannel(channelId).getImportance();
            manager.notify(DownloaderServiceNotificationId, notification);
            //Toast.makeText(getApplicationContext(), "下载失败！！！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProgress(long... values) {
            Notification notification = getNotificationPart2("下载进度", (int) values[0], (int) values[1]);
            manager.notify(DownloaderServiceNotificationId, notification);
            //Log.e(TAG, "当前下载进度: " + values[0] + "/" + values[1]);
        }
    }

    private void getNotificationPart1() {
        Intent it = new Intent(this, DownloaderActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, it, 0);
        NotificationChannel chan1 = new NotificationChannel(channelId, "name1", NotificationManager.IMPORTANCE_MAX);
        manager.createNotificationChannel(chan1);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)  //设置小图标
                .setContentIntent(pi)
                //.setSound(null)
                //.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setNotificationSilent();
    }

    private Notification getNotificationPart2(String title, int... params) {
        mBuilder.setContentTitle(title)  //设置标题
                .setContentText("已下载" + params[0] + "/" + params[1])
                .setProgress(params[1], params[0], false);
        return mBuilder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        getNotificationPart1();
    }

    public DownloaderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class DownloaderBinder extends Binder {
        //如果去掉if，连续点两次会开启两个task
        public void startDownload(String url) {
            //如果没有task，或者task已经被暂停或停止，重新new task
            if (downloaderTask == null) {
                downloaderTask = new DownloaderTask();
                downloaderTask.setDownloaderListener(downloaderListener);
                downloaderTask.execute(url);
                //startForeground(DownloaderServiceNotificationId,getNotificationPart2("starting",1,1));      //todo
            }
        }

        public void pauseDownload() {
            if (downloaderTask != null) {
                downloaderTask.setState(DownloaderTask.PAUSE);
            }
        }

        //如果先暂停再停止会无效,需要对downloaderTask == null的情况进行处理
        public void stopDownload() {
            if (downloaderTask != null) {
                downloaderTask.setState(DownloaderTask.DELETE);
            }
        }

    }
}