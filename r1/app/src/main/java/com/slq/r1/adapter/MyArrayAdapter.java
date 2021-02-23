package com.slq.r1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.slq.r1.R;

import com.slq.r1.pojo.News;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<News> {
    int resource;
    public MyArrayAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<News> objects) {
        super(context, textViewResourceId, objects);
        this.resource=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(resource,parent,false);
        }
        News item = getItem(position);
        TextView id = convertView.findViewById(R.id.newsid);
        TextView title = convertView.findViewById(R.id.newstitle);
        id.setText(""+item.getId());
        title.setText(item.getTitle());
        return convertView;
    }
}
