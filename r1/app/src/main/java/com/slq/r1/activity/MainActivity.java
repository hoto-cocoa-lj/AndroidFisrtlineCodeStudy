package com.slq.r1.activity;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.slq.r1.R;
import com.slq.r1.service.MyService1;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     //<----------------modify here
        Button goa2 = (Button) findViewById(R.id.goa2);
        goa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent("666.test666");
                startActivityForResult(intent, 1);
            }
        });
        Button tel1 = (Button) findViewById(R.id.tel1);
        tel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10000"));
                startActivity(intent);
            }
        });

        Button notice1 = (Button) findViewById(R.id.notice1);
        notice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder;
                long[] vibrationPattern = new long[]{111, 222, 333};
                String soundFile = "/storage/emulated/0/音乐/the apple is cast.mp3";
                Uri soundUri = Uri.fromFile(new File(soundFile));
                //判断是否是8.0Android.O
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "myid";
                    String channelName = "myname";
                    NotificationChannel chan1 = new NotificationChannel(channelId,
                            channelName, NotificationManager.IMPORTANCE_MAX);

//                    chan1.enableLights(true);
//                    chan1.setLightColor(Color.GREEN);
//                    chan1.enableVibration(true);
//                    chan1.setVibrationPattern(vibrationPattern);
//                    chan1.setSound(soundUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);

                    chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                    manager.createNotificationChannel(chan1);
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId);
                } else {
                    mBuilder = new NotificationCompat.Builder(getApplicationContext());
                }
                String content1 = "这是测试通知内容\n这是测试通知内容\n这是测试通知内容" +
                        "\n这是测试通知内容\n这是测试通知内容\n这是测试通知内容\n";
                Bitmap chinokokoa = BitmapFactory.decodeResource(getResources(), R.mipmap.chinokokoa);
                Intent it = new Intent(getApplicationContext(), MainActivity3.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, it, 0);
                mBuilder.setContentTitle("这是测试通知标题")  //设置标题
                        .setContentText(content1) //设置内容
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText(content1))
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(chinokokoa))

                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setWhen(System.currentTimeMillis() - 1000 * 60 * 60)  //设置时间
                        .setSmallIcon(R.mipmap.ic_launcher)  //设置小图标
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setVibrate(vibrationPattern)
                        //.setSound(soundUri)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));   //设置大图标
                Notification notify = mBuilder.build();
                manager.notify(666, notify);
            }
        });
        Button gotomusic = findViewById(R.id.gotomusic);
        gotomusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MusicActivity.class));
            }
        });
        Button gotomusicwithservice = findViewById(R.id.gotomusicwithservice);
        gotomusicwithservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MusicWithServiceActivity.class));
            }
        });

        Button gotomovie = findViewById(R.id.gotomovie);
        gotomovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MovieActivity.class));
            }
        });
        Button gotoNewsActivity = findViewById(R.id.gotoNewsActivity);
        gotoNewsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyContactsListActivity.class));
            }
        });

        Button gotoOkhttpActivity = (Button) findViewById(R.id.okhttpActivity);
        gotoOkhttpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OkhttpActivity.class);
                startActivity(intent);
            }
        });
        Button asynctaskActivity = (Button) findViewById(R.id.asynctaskActivity);
        asynctaskActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AsyncTaskDemoActivity.class);
                startActivity(intent);
            }
        });
        Button myService1Activity = (Button) findViewById(R.id.myService1Activity);
        myService1Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService1Activity.class);
                startActivity(intent);
            }
        });
        Button downloaderActivity = (Button) findViewById(R.id.downloaderActivity);
        downloaderActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloaderActivity.class);
                startActivity(intent);
            }
        });
        Button toolbarActivity = (Button) findViewById(R.id.toolbarActivity);
        toolbarActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToolbarActivity.class);
                startActivity(intent);
            }
        });
        Button downloaderActivityWithHandler = (Button) findViewById(R.id.downloaderActivityWithHandler);
        downloaderActivityWithHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloaderActivityWithHandler.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG, "onActivityResult: " + requestCode + ":" + resultCode + ":" + data.getStringExtra("re"));
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(this, "ADD IT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "DEL IT", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}