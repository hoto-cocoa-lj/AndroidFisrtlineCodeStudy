package com.slq.r1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.slq.r1.R;
import com.slq.r1.activity.WeatherInfoActivity;
import com.slq.r1.adapter.WeatherAdapter;
import com.slq.r1.interfaces.WeatherCallback;
import com.slq.r1.pojo.Weather;
import com.slq.r1.utils.FileUtil;
import com.slq.r1.utils.UrlUtil;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.attribute.BasicFileAttributeView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherFragment extends Fragment {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG = "WeatherFragment";
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    ArrayList<Weather> datas = new ArrayList<>();
    WeatherAdapter adapter;
    WeatherCallback weatherCallback;
    String url = "http://guolin.tech/api/china";
    Weather currentWeather;
    ImageView imageView;
    String SPName = TAG;
    String SPKey = "weather_list_pic_url";
    String SPTIMEKey = "weather_list_pic_url_time";
    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    String ImgFile = TAG + ".jpg";
    File file = new File(directory, ImgFile);

    public WeatherFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherCallback weatherCallback = new WeatherCallbackImpl();
        adapter = new WeatherAdapter(datas);
        adapter.setWeatherCallback(weatherCallback);
        manager = new LinearLayoutManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_weather, container, false);
        recyclerView = inflate.findViewById(R.id.weather_fragment_recyclerview);
        imageView = inflate.findViewById(R.id.weather_list_pic);
        loadPic();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        UrlUtil.get(url, callback);
        return inflate;
    }

    private void loadPic() {
        Date date = new Date(file.lastModified());
        if (!sdf.format(date).equals(sdf.format(new Date()))) {
            file.delete();
        }

        if (file.exists()) {
            Toast.makeText(getContext(), "已下载过背景图片", Toast.LENGTH_SHORT).show();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getContext()).load(file).into(imageView);
                }
            });
            return;
        }

        String value = FileUtil.getSP("SPName", SPKey);
        String sptime = FileUtil.getSP("SPName", SPTIMEKey);
        if (value != null & sptime!=null &!sptime.equals(sdf.format(new Date()))) {
            Toast.makeText(getContext(), "找到背景图片url", Toast.LENGTH_SHORT).show();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getContext()).load(value).into(imageView);
                    UrlUtil.get(value, downloadImgCallback);
                }
            });
        } else {
            Toast.makeText(getContext(), "没有找到背景图片url，将去网上查找", Toast.LENGTH_SHORT).show();
            //http://cn.bing.com/th?id=OHR.VolcanoLlaima_ROW4131292607_1920x1080.jpg&rf=LaDigue_1920x1081920x1080.jpg
            String imgUrl = "http://guolin.tech/api/bing_pic";
            UrlUtil.get(imgUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String value1 = response.body().string();
                    FileUtil.setSP(SPName, SPKey, value1);
                    FileUtil.setSP("SPName", SPTIMEKey,sdf.format(new Date()));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getContext()).load(value1).into(imageView);
                            UrlUtil.get(value1, downloadImgCallback);
                        }
                    });
                }
            });
        }
    }

    Callback downloadImgCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if (!file.exists()) {
                InputStream is = response.body().byteStream();
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                int len;
                byte[] b = new byte[1024];
                while ((len = is.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                }
                outputStream.close();
                is.close();
            }
        }
    };


    void initRecyclerViewDatas(ArrayList<Weather> weathers) {
        datas.clear();
        datas.add(new Weather(-1, "返回", "-1", weathers.get(0).getParents()));
        datas.addAll(weathers);
        adapter.notifyDataSetChanged();
    }

    class WeatherCallbackImpl implements WeatherCallback {
        @Override
        public void goUp(Weather weather) {
            String url1 = url;
            String[] parents = weather.getParents();
            if (parents != null) {
                for (int i = 0; i < parents.length - 1; i++) {
                    url1 = url1 + "/" + parents[i];
                }
            }
            UrlUtil.get(url1, callback);
        }

        @Override
        public void goDown(Weather weather) {
            currentWeather = weather;
            String url1 = url;
            String[] parents = weather.getParents();
            for (int i = 0; i < parents.length; i++) {
                url1 = url1 + "/" + parents[i];
            }
            url1 = url1 + "/" + weather.getId();
            UrlUtil.get(url1, callback);
        }

    }

    Callback callback = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String string = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Weather>>() {
            }.getType();
            ArrayList<Weather> weathers;
            try {
                weathers = gson.fromJson(string, type);
            } catch (JsonSyntaxException e) {
                FragmentActivity act = getActivity();
                if (act instanceof WeatherInfoActivity) {
                    WeatherInfoActivity act1=(WeatherInfoActivity)act;
                    act1.fix(currentWeather.getName());
                } else {
                    Intent intent = new Intent(act, WeatherInfoActivity.class);
                    intent.putExtra("name", currentWeather.getName());
                    startActivity(intent);
                }
                return;
            }

            List<String> strings = response.request().url().pathSegments();
            String[] parents = new String[strings.size() - 2];
            for (int i = 0; i < strings.size() - 2; i++) {
                parents[i] = strings.get(i + 2);
            }
            for (Weather w : weathers) {
                w.setParents(parents);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initRecyclerViewDatas(weathers);
                }
            });

        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            e.printStackTrace();
        }
    };
}