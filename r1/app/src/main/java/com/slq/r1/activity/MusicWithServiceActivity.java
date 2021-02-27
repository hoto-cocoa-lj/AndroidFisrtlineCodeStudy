package com.slq.r1.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.slq.r1.R;
import com.slq.r1.service.MyMusicService;
import com.slq.r1.utils.FileUtil;
import com.slq.r1.utils.RightUtil;

public class MusicWithServiceActivity extends AppCompatActivity implements View.OnClickListener {
    final int FILE_SELECT_CODE = 1;
    final int requestCodeGetMusic = 2;
    MyMusicService.MusicBinder binder;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyMusicService.MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_with_service);
        init();
    }

    private void init() {
        findViewById(R.id.anothermusicwithservice).setOnClickListener(this);
        findViewById(R.id.bindmusicservice).setOnClickListener(this);
        findViewById(R.id.unbindmusicservice).setOnClickListener(this);
        findViewById(R.id.musicselectwithservice).setOnClickListener(this);
        findViewById(R.id.musicstartwithservice).setOnClickListener(this);
        findViewById(R.id.musicstopwithservice).setOnClickListener(this);
        findViewById(R.id.musicpausewithservice).setOnClickListener(this);
        String[] rights = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.FOREGROUND_SERVICE
        };
        boolean hasRight = RightUtil.hasRightElseRequest(getApplicationContext(), rights, requestCodeGetMusic);
        if (hasRight) {
            //initMusic();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri musicUri = data.getData();
                    binder.select(musicUri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case requestCodeGetMusic:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //initMusic();
                } else {
                    Toast.makeText(getApplicationContext(), "没有读写文件的权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.anothermusicwithservice:
                intent = new Intent(getApplicationContext(), MusicWithServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.bindmusicservice:
                intent = new Intent(getApplicationContext(), MyMusicService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
                startService(intent);                     //place1
                break;
            case R.id.unbindmusicservice:
                unbindService(connection);
                break;
            case R.id.musicselectwithservice:
                FileUtil.showFileChooser(MusicWithServiceActivity.this, FILE_SELECT_CODE);
                break;
            case R.id.musicstartwithservice:
                binder.start();
                break;
            case R.id.musicpausewithservice:
                binder.pause();
                break;
            case R.id.musicstopwithservice:
                binder.stop();
                break;
            default:
                break;
        }
    }

}