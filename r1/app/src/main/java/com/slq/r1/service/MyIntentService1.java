package com.slq.r1.service;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class MyIntentService1 extends JobIntentService {
    /**
     * 这个Service 唯一的id
     */
    static final int JOB_ID = 10111;
    private static final String TAG = "MyIntentService1";

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyIntentService1.class, JOB_ID, work);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "create MyIntentService1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy MyIntentService1", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try {
            Thread.sleep(1111);
            Log.e(TAG, "onHandleWork: " + Thread.currentThread().getName());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}