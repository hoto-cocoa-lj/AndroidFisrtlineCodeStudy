package com.slq.r1.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.slq.r1.R;
import com.slq.r1.service.DownloaderService2;

public class DownloaderActivityWithHandler extends AppCompatActivity implements View.OnClickListener {
    final int DownloaderServiceNotificationId = 1;
    private final String TAG = "DownloaderActivityWithH";
    public NotificationManager manager;
    NotificationCompat.Builder mBuilder;
    String channelId = "id1";
    String DOWNLOADFILEURL = "https://dl.google.com/android/studio/plugins/android-gradle/preview/offline-android-gradle-plugin-preview.zip";
    Button binddownloadservice, unbinddownloadservice, startdownload, stopdownload, pausedownload;
    TextView downloadprogresstext, downloadurltext;
    String[] rights = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int RIGHTREWUESTCODE = 1;
    Messenger serviceMessenger;
    Bundle bundle;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);

            Messenger messenger = new Messenger(handler);
            // 创建消息
            Message msg = new Message();
            msg.what = -1;
            msg.replyTo = messenger;

            // 使用service中的messenger发送Activity中的messenger
            try {
                serviceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader_with_handler);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent it = new Intent(DownloaderActivityWithHandler.this, DownloaderService2.class);
        stopService(it);
        try {
            unbindService(connection);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        binddownloadservice = findViewById(R.id.binddownloadservice2);
        binddownloadservice.setOnClickListener(this);
        unbinddownloadservice = findViewById(R.id.unbinddownloadservice2);
        unbinddownloadservice.setOnClickListener(this);
        startdownload = findViewById(R.id.startdownload2);
        startdownload.setOnClickListener(this);
        stopdownload = findViewById(R.id.stopdownload2);
        stopdownload.setOnClickListener(this);
        pausedownload = findViewById(R.id.pausedownload2);
        pausedownload.setOnClickListener(this);
        downloadprogresstext = findViewById(R.id.downloadprogresstext2);
        downloadurltext = findViewById(R.id.downloadurltext2);
        downloadurltext.setText(DOWNLOADFILEURL);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        getNotificationPart1();
        bundle = new Bundle();
        bundle.putString("url", downloadurltext.getText().toString());
    }

    private void getNotificationPart1() {
        Intent it = new Intent(this, DownloaderActivityWithHandler.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, it, 0);
        NotificationChannel chan1 = new NotificationChannel(channelId, "name1", NotificationManager.IMPORTANCE_MAX);
        manager.createNotificationChannel(chan1);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setNotificationSilent();
    }

    private Notification getNotificationPart2(String title, int... params) {
        mBuilder.setContentTitle(title)  //设置标题
                .setContentText("已下载" + params[0] + "/" + params[1])
                .setProgress(params[1], params[0], false);
        return mBuilder.build();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle data = msg.getData();
                    int[] progresses = data.getIntArray("progress");
                    manager.notify(DownloaderServiceNotificationId, getNotificationPart2("下载进度", progresses[0], progresses[1]));
                    break;
                case 2:     //stop
                    manager.cancel(DownloaderServiceNotificationId);
                        break;
                case 3:
                    manager.cancel(DownloaderServiceNotificationId);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Message msg = new Message();

        switch (v.getId()) {
            case R.id.startdownload2:
                msg.what = 0;
                msg.setData(bundle);
                sendMsg(msg);
                break;
            case R.id.stopdownload2:
                msg.what = 1;
                sendMsg(msg);
                break;
            case R.id.pausedownload2:
                msg.what = 2;
                sendMsg(msg);
                break;
            case R.id.binddownloadservice2:
                Intent it = new Intent(DownloaderActivityWithHandler.this, DownloaderService2.class);
                startService(it);
                bindService(it, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbinddownloadservice2:
                unbindService(connection);
                break;
            default:
                break;
        }
    }

    private void sendMsg(Message msg) {
        try {
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RIGHTREWUESTCODE) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Message msg = new Message();
                msg.what = 0;
                msg.setData(bundle);
                sendMsg(msg);
            }
        }
    }
}