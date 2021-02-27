package com.slq.r1.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.slq.r1.R;
import com.slq.r1.service.MyIntentService1;
import com.slq.r1.service.MyService1;

public class MyService1Activity extends AppCompatActivity {
    private static final String TAG = "MyService1Activity";
    Button startMyService1, stopMyService1, bindService1, unBindService1,printService,myIntentService;
    TextView text;
    ProgressBar progressBar;
    int i;
    MyService1.LoadBinder binder;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService1.LoadBinder) service;
            i = binder.start1()+i;
            progressBar.setProgress(i);
            text.setText(i+"%");
        }

        @Override
        //在正常情况下是不被调用的，它的调用时机是当Service服务被异外销毁时，例如内存的资源不足
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: "+name );
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service1);
        text = (TextView) findViewById(R.id.myservicetext);
        progressBar = (ProgressBar) findViewById(R.id.myserviceprogress_bar);

        startMyService1 = (Button) findViewById(R.id.startMyService1);
        startMyService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyService1Activity.this, MyService1.class);
                startService(intent);
            }
        });
        stopMyService1 = (Button) findViewById(R.id.stopMyService1);
        stopMyService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyService1Activity.this, MyService1.class);
                stopService(intent);
            }
        });
        bindService1 = (Button) findViewById(R.id.bindService1);
        bindService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyService1Activity.this, MyService1.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
        });

        unBindService1 = (Button) findViewById(R.id.unBindService1);
        unBindService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(connection);
            }
        });

        printService = (Button) findViewById(R.id.printService);
        printService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.printService();
            }
        });

        myIntentService = (Button) findViewById(R.id.myIntentService);
        myIntentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                MyIntentService1.enqueueWork(getApplicationContext(),it);
                //Intent it = new Intent(MyService1Activity.this, MyIntentService1.class);
                //startService(it);
            }
        });
    }

}