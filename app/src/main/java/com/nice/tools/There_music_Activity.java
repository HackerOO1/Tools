package com.nice.tools;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class There_music_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    private AppBarLayout tool_music_mAppBarLayout;
    private Toolbar tool_music_toolbar;
    private TextInputLayout tool_music_editor;
    private MaterialButton tool_music_getbutton;
    private SmartRefreshLayout tool_music_refreshlayout;
    private RecyclerView tool_music_recycler;
    private List<There_music_Activity.Fruit> fruitList = new ArrayList<>();
    private There_music_Activity.FruitAdapter tool_music_adapter;
    private There_music_Activity.Fruit tool_music_datas;
    private RadioGroup tool_music_radiogroup;
    private RadioButton tool_music_wangyi;
    private RadioButton tool_music_kuwo;
    private RadioButton tool_music_kugou;
    private ProgressBar tool_music_progressBar;
    private String edit_name = "null";
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_music);
        intview();
        intdata();
    }

    //获取的歌曲数据刷新计数页面+名称存储+item调整+界面调整

    private void intdata() {
        //单选事件
        tool_music_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.tool_music_wangyi:
                        editor.putString("tool_music_radio", "netease");
                        editor.commit();
                        break;
                    case R.id.tool_music_kuwo:
                        editor.putString("tool_music_radio", "kuwo");
                        editor.commit();
                        break;
                    case R.id.tool_music_kugou:
                        editor.putString("tool_music_radio", "kugou");
                        editor.commit();
                        break;
                }
            }
        });
        //点击搜索列表
        tool_music_getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示控件
                mode.viewshow(tool_music_progressBar);
                edit_name = tool_music_editor.getEditText().getText().toString();
                if (!edit_name.equals("")) {
                    page = 1;
                    fruitList.clear();
                    mode.hideSoftInput((Activity) v.getContext(), tool_music_editor.getEditText());
                    String music_type = nicetools_prf.getString("tool_music_radio", "netease");
                    OkGo.<String>post("http://tbook.top/tool/music/")
                            .isMultipart(true)
                            .params("input", edit_name)
                            .params("filter", "name")
                            .params("type", music_type)
                            .params("page", page)
                            .cacheTime(200)
                            .headers("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .headers("X-Requested-With", "XMLHttpRequest")
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    String data = mode.decode(response.body());
                                    try {
                                        JSONObject json1 = new JSONObject(data);
                                        String code = json1.getString("code");
                                        if (code.equals("200")) {
                                            JSONArray json2 = json1.getJSONArray("data");
                                            //JSONArray json3 = json2.getJSONArray("messages");

                                            for (int i = 0; i < json2.length(); i++) {
                                                String title = json2.getJSONObject(i).optString("title");
                                                String mp3uri = json2.getJSONObject(i).optString("url");
                                                String author = json2.getJSONObject(i).optString("author");
                                                String imgurl = json2.getJSONObject(i).optString("pic");
                                                JSONObject js = json2.getJSONObject(i);
                                                tool_music_datas = new There_music_Activity.Fruit(imgurl, title, author, mp3uri, js);
                                                fruitList.add(tool_music_datas);
                                            }

                                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                            tool_music_recycler.setLayoutManager(manager);
                                            tool_music_adapter = new There_music_Activity.FruitAdapter(fruitList);
                                            tool_music_recycler.setAdapter(tool_music_adapter);
                                            //隐藏控件
                                            mode.viewhide(tool_music_progressBar);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    page = page + 1;
                }
            }
        });
    }

    private void intview() {
        //计数
        mode.jsphp(this,"tool_music");

        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        tool_music_mAppBarLayout = findViewById(R.id.tool_music_mAppBarLayout);
        tool_music_toolbar = findViewById(R.id.tool_music_toolbar);
        tool_music_editor = findViewById(R.id.tool_music_editor);
        tool_music_getbutton = findViewById(R.id.tool_music_getbutton);
        tool_music_refreshlayout = findViewById(R.id.tool_music_refreshlayout);
        tool_music_recycler = findViewById(R.id.tool_music_recycler);
        tool_music_radiogroup = findViewById(R.id.tool_music_radiogroup);
        tool_music_wangyi = findViewById(R.id.tool_music_wangyi);
        tool_music_kuwo = findViewById(R.id.tool_music_kuwo);
        tool_music_kugou = findViewById(R.id.tool_music_kugou);
        tool_music_progressBar = findViewById(R.id.tool_music_progressBar);

        setSupportActionBar(tool_music_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        //模式
        String radiobuttom = nicetools_prf.getString("tool_music_radio", "netease");
        if (radiobuttom.equals("netease")) {
            tool_music_wangyi.setChecked(true);
        } else if (radiobuttom.equals("kuwo")) {
            tool_music_kuwo.setChecked(true);
        } else if (radiobuttom.equals("kugou")) {
            tool_music_kugou.setChecked(true);
        }

        tool_music_refreshlayout.setEnableRefresh(false);
        tool_music_refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                //上拉加载
                //Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                String music_type = nicetools_prf.getString("tool_music_radio", "netease");
                OkGo.<String>post("http://tbook.top/tool/music/")
                        .isMultipart(true)
                        .params("input", edit_name)
                        .params("filter", "name")
                        .params("type", music_type)
                        .params("page", page)
                        .headers("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .headers("X-Requested-With", "XMLHttpRequest")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String data = mode.decode(response.body());
                                try {
                                    JSONObject json1 = new JSONObject(data);
                                    String code = json1.getString("code");
                                    if (code.equals("200")) {
                                        JSONArray json2 = json1.getJSONArray("data");
                                        //JSONArray json3 = json2.getJSONArray("messages");

                                        for (int i = 0; i < json2.length(); i++) {
                                            String title = json2.getJSONObject(i).optString("title");
                                            String mp3uri = json2.getJSONObject(i).optString("url");
                                            String author = json2.getJSONObject(i).optString("author");
                                            String imgurl = json2.getJSONObject(i).optString("pic");
                                            JSONObject js = json2.getJSONObject(i);
                                            tool_music_datas = new There_music_Activity.Fruit(imgurl, title, author, mp3uri, js);
                                            fruitList.add(tool_music_datas);
                                        }
                                        refreshlayout.finishLoadMore(100);//传入false表示加载失败
                                        tool_music_adapter.notifyItemInserted(tool_music_adapter.getItemCount());
                                        tool_music_recycler.getLayoutManager().scrollToPosition(tool_music_adapter.getItemCount());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                page = page + 1;
            }
        });

    }

    private class Fruit {
        private final String imguri;
        private final String title;
        private final String subtitel;
        private final String mp3uri;
        private final JSONObject js;

        public Fruit(String imguri, String title, String subtitel, String mp3uri, JSONObject js) {
            this.imguri = imguri;
            this.title = title;
            this.subtitel = subtitel;
            this.mp3uri = mp3uri;
            this.js = js;
        }

        public String getImguri() {
            return imguri;
        }

        public String getMp3uri() {
            return mp3uri;
        }

        public String getJs() {
            return js.toString();
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitel() {
            return subtitel;
        }
    }

    public static class FruitAdapter extends RecyclerView.Adapter<There_music_Activity.FruitAdapter.ViewHolder> {
        private List<There_music_Activity.Fruit> mFruitList;
        private String TAG = "TAG";

        static class ViewHolder extends RecyclerView.ViewHolder {
            CardView item_tool_music_dicard;
            ImageView item_tool_music_image;
            TextView item_tool_music_title;
            TextView item_tool_music_subtitle;


            public ViewHolder(View view) {
                super(view);
                item_tool_music_dicard = view.findViewById(R.id.item_tool_music_dicard);
                item_tool_music_image = view.findViewById(R.id.item_tool_music_image);
                item_tool_music_title = view.findViewById(R.id.item_tool_music_title);
                item_tool_music_subtitle = view.findViewById(R.id.item_tool_music_subtitle);
            }
        }

        public FruitAdapter(List<There_music_Activity.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public There_music_Activity.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool_music, parent, false);
            There_music_Activity.FruitAdapter.ViewHolder holder = new There_music_Activity.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final There_music_Activity.FruitAdapter.ViewHolder holder, int position) {

            final There_music_Activity.Fruit fruit = mFruitList.get(position);

            //动画匹配
            Animation animation = AnimationUtils.loadAnimation(holder.item_tool_music_dicard.getContext(), R.anim.anim_recycler_item_show);
            //holder.item_Crydear_cardview.startAnimation(animation);
            //透明度动画
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(500);
            holder.item_tool_music_dicard.startAnimation(aa);


            //数据传输进去
            holder.item_tool_music_title.setText(fruit.getTitle());
            holder.item_tool_music_subtitle.setText(fruit.getSubtitel());
            //使用Glide加载图片
            Glide.with(holder.item_tool_music_image.getContext())
                    //加载地址
                    .load(fruit.getImguri())
                    //加载失败时，设置默认的图片
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //显示的位置
                    .into(holder.item_tool_music_image);

            holder.item_tool_music_dicard.setOnClickListener(new View.OnClickListener() {
                public MediaPlayer mPlayer;

                @Override
                public void onClick(final View views) {
                    /*Toast.makeText(views.getContext(), fruit.getMp3uri(), Toast.LENGTH_LONG).show();
                    mode.copy(views.getContext(), fruit.getMp3uri());*/
                    //传递数据！
                    Intent intent = new Intent(views.getContext(), There_music_player_Activity.class);
                    intent.putExtra("mp3js", fruit.getJs());
                    views.getContext().startActivity(intent);
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
