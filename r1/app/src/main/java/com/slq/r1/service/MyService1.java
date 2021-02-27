package com.slq.r1.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MyService1 extends Service {
    private static final String TAG = "MyService1";
    private LoadBinder mbinder = new LoadBinder();

    public MyService1() {
    }

    public class LoadBinder extends Binder {
        public int start1() {
            Log.e(TAG, "start1: ");
            return 5;
        }

        public void printService() {
            Log.e(TAG, "MyService1: " + MyService1.this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(1111);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "printService: "+MyService1.this );
                    }
                }
            }).start();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mbinder;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: " + this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
    }
}