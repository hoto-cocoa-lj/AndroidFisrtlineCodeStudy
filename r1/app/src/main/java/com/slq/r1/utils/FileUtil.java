package com.slq.r1.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.slq.r1.app.MyApplication;

public class FileUtil {
    public static void setSP(String spname,String key,String value){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(spname,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getSP(String spname,String key){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(spname,Context.MODE_PRIVATE);
        return sp.getString(key,null);
    }

    public static  void showFileChooser(Context context,int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            Activity a=(Activity)context;
            a.startActivityForResult(Intent.createChooser(intent, "请选择文件"), requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}

