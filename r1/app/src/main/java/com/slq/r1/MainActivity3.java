package com.slq.r1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.provider.Settings;
import java.io.InputStream;

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
import android.provider.MediaStore;
import android.widget.ListView;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Build;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageView;

import java.io.File;

public class MainActivity3 extends AppCompatActivity {
    private static final String TAG = "MainActivity3";
    R1BroadcastReceiver cr;
    IntentFilter inf;
    final int TAKEPHONE = 777;
    final int ALBUM = 777 + 1;
    Uri imgUri;
    ImageView iv;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        cr = new R1BroadcastReceiver();
        inf = new IntentFilter();
        //inf.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        inf.addAction("FROMOTHERAPP");
        inf.setPriority(11);
        registerReceiver(cr, inf);

        iv = findViewById(R.id.phoneiv);
        Button phonebutton = (Button) findViewById(R.id.phonebutton);
        phonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file = new File(getExternalCacheDir(), "hehe.jpg");
                if (file.exists()) {
                    file.delete();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String fp = "666.Myfileprovider";
                if (Build.VERSION.SDK_INT >= 24) {
                    imgUri = FileProvider.getUriForFile(getApplicationContext(), fp, file);
                } else {
                    imgUri = Uri.fromFile(file);
                }

                int i = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                if (i != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity3.this,
                            new String[]{Manifest.permission.CAMERA}, 666);
                } else {
                    Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
                    it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    Log.e(TAG, "imgUri: " + imgUri);
                    startActivityForResult(it, TAKEPHONE);
                }
            }
        });

        Button albumbutton = (Button) findViewById(R.id.albumbutton);
        albumbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (i != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity3.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 777);
                } else {
                    Intent it = new Intent("android.intent.action.GET_CONTENT");
                    it.setType("image/*");
                    startActivityForResult(it, ALBUM);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 666) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
                it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(it, TAKEPHONE);
            } else {
                Toast.makeText(this, "你拒绝授权照相机", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == 777) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent it = new Intent("android.intent.action.GET_CONTENT");
//                it.setType("image/*");
                Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(it, ALBUM);
            } else {
                Toast.makeText(this, "你拒绝授权相册", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKEPHONE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream is = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    iv.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        if (requestCode == ALBUM) {
            if (resultCode == RESULT_OK) {
                Uri data1 = data.getData();
                Log.e(TAG, "onActivityResult: data1" + data1);
                InputStream is = null;
                try {
                    is = getContentResolver().openInputStream(data1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                iv.setImageBitmap(bitmap);
            }
            return;
        }
    }

    class R1BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long t = intent.getLongExtra("time", 0L);
            Toast.makeText(context, "r1 receive:" + t, Toast.LENGTH_SHORT).show();
            //abortBroadcast();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cr);
    }
}