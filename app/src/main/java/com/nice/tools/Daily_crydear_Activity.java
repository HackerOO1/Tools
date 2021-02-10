package com.nice.tools;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Daily_crydear_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private Toolbar crydear_toolbar;
    private AppBarLayout crydear_mAppBarLayout;
    private FloatingActionButton crydear_fab;
    private SwipeRecyclerView crydear_swiperecycler;
    private List<Daily_crydear_Activity.Fruit> fruitList = new ArrayList<>();
    private Daily_crydear_Activity.FruitAdapter crydear_adapter;
    private String[] mdata_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crydear);
        //加载控件啥的
        intview();
        //加载数据
        intdata();
        //点击事件
        intonick();

    }

    private void int_clear_data(RefreshLayout refreshlayout) {
        //清空之前的数据
        fruitList.clear();
        //快递记录创建
        if (mode.isDatainfo(this, "crydear_data.xml") == false) {
            mode.setDataInfo(this, "crydear_data.xml", "");
        } else {
            String mdata = mode.getDataInfo(this, "crydear_data.xml");

            if (mdata != null) {
                //删除第一个字符，然后使用@符号隔开
                mdata_list = mode.Sl(mode.removeCharAt(mdata, 0), "@");
                for (String crydear_data : mdata_list) {
                    String comb = mode.Sj(crydear_data, "&", null);
                    String nu = mode.Sj(crydear_data, null, "&");
                    Daily_crydear_Activity.Fruit data = new Daily_crydear_Activity.Fruit(comb, nu);
                    fruitList.add(data);
                }
            }
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        crydear_swiperecycler.setLayoutManager(manager);
        crydear_adapter = new Daily_crydear_Activity.FruitAdapter(fruitList);
        crydear_swiperecycler.setAdapter(crydear_adapter);

        if (refreshlayout != null) {
            refreshlayout.finishRefresh(700);
            Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
        }
    }


    private void intdata() {

        //快递记录创建
        if (mode.isDatainfo(this, "crydear_data.xml") == false) {
            mode.setDataInfo(this, "crydear_data.xml", "");
        } else {
            String mdata = mode.getDataInfo(this, "crydear_data.xml");
            if (mdata != null) {
                //删除第一个字符，然后使用@符号隔开
                mdata_list = mode.Sl(mode.removeCharAt(mdata, 0), "@");
                for (String crydear_data : mdata_list) {
                    String comb = mode.Sj(crydear_data, "&", null);
                    String nu = mode.Sj(crydear_data, null, "&");
                    Daily_crydear_Activity.Fruit data = new Daily_crydear_Activity.Fruit(comb, nu);
                    fruitList.add(data);
                }
            }
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        crydear_swiperecycler.setLayoutManager(manager);
        crydear_adapter = new Daily_crydear_Activity.FruitAdapter(fruitList);
        crydear_swiperecycler.setAdapter(crydear_adapter);

        //开启侧滑删除
        crydear_swiperecycler.setItemViewSwipeEnabled(true);

        OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                return false;
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

                // 从数据源移除该Item对应的数据，并刷新Adapter
                int position = srcHolder.getAdapterPosition();
                String mdata = mode.getDataInfo(getApplicationContext(), "crydear_data.xml");

                //删除第一个字符，然后使用@符号隔开
                mdata_list = mode.Sl(mode.removeCharAt(mdata, 0), "@");

                //Log.d(TAG, "@" + mdata_list[position]);
                String new_cry_data = mode.Sr(mode.getDataInfo(getApplicationContext(), "crydear_data.xml"), "@" + mdata_list[position], "");
                //Log.d(TAG, new_cry_data);
                boolean pd = mode.setDataInfo(getApplicationContext(), "crydear_data.xml", new_cry_data);
                if (pd) {
                    Toast.makeText(getApplicationContext(), "删除单号成功", Toast.LENGTH_SHORT).show();
                }
                fruitList.remove(position);
                crydear_adapter.notifyItemRemoved(position);
            }
        };

        crydear_swiperecycler.setOnItemMoveListener(mItemMoveListener);
        // 监听拖拽，更新UI。
    }

    private void intview() {
        //计数
        mode.jsphp(this,"crydear");

        crydear_toolbar = findViewById(R.id.crydear_toolbar);
        crydear_mAppBarLayout = findViewById(R.id.crydear_mAppBarLayout);
        crydear_fab = findViewById(R.id.crydear_fab);
        crydear_swiperecycler = findViewById(R.id.crydear_sw_recycler);

        setSupportActionBar(crydear_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //返回键事件
        crydear_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //下拉刷新
        RefreshLayout refreshLayout = findViewById(R.id.crydear_refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                int_clear_data(refreshlayout);
            }
        });

        //通过APPBAR监听底部fab上下滑动隐藏！
        crydear_mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                crydear_fab.setTranslationY(-verticalOffset - verticalOffset);
            }
        });
    }

    private void intonick() {
        //fab点击事件
        crydear_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View Bottom_edit_view = View.inflate(view.getContext(), R.layout.bottom_dialog_edit, null);
                final BottomSheetDialog bottom_edit_dialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialog);
                bottom_edit_dialog.setContentView(Bottom_edit_view);
                //底部弹窗展开
                BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) Bottom_edit_view.getParent());
                mDialogBehavior.setPeekHeight(mode.getResolution(Daily_crydear_Activity.this,"h"));

                bottom_edit_dialog.show();
                //标题
                final TextView bottom_edit_title = Bottom_edit_view.findViewById(R.id.bottom_edit_title);
                final Button bottom_edit_enter = Bottom_edit_view.findViewById(R.id.bottom_edit_but);
                final TextInputLayout bottom_edit_inputlayout = Bottom_edit_view.findViewById(R.id.bottom_edit_edit);
                //style
                bottom_edit_title.setText("查询快递");
                bottom_edit_enter.setText("查询");
                //编辑框行数
                bottom_edit_inputlayout.getEditText().setMaxEms(1);
                //编辑框类型
                //bottom_edit_inputlayout.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_CLASS_PHONE);
                bottom_edit_inputlayout.getEditText().setKeyListener(DigitsKeyListener.getInstance("0123456789zxcvbnmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP"));
                //编辑框长度
                bottom_edit_inputlayout.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                //编辑框hint
                bottom_edit_inputlayout.setHint("单号");

                //关闭弹窗
                Bottom_edit_view.findViewById(R.id.bottom_edit_close_icon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottom_edit_dialog.dismiss();
                    }
                });
                //debug
                // bottom_edit_inputlayout.getEditText().setText("773025818976244");
                //按钮点击事件
                bottom_edit_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final String edit_data = bottom_edit_inputlayout.getEditText().getText().toString();
                        if (!edit_data.equals("")) {
                            String link = "http://tbook.top/iso/ntools/crydear/?nu=" + edit_data;
                            Log.d(TAG, link);
                            OkGo.<String>get(link)
                                    .tag(this)
                                    .cacheKey("cacheKey")
                                    .cacheMode(CacheMode.NO_CACHE)
                                    .cacheTime(2000)//缓存时间
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            Log.d(TAG, response.body());
                                            if (response.body() != null) {
                                                //判断返回数据是不是空数据
                                                if (response.body().equals("false")) {
                                                    bottom_edit_inputlayout.setError("快递不存在");
                                                } else {
                                                    try {
                                                        JSONObject json1 = new JSONObject(response.body());
                                                        JSONObject json2 = json1.getJSONObject("data");
                                                        String company = json2.optString("company");
                                                        Log.d(TAG, company);
                                                        //做一个边缘判断
                                                        if (company == null) {
                                                            company = "未知快递";
                                                        }
                                                        //判断一下数据库是否包含了编辑框的单号
                                                        if (!mode.ifbhs(mode.getDataInfos(view.getContext(), "crydear_data.xml"), edit_data)) {
                                                            String newdata = "@" + edit_data + "&" + company;
                                                            if (mode.setGameInfo(getApplication(), "crydear_data.xml", newdata)) {
                                                                //存储好了数据之后开始查询，传入单号查询！
                                                                Intent intent = new Intent(view.getContext(), Daily_crydear_open_Activity.class);
                                                                intent.putExtra("number", edit_data);
                                                                startActivity(intent);
                                                                bottom_edit_dialog.dismiss();
                                                            }

                                                        } else {
                                                            //包含了文本也能咋的，查询呗，传入单号查询！存储好了数据之后开始查询，传入单号查询！
                                                            Intent intent = new Intent(view.getContext(), Daily_crydear_open_Activity.class);
                                                            intent.putExtra("number", edit_data);
                                                            startActivity(intent);
                                                            bottom_edit_dialog.dismiss();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });

            }
        });
    }

    private class Fruit {
        private final String conpent;
        private final String number;

        public Fruit(String conpent, String number) {
            this.conpent = conpent;
            this.number = number;
        }

        public String getConpent() {
            return conpent;
        }

        public String getNumber() {
            return number;
        }

    }

    public static class FruitAdapter extends RecyclerView.Adapter<Daily_crydear_Activity.FruitAdapter.ViewHolder> {
        private List<Daily_crydear_Activity.Fruit> mFruitList;
        private String TAG = "TAG";

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_Crydear_title;
            TextView item_Crydear_subtitle;
            CardView item_Crydear_cardview;

            public ViewHolder(View view) {
                super(view);
                item_Crydear_title = view.findViewById(R.id.item_Crydear_title);
                item_Crydear_subtitle = view.findViewById(R.id.item_Crydear_subtitle);
                item_Crydear_cardview = view.findViewById(R.id.item_Crydear_cardview);
            }
        }

        public FruitAdapter(List<Daily_crydear_Activity.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public Daily_crydear_Activity.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crydear, parent, false);
            Daily_crydear_Activity.FruitAdapter.ViewHolder holder = new Daily_crydear_Activity.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Daily_crydear_Activity.FruitAdapter.ViewHolder holder, int position) {

            final Daily_crydear_Activity.Fruit fruit = mFruitList.get(position);

            //动画匹配
            Animation animation = AnimationUtils.loadAnimation(holder.item_Crydear_cardview.getContext(), R.anim.anim_recycler_item_show);
            //holder.item_Crydear_cardview.startAnimation(animation);
            //透明度动画
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(500);
            holder.item_Crydear_title.startAnimation(aa);
            holder.item_Crydear_subtitle.startAnimation(aa);
            holder.item_Crydear_cardview.startAnimation(aa);

            //数据传输进去
            holder.item_Crydear_title.setText(fruit.getConpent());
            holder.item_Crydear_subtitle.setText(fruit.getNumber());


            //跳转开始查询，传入单号查询！
            holder.item_Crydear_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View views) {
                    Intent intent = new Intent(views.getContext(), Daily_crydear_open_Activity.class);
                    intent.putExtra("number", fruit.getNumber());
                    views.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }

    //返回界面事件
    protected void onStart() {
        super.onStart();
        //加载列表
        int_clear_data(null);

    }

    /*//获取ID，判断id，设置事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //返回键事件
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/
}
