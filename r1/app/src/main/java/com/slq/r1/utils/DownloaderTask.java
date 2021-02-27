package com.slq.r1.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.slq.r1.DownloaderListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloaderTask extends AsyncTask<String, Long, Integer> {
    private static final String TAG = "DownloaderTask";
    private String url;
    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    File file;
    final public static int SUCCESS = 1, PAUSE = 2, DELETE = 3, FAIL = 0, DOING = 5;
    int state;
    DownloaderListener downloaderListener;

    public void setState(int state) {
        this.state = state;
    }

    public void setDownloaderListener(DownloaderListener downloaderListener) {
        this.downloaderListener = downloaderListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Log.e(TAG, "下载结果：" + integer);
        switch (integer) {
            case SUCCESS:
                downloaderListener.onSucess();
                break;
            case FAIL:
                downloaderListener.onFail();
                break;
            case PAUSE:
                downloaderListener.onPause();
                break;
            case DELETE:
                file.delete();
                downloaderListener.onStop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        downloaderListener.onProgress(values[0], values[1]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        if(strings[0]!="")url = strings[0];
//        if (state == DOING) {
//            return null;
//        } else {
//            state = DOING;
//        }
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
                    if (++co % 100 == 0) publishProgress(current, fileSize);
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
}
