package com.slq.r1.service;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.slq.r1.utils.DownloaderTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloaderService2 extends Service {
    private static final String TAG = "DownloaderService2";

    DownloaderTask downloaderTask;
    Messenger activityMessenger;
    Thread downloadThread;
    String url;
    final public static int SUCCESS = 1, PAUSE = 2, DELETE = 3, FAIL = 0, DOING = 5;
    int state;
    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    File file;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Message msg1 = new Message();
            switch (msg.what) {
                case -1:
                    activityMessenger = msg.replyTo;
                    break;
                case 10:
                    url = msg.getData().getString("url");
                    if (downloadThread == null || !downloadThread.isAlive()) {
                        downloadThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                downloadWithResult(url);
                            }
                        });
                        downloadThread.start();
                    }
                    break;
                case 0:
                    url = msg.getData().getString("url");
                    if (downloadThread == null || !downloadThread.isAlive()) {
                        Callable caller = new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {
                                return downloadWithResult(url);
                            }
                        };
                        FutureTask f = new FutureTask(caller);
                        downloadThread = new Thread(f);
                        try {
                            downloadThread.start();
                            //Log.e(TAG, "f.get(): "+f.get() );         //放开注释f.get()会阻塞主线程
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:     //stop
                    if (downloadThread != null) {
                        downloadThread.interrupt();
                        file.delete();
                        Toast.makeText(getApplicationContext(), "你已经stop下载", Toast.LENGTH_SHORT).show();
                        msg1.what = 2;
                        sendMsg(msg1);
                    }
                    break;
                case 2:       //pause
                    if (downloadThread != null) {
                        downloadThread.interrupt();
                        Toast.makeText(getApplicationContext(), "你已经暂停下载", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // 创建Messenger对象包含handler引用
        Messenger messenger = new Messenger(handler);
        // 返回Messenger的binder
        return messenger.getBinder();
    }

    protected Integer downloadWithResult(String... strings) {
        if (strings[0] != "") url = strings[0];
        state = DOING;
        String filename = url.substring(url.lastIndexOf("/") + 1);
        InputStream is = null;
        RandomAccessFile rw = null;
        Response response = null;
        try {
            file = new File(directory, filename);
            Log.e(TAG, "download path:" + directory + "/" + filename);
            if (!file.exists()) file.createNewFile();
            long current = file.length();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("range", "bytes=" + current + "-")
                    .url(url).build();
            response = client.newCall(request).execute();
            if (response != null) {
                long fileSize = response.body().contentLength() + current;
                if (current == fileSize) {
                    return SUCCESS;
                }
                is = response.body().byteStream();
                rw = new RandomAccessFile(file, "rw");
                rw.seek(current);
                byte[] b = new byte[1024];
                int len, co = 0;
                while ((len = is.read(b)) != -1) {
                    if (state != DOING) {
                        return state;
                    }
                    rw.write(b, 0, len);
                    current += len;
                    if (++co % 100 == 0) {
                        publishProgress(current, fileSize);
                    }
                }
                return SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (rw != null) rw.close();
                if (response != null) response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FAIL;
    }

    void publishProgress(long... values) {
        try {
            Message msg1 = new Message();
            msg1.what = 1;
            Bundle bundle = new Bundle();
            bundle.putIntArray("progress", new int[]{(int) values[0], (int) values[1]});
            msg1.setData(bundle);
            activityMessenger.send(msg1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Message msg) {
        try {
            activityMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
