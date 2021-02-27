package com.slq.r1.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.slq.r1.R;
import com.slq.r1.fragment.MyFragment1;
import com.slq.r1.fragment.MyFragment2;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.hide();
        }
        Button b1 = (Button) findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager sfm = getSupportFragmentManager();
                FragmentTransaction t = sfm.beginTransaction();
                List<Fragment> fragments = sfm.getFragments();
                int f=R.id.myf1;
                t.addToBackStack("");
                if (fragments.size()>0 && fragments.get(fragments.size()-1) instanceof MyFragment1) {
                    t.replace(f, new MyFragment2());
                } else {
                    t.replace(f, new MyFragment1());
                }
                t.commit();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("re", "back!!");
        setResult(RESULT_OK, i);
        super.onBackPressed();
    }
}