package com.slq.r1.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.slq.r1.R;
import com.slq.r1.utils.RightUtil;
public class WeatherActivity extends AppCompatActivity {
    String[] rights = new String[]{Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    Toolbar toolbar;
    LinearLayout myFrameLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int requestCode = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        myFrameLayout1=findViewById(R.id.myWeatherFrameLayout1);
        getToolBar3();
        RightUtil.hasRightElseRequest(this, rights,requestCode);
    }

    private void getToolBar3() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.mybar, myFrameLayout1, false);
        toolbar = (Toolbar) inflate.findViewById(R.id.mybar);
        myFrameLayout1.addView(toolbar,0);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_item:
                Toast.makeText(this, "DEL IT", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "delete IT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.backup:
                Toast.makeText(this, "backup IT", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}