package com.slq.r1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.slq.r1.utils.FileUtil;
import com.slq.r1.utils.RightUtil;

import java.io.File;
import java.io.IOException;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    final int requestCodeGetMusic = 1;
    final int FILE_SELECT_CODE = 2;
    MediaPlayer mp = new MediaPlayer();
    Uri musicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        init();
    }

    private void init() {
        findViewById(R.id.musicselect).setOnClickListener(this);
        findViewById(R.id.musicstart).setOnClickListener(this);
        findViewById(R.id.musicstop).setOnClickListener(this);
        findViewById(R.id.musicpause).setOnClickListener(this);
        String[] rights = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        boolean hasRight = RightUtil.hasRightElseRequest(MusicActivity.this, rights, requestCodeGetMusic);
        if (hasRight) {
            initMusic();
        }
    }

    private void initMusic() {
        if(musicUri==null)return;
        try {
            mp.reset();
            mp.setDataSource(this,musicUri);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    musicUri = data.getData();
                    initMusic();
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
                    initMusic();
                } else {
                    Toast.makeText(getApplicationContext(), "没有读写文件的权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.musicselect:
                FileUtil.showFileChooser(MusicActivity.this, FILE_SELECT_CODE);
                break;
            case R.id.musicstart:
                if (!mp.isPlaying()) mp.start();
                break;

            case R.id.musicpause:
                if (mp.isPlaying()) mp.pause();
                break;
            case R.id.musicstop:
                if (!mp.isPlaying()) mp.stop();
                initMusic();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }
}