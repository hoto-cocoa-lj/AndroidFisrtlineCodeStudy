package com.slq.r1.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RightUtil {
    public static boolean hasRightElseRequest(Context context, String[] rights, int requestCode) {
        int i = ContextCompat.checkSelfPermission(context, rights[0]);
        if (i != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, rights, requestCode);
            return false;
        } else {
            return true;
        }
    }
}
