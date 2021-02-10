package com.nice.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

public class Activity_github extends AppCompatActivity {
    public Toolbar setting_github_toolbar;
    public RecyclerView setting_github_recycler;
    public String TAG = "TAG";
    private List<Activity_github.Fruit> github_fruitList = new ArrayList<>();
    private Activity_github.FruitAdapter github_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        intview();
        intdata();
    }

    private void intview() {
        setting_github_toolbar = findViewById(R.id.setting_github_toolbar);
        setting_github_recycler = findViewById(R.id.setting_github_recycler);
        setSupportActionBar(setting_github_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        //返回键事件
        setting_github_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void intdata() {
        //获取数据并列表
        OkGo.<String>get("http://tbook.top/iso/ntools/github/")
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body() != null) {
                            String[] github_list = mode.Sl(response.body(), "【");
                            for (String github_list_data : github_list) {
                                String github_title = mode.Sj(github_list_data, null, "】");
                                String github_subtitle = mode.Sj(github_list_data, "】", null);
                                Activity_github.Fruit data = new Activity_github.Fruit(github_title, github_subtitle);
                                github_fruitList.add(data);
                            }
                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                            setting_github_recycler.setLayoutManager(manager);

                            Activity_github.FruitAdapter adapter = new Activity_github.FruitAdapter(github_fruitList);
                            setting_github_recycler.setAdapter(adapter);
                        }
                    }
                });
        //计数
        mode.jsphp(this,"github");
    }

    private class Fruit {
        private final String github_title;
        private final String github_subtitle;

        public Fruit(String github_title, String github_subtitle) {
            this.github_title = github_title;
            this.github_subtitle = github_subtitle;
        }


        public String getGithub_title() {
            return github_title;
        }

        public String getGithub_subtitle() {
            return github_subtitle;
        }
    }

    public static class FruitAdapter extends RecyclerView.Adapter<Activity_github.FruitAdapter.ViewHolder> {
        private List<Activity_github.Fruit> mFruitList;
        private String TAG = "TAG";

        static class ViewHolder extends RecyclerView.ViewHolder {
            CardView item_github_bgcard;
            TextView item_github_title;
            TextView item_github_subtitle;

            public ViewHolder(View view) {
                super(view);
                item_github_bgcard = view.findViewById(R.id.item_github_bgcard);
                item_github_title = view.findViewById(R.id.item_github_title);
                item_github_subtitle = view.findViewById(R.id.item_github_subtitle);
            }
        }

        public FruitAdapter(List<Activity_github.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public Activity_github.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_github, parent, false);
            Activity_github.FruitAdapter.ViewHolder holder = new Activity_github.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Activity_github.FruitAdapter.ViewHolder holder, int position) {

            final Activity_github.Fruit fruit = mFruitList.get(position);

            //透明度动画
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(500);
            holder.item_github_bgcard.startAnimation(aa);


            //数据传输进去
            holder.item_github_title.setText(fruit.getGithub_title());
            holder.item_github_subtitle.setText(fruit.getGithub_subtitle());


            //跳转开始查询，传入单号查询！
            holder.item_github_bgcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View views) {
                    mode.inter(views.getContext(),fruit.getGithub_subtitle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }

}