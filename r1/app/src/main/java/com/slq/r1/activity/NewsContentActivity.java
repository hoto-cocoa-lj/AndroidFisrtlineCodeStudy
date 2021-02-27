package com.slq.r1.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.slq.r1.R;
import com.slq.r1.fragment.NewsContentFrag;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;

public class NewsContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        NewsContentFrag f1 = (NewsContentFrag) getSupportFragmentManager().findFragmentById(R.id.newscontentfrag1);
        Intent i = getIntent();
        String a = i.getStringExtra("a");
        f1.refresh(a);
    }

    public static void startActivity0(Context c, String a) {
        Intent intent = new Intent(c, NewsContentActivity.class);
        intent.putExtra("a", a);
        c.startActivity(intent);
    }
}