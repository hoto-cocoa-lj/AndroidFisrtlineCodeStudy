package com.slq.r1.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.slq.r1.R;

import java.util.zip.Inflater;

public class MyTitleLayout extends  LinearLayout{
    public MyTitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.mytitle,this);
    }


}
