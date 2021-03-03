package com.slq.r1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.slq.r1.R;
import com.slq.r1.interfaces.WeatherCallback;
import com.slq.r1.pojo.Weather;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {
    ArrayList<Weather> datas;
    Context context;
    WeatherCallback weatherCallback;

    public WeatherAdapter(ArrayList<Weather> datas) {
        this.datas = datas;
    }

    public void setDatas(ArrayList<Weather> datas) {
        this.datas = datas;
    }

    public void setWeatherCallback(WeatherCallback weatherCallback) {
        this.weatherCallback = weatherCallback;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        WeatherHolder weatherHolder = new WeatherHolder(view);
        return weatherHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
        Weather weather = datas.get(position);
        holder.textView.setText(weather.getName());
        holder.textView.setOnClickListener(v -> {
            if (position == 0) {
                weatherCallback.goUp(weather);
            } else {
                weatherCallback.goDown(weather);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class WeatherHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public WeatherHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.weather_name);
        }
    }
}
