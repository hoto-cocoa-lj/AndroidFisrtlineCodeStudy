package com.slq.r1.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.slq.r1.R;
import com.slq.r1.service.DownloaderService;
import com.slq.r1.utils.RightUtil;

import java.security.PermissionCollection;

public class DownloaderActivity extends AppCompatActivity implements View.OnClickListener {
    private DownloaderService.DownloaderBinder binder;
    //String DOWNLOADFILEURL="https://dl.google.com/dl/android/studio/install/3.5.2.0/android-studio-ide-191.5977832-windows.exe";
    String DOWNLOADFILEURL = "https://dl.google.com/android/studio/plugins/android-gradle/preview/offline-android-gradle-plugin-preview.zip";
    Button binddownloadservice, unbinddownloadservice, startdownload, stopdownload, pausedownload;
    TextView downloadprogresstext, downloadurltext;
    String[] rights = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int RIGHTREWUESTCODE = 1;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloaderService.DownloaderBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent it = new Intent(DownloaderActivity.this, DownloaderService.class);
        stopService(it);
        try {
            unbindService(connection);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        binddownloadservice = findViewById(R.id.binddownloadservice);
        binddownloadservice.setOnClickListener(this);
        unbinddownloadservice = findViewById(R.id.unbinddownloadservice);
        unbinddownloadservice.setOnClickListener(this);
        startdownload = findViewById(R.id.startdownload);
        startdownload.setOnClickListener(this);
        stopdownload = findViewById(R.id.stopdownload);
        stopdownload.setOnClickListener(this);
        pausedownload = findViewById(R.id.pausedownload);
        pausedownload.setOnClickListener(this);
        downloadprogresstext = findViewById(R.id.downloadprogresstext);
        downloadurltext = findViewById(R.id.downloadurltext);
        downloadurltext.setText(DOWNLOADFILEURL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startdownload:
                boolean b = RightUtil.hasRightElseRequest(this, rights, RIGHTREWUESTCODE);
                if (b) {
                    String text = downloadurltext.getText().toString();
                    binder.startDownload(text);
                }
                break;
            case R.id.stopdownload:
                binder.stopDownload();
                break;
            case R.id.pausedownload:
                binder.pauseDownload();
                break;
            case R.id.binddownloadservice:
                Intent it = new Intent(DownloaderActivity.this, DownloaderService.class);
                startService(it);
                bindService(it, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbinddownloadservice:
                unbindService(connection);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==RIGHTREWUESTCODE){
            if(grantResults.length>1 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                String text = downloadurltext.getText().toString();
                binder.startDownload(text);
            }
        }
    }
}