package com.slq.r1.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.slq.r1.R;
import com.slq.r1.utils.FileUtil;
import com.slq.r1.utils.RightUtil;

public class MovieActivity extends AppCompatActivity implements View.OnClickListener {
    final int requestCodeGetMovie = 1;
    final int FILE_SELECT_CODE = 2;
    VideoView mp;
    Uri movieUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        init();
    }

    private void init() {
        mp = findViewById(R.id.movieview);
        findViewById(R.id.moviepause).setOnClickListener(this);
        findViewById(R.id.moviestart).setOnClickListener(this);
        findViewById(R.id.movieresume).setOnClickListener(this);
        findViewById(R.id.movieselect).setOnClickListener(this);
        String[] rights = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        boolean hasRight = RightUtil.hasRightElseRequest(MovieActivity.this, rights, requestCodeGetMovie);
        if (hasRight) {
            initMovie();
        }
    }

    private void initMovie() {
        if (movieUri != null) {
            mp.setVideoURI(movieUri);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    movieUri = data.getData();
                    initMovie();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case requestCodeGetMovie:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMovie();
                } else {
                    Toast.makeText(getApplicationContext(), "没有读写文件的权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.movieselect:
                FileUtil.showFileChooser(MovieActivity.this, FILE_SELECT_CODE);
                break;
            case R.id.moviepause:
                if (mp.isPlaying()) mp.pause();
                break;
            case R.id.moviestart:
                if (!mp.isPlaying()) mp.start();
                break;
            case R.id.movieresume:
                mp.resume();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.suspend();
        }
    }
}