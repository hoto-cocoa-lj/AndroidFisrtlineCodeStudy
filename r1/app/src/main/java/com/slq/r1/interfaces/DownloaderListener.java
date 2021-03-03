package com.slq.r1.interfaces;

public interface DownloaderListener {
    public void onSucess();

    public void onPause();

    public void onStop();

    public void onFail();

    public void onProgress(long... params);

}
