package com.slq.r1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slq.r1.NewsContentActivity;
import com.slq.r1.R;
import com.slq.r1.pojo.News;
import com.slq.r1.NewsActivity;
public class NewsTitleFrag extends Fragment {
    boolean side2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class RVH extends RecyclerView.ViewHolder {
        TextView tv;
        TextView tv2;

        public RVH(@NonNull View itemView) {
            super(itemView);
            this.tv = itemView.findViewById(R.id.newsid);
            this.tv2 = itemView.findViewById(R.id.newstitle);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (side2) {
                        NewsContentFrag contentFrag = (NewsContentFrag)getParentFragmentManager().findFragmentById(R.id.newscontentfrag);
                        contentFrag.refresh("" + adapterPosition);
                    } else {
                        NewsContentActivity.startActivity0(getActivity(), "" + adapterPosition);
                    }

                }
            });
        }
    }

    public class RVA extends RecyclerView.Adapter<NewsTitleFrag.RVH> {
        ArrayList<News> data;

        public RVA(ArrayList<News> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsitem, parent, false);
            RVH rvh = new RVH(view);
            return rvh;
        }

        @Override
        public void onBindViewHolder(@NonNull RVH holder, int position) {
            News news = data.get(position);
            holder.tv.setText("" + news.getId());
            holder.tv2.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newstitle, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rcl1);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        ArrayList<News> getnews = getnews(33);
        RVA rva = new RVA(getnews);
        rv.setAdapter(rva);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        Fragment v = activity.getSupportFragmentManager().findFragmentById(R.id.newscontentfrag);
        if (v == null) {
            side2 = false;

        } else {
            side2 = true;
        }
    }

    ArrayList<News> getnews(int l) {
        ArrayList<News> a = new ArrayList<News>();
        for (int i = 0; i < l; i++) {
            a.add(new News("NEWS" + i, i));
        }
        return a;
    }
}
