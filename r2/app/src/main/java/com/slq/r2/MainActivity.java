package com.slq.r2;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//使用LocalBroadcastManager需要implementation 'androidx.localbroadcastmanager:localbroadcastmanager:1.0.0'
//本地广播的注册，解绑，和发送都要用localBroadcastManager的方法；
//本地广播和标准广播使用同一个actino的情况下，本地广播能当前APP的本地receiver能收到；
//标准广播能在所有APP的非本地receiver收到
public class MainActivity extends AppCompatActivity {
    IntentFilter itf;
    R2BroadcastReceiver br;

    IntentFilter itf2;
    LocalBroadcastManager localBroadcastManager;
    R2LocalBroadcastReceiver brlocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itf = new IntentFilter();
        itf.addAction("FROMOTHERAPP");
        br=new R2BroadcastReceiver();
        itf.setPriority(1);
        registerReceiver(br,itf);
        Button b1 = findViewById(R.id.broadcast1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("FROMOTHERAPP");
                it.putExtra("time",System.currentTimeMillis());
                //sendBroadcast(it);
                sendOrderedBroadcast(it,null);
            }
        });

        brlocal=new R2LocalBroadcastReceiver();
        itf2 = new IntentFilter();
        itf2.addAction("FROMOTHERAPP");
        localBroadcastManager=LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(brlocal,itf2);
        Button b2 = findViewById(R.id.broadcast2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent("FROMOTHERAPP");
                it.putExtra("time",System.currentTimeMillis());
                localBroadcastManager.sendBroadcast(it);
            }
        });

    }

    class R2BroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            long t=intent.getLongExtra("time",0L);
            Toast.makeText(context,"r2 receive:"+t,Toast.LENGTH_SHORT).show();

        }
    }

    class R2LocalBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            long t=intent.getLongExtra("time",0L);
            Toast.makeText(context,"r2 receive local msg:"+t,Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
        localBroadcastManager.unregisterReceiver(brlocal);
    }
}