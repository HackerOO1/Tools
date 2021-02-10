package com.nice.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

public class Tool_pastoday_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private Toolbar tool_pastoday_toolbar;
    private RecyclerView tool_pastoday_recycler;

    private List<Tool_pastoday_Activity.Fruit> fruitList = new ArrayList<>();
    private Tool_pastoday_Activity.FruitAdapter crydear_adapter;
    private Tool_pastoday_Activity.Fruit data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_pastoday);
        //加载控件啥的
        intview();
        //加载数据
        intdata();
    }

    private void intdata() {
        OkGo.<String>get("http://hao.360.com/histoday/")
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)//缓存时间
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body() != null) {
                            //判断返回数据是不是空数据
                            String datas = mode.Sj(response.body(), " <dt><em>", "doc-ft");
                            String[] datalist = mode.Sl(datas, " <dt><em>");
                            for (String datasl : datalist) {
                                //Log.d(TAG, datasl);
                                final String pastoday_title = mode.Sr(mode.Sr(mode.Sj(datasl, null, "<dd>"), "</em>", ""), "</dt>", "").trim();
                                String pastoday_img_link = "http://tbook.top/iso/ntools/img/null.png";
                                if (mode.ifbhs(mode.Sj(datasl, "src=\"", "clearfix"), "g\"")) {
                                    pastoday_img_link = "http:" + mode.Sj(mode.Sj(datasl, "src=\"", "clearfix"), null, "g\"") + "g";
                                } else {
                                    pastoday_img_link = "http://tbook.top/iso/ntools/img/null.png";
                                }
                                String subtitle= mode.Sj(mode.Sj(datasl, "desc", "right-btn-container"),">","</div>").trim();

                                data = new Tool_pastoday_Activity.Fruit(pastoday_img_link, pastoday_title, subtitle);
                                fruitList.add(data);

                            }
                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                            tool_pastoday_recycler.setLayoutManager(manager);
                            crydear_adapter = new Tool_pastoday_Activity.FruitAdapter(fruitList);
                            tool_pastoday_recycler.setAdapter(crydear_adapter);
                        }
                    }
                });


    }


    private void intview() {
        //计数
        mode.jsphp(this,"pastoday");
        tool_pastoday_toolbar = findViewById(R.id.tool_pastoday_toolbar);
        tool_pastoday_recycler = findViewById(R.id.tool_pastoday_recycler);

        //初始toolbar
        setSupportActionBar(tool_pastoday_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //Recyler适配器
    private class Fruit {
        private final String bitbmpuri;
        private final String title;
        private final String subtitle;

        public Fruit(String bitbmpuri, String title, String subtitle) {
            this.bitbmpuri = bitbmpuri;
            this.title = title;
            this.subtitle = subtitle;
        }

        public String getBitbmpuri() {
            return bitbmpuri;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }

    public static class FruitAdapter extends RecyclerView.Adapter<Tool_pastoday_Activity.FruitAdapter.ViewHolder> {
        private List<Tool_pastoday_Activity.Fruit> mFruitList;
        private String TAG = "TAG";

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_pastoday_title;
            TextView item_pastoday_subtitle;
            ImageView item_pastoday_image;
            MaterialCardView item_pastoday_card;

            public ViewHolder(View view) {
                super(view);
                item_pastoday_card = view.findViewById(R.id.item_pastoday_card);
                item_pastoday_image = view.findViewById(R.id.item_pastoday_image);
                item_pastoday_title = view.findViewById(R.id.item_pastoday_title);
                item_pastoday_subtitle = view.findViewById(R.id.item_pastoday_subtitle);
            }
        }

        public FruitAdapter(List<Tool_pastoday_Activity.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public Tool_pastoday_Activity.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_today, parent, false);
            Tool_pastoday_Activity.FruitAdapter.ViewHolder holder = new Tool_pastoday_Activity.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Tool_pastoday_Activity.FruitAdapter.ViewHolder holder, int position) {

            final Tool_pastoday_Activity.Fruit fruit = mFruitList.get(position);

            //显示控件
            mode.viewshow(holder.item_pastoday_card);

            //数据传输进去
            holder.item_pastoday_title.setText(fruit.getTitle());
            holder.item_pastoday_subtitle.setText(fruit.getSubtitle());

            //使用Glide加载图片
            Glide.with(holder.item_pastoday_card.getContext())
                    //加载地址
                    .load(fruit.getBitbmpuri())
                    //加载失败时，设置默认的图片
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //显示的位置
                    .into(holder.item_pastoday_image);

            //跳转开始查询，传入单号查询！
            holder.item_pastoday_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View views) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }


    //获取ID，判断id，设置事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //返回键事件
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
