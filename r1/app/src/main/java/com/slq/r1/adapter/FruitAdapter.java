package com.slq.r1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.slq.r1.R;
import com.slq.r1.pojo.Fruit;

import java.util.ArrayList;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitHolder> {
    ArrayList<Fruit> datas;
    Context context;

    public FruitAdapter(ArrayList<Fruit> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public FruitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null)context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fruit_item,parent,false);
        FruitHolder fruitHolder = new FruitHolder(view);
        return fruitHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FruitHolder holder, int position) {
        Fruit fruit = datas.get(position);
        //holder.imageView.setImageResource(fruit.getImgId());
        Glide.with(context).load(fruit.getImgId()).into(holder.imageView);
        holder.textView.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class FruitHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public FruitHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.fruit_name);
            imageView=itemView.findViewById(R.id.fruit_img);
        }
    }
}
