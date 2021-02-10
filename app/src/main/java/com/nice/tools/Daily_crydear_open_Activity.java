package com.nice.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Daily_crydear_open_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private Toolbar crydear_open_toolbar;
    private AppBarLayout crydear_open_mAppBarLayout;
    private FloatingActionButton crydear_open_fab;
    private RecyclerView crydear_open_recycler;
    private List<Fruit> fruitList = new ArrayList<>();
    private Daily_crydear_open_Activity.FruitAdapter crydear_open_adapter;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crydear_open);

        //获取传来的参数，获取源码在获取json
        Intent intent = getIntent();
        number = intent.getStringExtra("number");

        //安排控件
        intview();

        //安排数据
        int_rec_data();

        RefreshLayout refreshLayout = findViewById(R.id.crydear_open_refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                int_clear_data(refreshlayout);
            }
        });
    }

    private void int_clear_data(final RefreshLayout refreshlayout) {
        //清空之前的数据
        fruitList.clear();
        String link = "http://tbook.top/iso/ntools/crydear/?nu=" + number;
        OkGo.<String>get(link)
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject json1 = new JSONObject(response.body());
                            JSONObject json2 = json1.getJSONObject("data");
                            JSONArray json3 = json2.getJSONArray("messages");

                            for (int i = 0; i < json3.length(); i++) {
                                String context = json3.getJSONObject(i).optString("context");
                                String time = json3.getJSONObject(i).optString("time");
                                Daily_crydear_open_Activity.Fruit data = new Daily_crydear_open_Activity.Fruit(context, time, i, json3.length());
                                fruitList.add(data);
                            }
                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                            crydear_open_recycler.setLayoutManager(manager);
                            crydear_open_adapter = new Daily_crydear_open_Activity.FruitAdapter(fruitList);
                            crydear_open_recycler.setAdapter(crydear_open_adapter);

                            refreshlayout.finishRefresh(700);
                            Toast.makeText(getApplicationContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                            //刷新完成
                        } catch (JSONException e) {
                            //刷新失败
                            refreshlayout.finishRefresh(false);
                            Toast.makeText(getApplicationContext(),"刷新失败，请稍后重试",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void intview() {
        crydear_open_toolbar = findViewById(R.id.crydear_open_toolbar);
        crydear_open_recycler = findViewById(R.id.crydear_open_recycler);
        setSupportActionBar(crydear_open_toolbar);
        crydear_open_toolbar.setTitle(number);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);
        //设置返回键可用

        //返回键事件
        crydear_open_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void int_rec_data() {
        String link = "http://tbook.top/iso/ntools/crydear/?nu=" + number;
        OkGo.<String>get(link)
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //Log.d(TAG, response.body());
                        try {
                            JSONObject json1 = new JSONObject(response.body());
                            JSONObject json2 = json1.getJSONObject("data");
                            JSONArray json3 = json2.getJSONArray("messages");

                            for (int i = 0; i < json3.length(); i++) {
                                String context = json3.getJSONObject(i).optString("context");
                                String time = json3.getJSONObject(i).optString("time");
                                Daily_crydear_open_Activity.Fruit data = new Daily_crydear_open_Activity.Fruit(context, time, i, json3.length());
                                fruitList.add(data);
                            }

                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                            crydear_open_recycler.setLayoutManager(manager);
                            crydear_open_adapter = new Daily_crydear_open_Activity.FruitAdapter(fruitList);
                            crydear_open_recycler.setAdapter(crydear_open_adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private class Fruit {
        private final String context;
        private final String time;
        private final int tion;
        private final int jslenth;

        public Fruit(String context, String time, int tion, int jslenth) {
            this.context = context;
            this.time = time;
            this.tion = tion;
            this.jslenth = jslenth;
        }

        public String getContext() {
            return context;
        }

        public String getTime() {
            return time;
        }

        public int getTion() {
            return tion;
        }

        public int getJslenth() {
            return jslenth;
        }
    }


    public static class FruitAdapter extends RecyclerView.Adapter<Daily_crydear_open_Activity.FruitAdapter.ViewHolder> {
        private List<Daily_crydear_open_Activity.Fruit> mFruitList;
        private String TAG = "TAG";

        static class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout item_Crydear_open_tion_hander;
            CardView item_Crydear_open_tion_center;
            LinearLayout item_Crydear_open_tion_bottom;
            TextView item_Crydear_open_title;
            TextView item_Crydear_open_subtitle;

            public ViewHolder(View view) {
                super(view);
                item_Crydear_open_tion_hander = view.findViewById(R.id.item_Crydear_open_tion_hander);
                item_Crydear_open_tion_center = view.findViewById(R.id.item_Crydear_open_tion_center);
                item_Crydear_open_tion_bottom = view.findViewById(R.id.item_Crydear_open_tion_bottom);
                item_Crydear_open_title = view.findViewById(R.id.item_Crydear_open_title);
                item_Crydear_open_subtitle = view.findViewById(R.id.item_Crydear_open_subtitle);
            }
        }

        public FruitAdapter(List<Daily_crydear_open_Activity.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public Daily_crydear_open_Activity.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crydear_open, parent, false);
            Daily_crydear_open_Activity.FruitAdapter.ViewHolder holder = new Daily_crydear_open_Activity.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Daily_crydear_open_Activity.FruitAdapter.ViewHolder holder, int position) {

            final Daily_crydear_open_Activity.Fruit fruit = mFruitList.get(position);

            //动画匹配
            /*Animation animation = AnimationUtils.loadAnimation(holder.item_Crydear_open_title.getContext(), R.anim.anim_recycler_item_show);
            holder.item_Crydear_open_tion_hander.startAnimation(animation);
            holder.item_Crydear_open_tion_center.startAnimation(animation);
            holder.item_Crydear_open_tion_bottom.startAnimation(animation);*/
            //透明度动画
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(500);
            holder.item_Crydear_open_tion_hander.startAnimation(aa);
            holder.item_Crydear_open_tion_center.startAnimation(aa);
            holder.item_Crydear_open_tion_bottom.startAnimation(aa);
            holder.item_Crydear_open_title.startAnimation(aa);
            holder.item_Crydear_open_subtitle.startAnimation(aa);

            //数据传输进去
            holder.item_Crydear_open_title.setText(fruit.getContext());
            holder.item_Crydear_open_subtitle.setText(fruit.getTime());
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }

}
