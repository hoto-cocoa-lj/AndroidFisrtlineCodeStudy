package com.slq.r1;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.Manifest;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Build;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.net.Uri;

import com.slq.r1.adapter.MyArrayAdapter;
import com.slq.r1.pojo.News;

import android.app.Notification;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Button gotonews = (Button) findViewById(R.id.gotonews);
        gotonews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.startActivity0(MainActivity.this);
            }
        });

        ListView lv = findViewById(R.id.testListView);
        ArrayList<News> objs = requestPermiIfNeed();
        MyArrayAdapter adapter = new MyArrayAdapter(getApplicationContext(), R.layout.newsitem, objs);
        lv.setAdapter(adapter);

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
                            channelName, NotificationManager.IMPORTANCE_DEFAULT);

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
    }

    public ArrayList<News> requestPermiIfNeed() {
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (i != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 666);
            return null;
        } else {
            return readContacts();
        }
    }

    public ArrayList<News> readContacts() {
        ArrayList<News> objs = new ArrayList<News>();
        ContentResolver resolver = getContentResolver();
        int i = 1;
        Cursor query = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                String name = query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        + "\n\t\t" + query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //Log.e(TAG, "readContacts: " + name);
                objs.add(new News(name, i++));
            }
        }
        return objs;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 666) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                Toast.makeText(this, "你拒绝授权", Toast.LENGTH_SHORT).show();
            }
        }
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