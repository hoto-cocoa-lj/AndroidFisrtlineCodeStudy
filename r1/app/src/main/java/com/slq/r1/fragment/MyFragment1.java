package com.slq.r1.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slq.r1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment1 extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_my1, container, false);
        return inflate;
    }
}