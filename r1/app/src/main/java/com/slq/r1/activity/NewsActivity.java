package com.slq.r1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.slq.r1.R;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    public static void startActivity0(Context c){
        Intent intent = new Intent(c, NewsActivity.class);
        c.startActivity(intent);
    }

}