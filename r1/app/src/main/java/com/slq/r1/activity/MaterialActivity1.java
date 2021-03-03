package com.slq.r1.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.slq.r1.R;
import com.slq.r1.adapter.FruitAdapter;
import com.slq.r1.pojo.Fruit;

import java.util.ArrayList;

public class MaterialActivity1 extends AppCompatActivity {
    private static final String TAG = "MaterialActivity1";
    DrawerLayout myDrawerLayout1;

    //FrameLayout myFrameLayout1;
    CoordinatorLayout myFrameLayout1;
    AppBarLayout myAppBarLayout1;
    SwipeRefreshLayout mySwipeRefreshLayout1;

    RecyclerView recyclerView;

    Toolbar toolbar;
    NavigationView navigationView;
    FloatingActionButton myFloatingActionButton1;

    FruitAdapter fruitAdapter;
    ArrayList<Fruit> datas = new ArrayList<>();
    int imgid1;
    int start = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material1);
        myDrawerLayout1 = findViewById(R.id.myDrawerLayout1);
        myFrameLayout1 = findViewById(R.id.myFrameLayout1);
        myAppBarLayout1 = findViewById(R.id.myAppBarLayout1);
        mySwipeRefreshLayout1 = findViewById(R.id.mySwipeRefreshLayout1);

        //添加并设置toolbar
        //getToolBar2();                    //getToolBar1/2/3()都可以实现把toolbar放myFrameLayout1下
        getToolBar4();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);                              //显示HomeAsUp按钮
            //actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher_round);           //修改默认图标
        }

        //设置navigation
        navigationView = findViewById(R.id.navigation1);
        navigationView.setCheckedItem(R.id.nav_menu1);                  //设置默认选择
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                myDrawerLayout1.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //设置FloatingActionButton
        myFloatingActionButton1 = findViewById(R.id.myFloatingActionButton1);
        myFloatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "是否格式化手机？", Snackbar.LENGTH_SHORT)
                        .setAction("OK!", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "手机格式化成功！！", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        //设置recyclerView，添加卡片布局
        recyclerView = findViewById(R.id.fruits_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(MaterialActivity1.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        imgid1 = getResources().getIdentifier("chinokokoa", "mipmap", getPackageName());
        addFruits(30);
        fruitAdapter = new FruitAdapter(datas);
        recyclerView.setAdapter(fruitAdapter);

        //设置SwipeRefreshLayout，给recyclerView实现下拉刷新效果
        mySwipeRefreshLayout1.setColorSchemeResources(R.color.design_default_color_primary);
        mySwipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
    }


    //mybar1必须在当前activity的样式(activity_material1)里有
    private void getToolBar1() {
        //toolbar = (Toolbar) findViewById(R.id.mybar1);
    }

    private void getToolBar2() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.mybar, myFrameLayout1, true);
        toolbar = (Toolbar) inflate.findViewById(R.id.mybar);
        //toolbar外还有一层时，此层也会加入
        //Log.e(TAG, "getToolBar2:" +myFrameLayout1.findViewById(R.id.myLinearLayout1));
    }

    private void getToolBar3() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.mybar, myFrameLayout1, false);
        toolbar = (Toolbar) inflate.findViewById(R.id.mybar);
        myFrameLayout1.addView(toolbar);
    }

    //不把toolbar放myFrameLayout1下，而是放myFrameLayout1里的AppBarLayout下
    //注意inflate的参数2必须是myAppBarLayout1，如果是myFrameLayout1，toolbar的layout_scrollFlags属性会失效
    private void getToolBar4() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.mybar, myAppBarLayout1, false);
        toolbar = (Toolbar) inflate.findViewById(R.id.mybar);
        myAppBarLayout1.addView(toolbar);
    }

    private void addFruits(int count) {
        for (int i = start; i < start + count; i++) {
            //下划刷新，把新数据放前面
            datas.add(0, new Fruit(imgid1, "水果" + i + "号"));
        }
        start = start + count;
    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1111);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addFruits(5);
                        fruitAdapter.notifyDataSetChanged();
                        mySwipeRefreshLayout1.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                myDrawerLayout1.openDrawer(GravityCompat.START);
                break;
            case R.id.add_item:
                myDrawerLayout1.openDrawer(GravityCompat.END);
                break;
            case R.id.remove_item:
                Toast.makeText(this, "DEL IT", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "delete IT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.backup:
                Toast.makeText(this, "backup IT", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}