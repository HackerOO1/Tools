package com.nice.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Daily_rubbish_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private Toolbar rubbish_toolbar;
    private AppBarLayout rubbish_mAppBarLayout;
    private FloatingActionButton rubbish_fab;
    private RecyclerView rubbish_recycler;
    private List<Daily_rubbish_Activity.Fruit> fruitList = new ArrayList<>();
    private Daily_rubbish_Activity.FruitAdapter rubbish_adapter;
    private String get_rubs_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish);
        //初始化控件
        intview();
        //点击事件
        intonick();
        //获取传来的参数，获取源码在获取json
        Intent intent = getIntent();
        get_rubs_data = intent.getStringExtra("get_rubs_data");
        onedata(get_rubs_data);

    }

    //从主页哪里获取的数据
    private void onedata(String rbs_data) {
        if (rbs_data != null) {
            try {
                JSONObject json1 = new JSONObject(rbs_data);
                if (!json1.getString("kw_arr").equals("null")) {
                    //清空数据
                    if (fruitList != null) {
                        fruitList.clear();
                    }
                    JSONArray data_list = json1.getJSONArray("kw_arr");
                    for (int i = 0; i < data_list.length(); i++) {
                        String name = data_list.getJSONObject(i).getString("Name");
                        String key = data_list.getJSONObject(i).getString("TypeKey");
                        Drawable bmp = getDrawable(R.drawable.ic_search_black_24dp);
                        int rubuuish_color = 0xff336699;
                        if (key.equals("湿垃圾")) {
                            bmp = getDrawable(R.drawable.ic_rubbish_wet_24dp);
                            rubuuish_color = 0xFF623F36;
                        }
                        if (key.equals("干垃圾")) {
                            bmp = getDrawable(R.drawable.ic_rubbish_dry_24dp);
                            rubuuish_color = 0xFF2A2A27;
                        }
                        if (key.equals("有害垃圾")) {
                            bmp = getDrawable(R.drawable.ic_rubbish_harmful_24dp);
                            rubuuish_color = 0xFFD72F24;
                        }
                        if (key.equals("可回收")) {
                            bmp = getDrawable(R.drawable.ic_rubbish_recyclable_24dp);
                            rubuuish_color = 0xFF13457C;
                        }
                        if (key.equals("电器电子产品")) {
                            bmp = getDrawable(R.drawable.ic_search_black_24dp);
                            rubuuish_color = 0xFFD72F24;
                        }
                        if (key.equals("装修垃圾")) {
                            bmp = getDrawable(R.drawable.ic_search_black_24dp);
                            rubuuish_color = 0xFFD72F24;
                        }
                        if (key.equals("宠物粪便")) {
                            bmp = getDrawable(R.drawable.ic_search_black_24dp);
                            rubuuish_color = 0xFF2A2A27;
                        }
                        if (key.equals("家用医疗用品")) {
                            bmp = getDrawable(R.drawable.ic_search_black_24dp);
                            rubuuish_color = 0xFFD72F24;
                        }

                        Daily_rubbish_Activity.Fruit data = new Daily_rubbish_Activity.Fruit(name, key, bmp, rubuuish_color);
                        fruitList.add(data);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    rubbish_recycler.setLayoutManager(manager);
                    rubbish_adapter = new Daily_rubbish_Activity.FruitAdapter(fruitList);
                    rubbish_recycler.setAdapter(rubbish_adapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void intonick() {
        //fab点击事件
        rubbish_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View Bottom_edit_view = View.inflate(view.getContext(), R.layout.bottom_dialog_edit, null);
                final BottomSheetDialog bottom_edit_dialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialog);
                bottom_edit_dialog.setContentView(Bottom_edit_view);
                //底部弹窗展开
                BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) Bottom_edit_view.getParent());
                mDialogBehavior.setPeekHeight(mode.getResolution(Daily_rubbish_Activity.this, "h"));

                bottom_edit_dialog.show();
                //标题
                final TextView bottom_edit_title = Bottom_edit_view.findViewById(R.id.bottom_edit_title);
                final Button bottom_edit_enter = Bottom_edit_view.findViewById(R.id.bottom_edit_but);
                final TextInputLayout bottom_edit_inputlayout = Bottom_edit_view.findViewById(R.id.bottom_edit_edit);
                //style
                bottom_edit_title.setText("垃圾分类查询");
                bottom_edit_enter.setText("查询");
                //编辑框行数
                bottom_edit_inputlayout.getEditText().setMaxEms(1);
                //编辑框长度
                bottom_edit_inputlayout.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                //编辑框hint
                bottom_edit_inputlayout.setHint("你是什么垃圾");

                //关闭弹窗
                Bottom_edit_view.findViewById(R.id.bottom_edit_close_icon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottom_edit_dialog.dismiss();
                    }
                });
                //DEBUG
                // bottom_edit_inputlayout.getEditText().setText("鸡蛋");
                //按钮点击事件
                bottom_edit_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final String edit_data = bottom_edit_inputlayout.getEditText().getText().toString();
                        if (!edit_data.equals("")) {
                            String link = "http://trash.lhsr.cn/sites/feiguan/trashTypes_3/Handler/Handler.ashx?a=GET_KEYWORDS&kw=" + edit_data;
                            OkGo.<String>get(link)
                                    .tag(this)
                                    .cacheKey("cacheKey")
                                    .cacheMode(CacheMode.NO_CACHE)
                                    .cacheTime(2000)//缓存时间
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {

                                            if (response.body() != null) {
                                                //判断返回数据是不是空数据
                                                Log.d(TAG, response.body());

                                                try {
                                                    JSONObject json1 = new JSONObject(response.body());
                                                    if (!json1.getString("kw_arr").equals("null")) {
                                                        //清空数据
                                                        if (fruitList != null) {
                                                            fruitList.clear();
                                                        }
                                                        JSONArray data_list = json1.getJSONArray("kw_arr");
                                                        for (int i = 0; i < data_list.length(); i++) {
                                                            String name = data_list.getJSONObject(i).getString("Name");
                                                            String key = data_list.getJSONObject(i).getString("TypeKey");
                                                            Drawable bmp = getDrawable(R.drawable.ic_search_black_24dp);
                                                            int rubuuish_color = 0xff336699;
                                                            if (key.equals("湿垃圾")) {
                                                                bmp = getDrawable(R.drawable.ic_rubbish_wet_24dp);
                                                                rubuuish_color = 0xFF623F36;
                                                            }
                                                            if (key.equals("干垃圾")) {
                                                                bmp = getDrawable(R.drawable.ic_rubbish_dry_24dp);
                                                                rubuuish_color = 0xFF2A2A27;
                                                            }
                                                            if (key.equals("有害垃圾")) {
                                                                bmp = getDrawable(R.drawable.ic_rubbish_harmful_24dp);
                                                                rubuuish_color = 0xFFD72F24;
                                                            }
                                                            if (key.equals("可回收")) {
                                                                bmp = getDrawable(R.drawable.ic_rubbish_recyclable_24dp);
                                                                rubuuish_color = 0xFF13457C;
                                                            }
                                                            if (key.equals("电器电子产品")) {
                                                                bmp = getDrawable(R.drawable.ic_search_black_24dp);
                                                                rubuuish_color = 0xFFD72F24;
                                                            }
                                                            if (key.equals("装修垃圾")) {
                                                                bmp = getDrawable(R.drawable.ic_search_black_24dp);
                                                                rubuuish_color = 0xFFD72F24;
                                                            }
                                                            if (key.equals("宠物粪便")) {
                                                                bmp = getDrawable(R.drawable.ic_search_black_24dp);
                                                                rubuuish_color = 0xFF2A2A27;
                                                            }
                                                            if (key.equals("家用医疗用品")) {
                                                                bmp = getDrawable(R.drawable.ic_search_black_24dp);
                                                                rubuuish_color = 0xFFD72F24;
                                                            }

                                                            Daily_rubbish_Activity.Fruit data = new Daily_rubbish_Activity.Fruit(name, key, bmp, rubuuish_color);
                                                            fruitList.add(data);
                                                        }
                                                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                                        rubbish_recycler.setLayoutManager(manager);
                                                        rubbish_adapter = new Daily_rubbish_Activity.FruitAdapter(fruitList);
                                                        rubbish_recycler.setAdapter(rubbish_adapter);
                                                        bottom_edit_dialog.dismiss();
                                                    } else {
                                                        //数据不对的时候
                                                        bottom_edit_inputlayout.setError("未知垃圾，请换个关键词重试");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    bottom_edit_dialog.dismiss();
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


    private void intview() {
        //计数
        mode.jsphp(this,"rubbish");

        rubbish_toolbar = findViewById(R.id.rubbish_toolbar);
        rubbish_fab = findViewById(R.id.rubbish_fab);
        rubbish_mAppBarLayout = findViewById(R.id.rubbish_mAppBarLayout);
        rubbish_recycler = findViewById(R.id.rubbish_recycler);

        setSupportActionBar(rubbish_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        //通过APPBAR监听底部fab上下滑动隐藏！
        rubbish_mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                rubbish_fab.setTranslationY(-verticalOffset - verticalOffset);
            }
        });
    }

    private class Fruit {
        private final String name;
        private final String key;
        private final Drawable icon;
        private final int color;

        public Fruit(String name, String key, Drawable icon, int color) {
            this.name = name;
            this.key = key;
            this.icon = icon;
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public Drawable getIcon() {
            return icon;
        }
    }

    public static class FruitAdapter extends RecyclerView.Adapter<Daily_rubbish_Activity.FruitAdapter.ViewHolder> {
        private List<Daily_rubbish_Activity.Fruit> mFruitList;
        private String TAG = "TAG";

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_rubbish_title;
            TextView item_rubbish_subtitle;
            CardView item_rubbish_bigcard;
            ImageView item_rubbish_icon;

            public ViewHolder(View view) {
                super(view);
                item_rubbish_title = view.findViewById(R.id.item_rubbish_title);
                item_rubbish_subtitle = view.findViewById(R.id.item_rubbish_subtitle);
                item_rubbish_bigcard = view.findViewById(R.id.item_rubbish_bigcard);
                item_rubbish_icon = view.findViewById(R.id.item_rubbish_icon);
            }
        }

        public FruitAdapter(List<Daily_rubbish_Activity.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public Daily_rubbish_Activity.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rubbish, parent, false);
            Daily_rubbish_Activity.FruitAdapter.ViewHolder holder = new Daily_rubbish_Activity.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Daily_rubbish_Activity.FruitAdapter.ViewHolder holder, int position) {

            final Daily_rubbish_Activity.Fruit fruit = mFruitList.get(position);

            //动画匹配
            Animation animation = AnimationUtils.loadAnimation(holder.item_rubbish_bigcard.getContext(), R.anim.anim_recycler_item_show);
            holder.item_rubbish_bigcard.startAnimation(animation);

            //透明度动画
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(500);
            holder.item_rubbish_title.startAnimation(aa);
            holder.item_rubbish_subtitle.startAnimation(aa);
            holder.item_rubbish_icon.startAnimation(aa);

            //数据传输进去
            holder.item_rubbish_title.setText(fruit.getName());
            holder.item_rubbish_subtitle.setText(fruit.getKey());
            holder.item_rubbish_icon.setImageDrawable(fruit.getIcon());
            //设置颜色
            holder.item_rubbish_icon.setColorFilter(fruit.getColor(), PorterDuff.Mode.SRC_ATOP);
            holder.item_rubbish_title.setTextColor(fruit.getColor());
            holder.item_rubbish_subtitle.setTextColor(fruit.getColor());

            //获取key然后弹窗？
            holder.item_rubbish_bigcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View views) {
                    //Toast.makeText(views.getContext(), fruit.getKey(), Toast.LENGTH_SHORT).show();


                    if (fruit.getKey().equals("湿垃圾")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String title2 = fruit.getKey() + "主要包括";
                        String title3 = fruit.getKey() + "投放要求";
                        String subtitle1 = "剩菜剩饭、瓜皮果核、花卉绿植、过期食品等";
                        String subtitle2 = "纯流质的食物垃圾，如牛奶等，应直接倒进下水口\n有包装物的湿垃圾应将包装物去除后分类投放，包装物请投放到对应的可回收物或干垃圾容器";
                        bottom_rubbish_dialog_text(context, fruit.getIcon(), title1, title2, title3, subtitle1, subtitle2, fruit.getColor());
                    }
                    if (fruit.getKey().equals("干垃圾")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String title2 = fruit.getKey() + "主要包括";
                        String title3 = fruit.getKey() + "投放要求";
                        String subtitle1 = "餐盒、餐巾纸、湿纸巾、卫生间用纸、塑料袋、食品包装袋、污染严重的纸、烟蒂、纸尿裤、一次性杯子、大骨头、贝壳、花盆、陶瓷等";
                        String subtitle2 = "尽量沥干水分\n难以辨识类别的生活垃圾投入干垃圾容器内";
                        bottom_rubbish_dialog_text(context, fruit.getIcon(), title1, title2, title3, subtitle1, subtitle2, fruit.getColor());
                    }
                    if (fruit.getKey().equals("有害垃圾")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String title2 = fruit.getKey() + "主要包括";
                        String title3 = fruit.getKey() + "投放要求";
                        String subtitle1 = "废电池、油漆桶、荧光灯管、废药品及其包装物等";
                        String subtitle2 = "投放时请注意轻放\n易破损的请连带包装或包裹后轻放\n如易挥发，请密封后投放";
                        bottom_rubbish_dialog_text(context, fruit.getIcon(), title1, title2, title3, subtitle1, subtitle2, fruit.getColor());
                    }
                    if (fruit.getKey().equals("可回收")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String title2 = fruit.getKey() + "主要包括";
                        String title3 = fruit.getKey() + "投放要求";
                        String subtitle1 = "酱油瓶、玻璃杯、平板玻璃、易拉罐、饮料瓶、洗发水瓶、塑料玩具、书本、报纸、广告单、纸板箱、衣服、床上用品等";
                        String subtitle2 = "轻投轻放\n清洁干燥，避免污染\n废纸尽量平整\n立体包装物请清空内容物，清洁后压扁投放\n有尖锐边角的，应包裹后投放";
                        bottom_rubbish_dialog_text(context, fruit.getIcon(), title1, title2, title3, subtitle1, subtitle2, fruit.getColor());
                    }
                    if (fruit.getKey().equals("电器电子产品")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String subtitle1 = "你查询的" + fruit.getName() + "属于电器电子产品，可联系规范的电子废弃物回收企业预约回收，或按大件垃圾管理要求投放。";
                        int desion_color = 0xff336699;
                        bottom_rubbish_dialog_text_lite(context, title1, subtitle1, fruit.getColor());
                    }
                    if (fruit.getKey().equals("装修垃圾")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String subtitle1 = "你查询的" + fruit.getName() + "属于装修垃圾，袋装后投放到指定的装修垃圾堆放场所；应与生活垃圾分开收集。";
                        int desion_color = 0xff336699;
                        bottom_rubbish_dialog_text_lite(context, title1, subtitle1, fruit.getColor());

                    }
                    if (fruit.getKey().equals("宠物粪便")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String subtitle1 = "你查询的" + fruit.getName() + "属于宠物粪便，此类物品可能携带细菌病毒寄生虫，不建议进入生活垃圾处理渠道，尽量将其归入城市粪便处理系统；\n实在不便归入粪便处理系统时，可包裹后投入干垃圾桶内。";
                        int desion_color = 0xff336699;
                        bottom_rubbish_dialog_text_lite(context, title1, subtitle1, fruit.getColor());
                    }
                    if (fruit.getKey().equals("家用医疗用品")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String subtitle1 = "你查询的" + fruit.getName() + "属于家用医疗用品，建议投放到医院，社区医院或者设有医废回收的药店；无法进行上述投放时，可以投放到干垃圾。\n小提示:\n生活中出现的这类医疗用品产生量小，不同于医院等医疗场所产生大量的医疗废弃物；\n居住区产生的有害垃圾在进入末端处理之前，有预处理分拣环节涉及到人工分拣，从常情而论，这类使用过的东西与人体直接接触并不安全；\n居住区有害垃圾产量小，所以清运频次远低于干垃圾的清运频次，此类物品在社区内驻存也有卫生隐患。";
                        int desion_color = 0xff336699;
                        bottom_rubbish_dialog_text_lite(context, title1, subtitle1, fruit.getColor());
                    }
                    if (fruit.getKey().equals("大件垃圾")) {
                        Context context = views.getContext();
                        String title1 = fruit.getName() + "属于" + fruit.getKey();
                        String subtitle1 = "你查询的" + fruit.getName() + fruit.getKey() + "，可以预约可回收物经营者或者大件垃圾收集运输单位上门回收，或者投放至管理责任人指定的场所。";
                        bottom_rubbish_dialog_text_lite(context, title1, subtitle1, fruit.getColor());
                    }
                }


            });
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }

    private static void bottom_rubbish_dialog_text_lite(Context context, String title1, String subtitle1, int desion_color) {
        View Bottom_dialog_rubbish_view_lite = View.inflate(context, R.layout.bottom_dialog_rubbish_lite, null);
        final BottomSheetDialog bottom_rubbish_dialog_lite = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        bottom_rubbish_dialog_lite.setContentView(Bottom_dialog_rubbish_view_lite);
        bottom_rubbish_dialog_lite.show();

        //初始化相关
        final ImageView bottom_rubbish_icon_lite = Bottom_dialog_rubbish_view_lite.findViewById(R.id.bottom_rubbish_icon_lite);
        final ImageView bottom_rubbish_close_icon_lite = Bottom_dialog_rubbish_view_lite.findViewById(R.id.bottom_rubbish_close_icon_lite);
        final TextView bottom_rubbish_title1_lite = Bottom_dialog_rubbish_view_lite.findViewById(R.id.bottom_rubbish_title1_lite);
        final TextView bottom_rubbish_title2_lite = Bottom_dialog_rubbish_view_lite.findViewById(R.id.bottom_rubbish_title2_lite);
        final TextView bottom_rubbish_subtitle1_lite = Bottom_dialog_rubbish_view_lite.findViewById(R.id.bottom_rubbish_subtitle1_lite);
        final TextView bottom_rubbish_subtitle3_lite = Bottom_dialog_rubbish_view_lite.findViewById(R.id.bottom_rubbish_subtitle3_lite);
        final LinearLayout bottom_rubbish_tion1_lite = Bottom_dialog_rubbish_view_lite.findViewById(R.id.bottom_rubbish_tion1_lite);


        //大标题1
        bottom_rubbish_title1_lite.setText(title1);
        //图标设置
        bottom_rubbish_icon_lite.setImageDrawable(context.getDrawable(R.drawable.ic_search_black_24dp));
        //小标题1
        bottom_rubbish_subtitle1_lite.setText(subtitle1);


        //所有的颜色设置
        bottom_rubbish_icon_lite.setColorFilter(desion_color, PorterDuff.Mode.SRC_ATOP);
        bottom_rubbish_close_icon_lite.setColorFilter(desion_color, PorterDuff.Mode.SRC_ATOP);
        bottom_rubbish_title1_lite.setTextColor(desion_color);
        bottom_rubbish_title2_lite.setTextColor(desion_color);
        bottom_rubbish_subtitle3_lite.setTextColor(desion_color);
        bottom_rubbish_tion1_lite.setBackgroundColor(desion_color);

        //关闭弹窗
        bottom_rubbish_close_icon_lite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_rubbish_dialog_lite.dismiss();
            }
        });


    }

    private static void bottom_rubbish_dialog_text(Context context, Drawable icon, String title1, String title2, String title3, String subtitle1, String subtitle2, int desion_color) {
        View Bottom_dialog_rubbish_view = View.inflate(context, R.layout.bottom_dialog_rubbish, null);
        final BottomSheetDialog bottom_rubbish_dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        bottom_rubbish_dialog.setContentView(Bottom_dialog_rubbish_view);
        bottom_rubbish_dialog.show();

        //初始化相关
        final ImageView bottom_rubbish_icon = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_icon);
        final ImageView bottom_rubbish_close_icon = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_close_icon);
        final TextView bottom_rubbish_title1 = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_title1);
        final TextView bottom_rubbish_title2 = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_title2);
        final TextView bottom_rubbish_title3 = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_title3);
        final TextView bottom_rubbish_subtitle1 = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_subtitle1);
        final TextView bottom_rubbish_subtitle2 = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_subtitle2);
        final TextView bottom_rubbish_subtitle3 = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_subtitle3);
        final LinearLayout bottom_rubbish_tion1 = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_tion1);
        final LinearLayout bottom_rubbish_tion2 = Bottom_dialog_rubbish_view.findViewById(R.id.bottom_rubbish_tion2);


        //大标题1
        bottom_rubbish_title1.setText(title1);
        //设置图标
        bottom_rubbish_icon.setImageDrawable(icon);
        //图标颜色
        bottom_rubbish_icon.setColorFilter(mode.getColorPrimary(context), PorterDuff.Mode.SRC_ATOP);
        //大标题2
        bottom_rubbish_title2.setText(title2);
        //大标题3
        bottom_rubbish_title3.setText(title3);
        //小标题1
        bottom_rubbish_subtitle1.setText(subtitle1);
        //小标题2
        bottom_rubbish_subtitle2.setText(subtitle2);

        //所有的颜色设置
        bottom_rubbish_icon.setColorFilter(desion_color, PorterDuff.Mode.SRC_ATOP);
        bottom_rubbish_close_icon.setColorFilter(desion_color, PorterDuff.Mode.SRC_ATOP);
        bottom_rubbish_title1.setTextColor(desion_color);
        bottom_rubbish_title2.setTextColor(desion_color);
        bottom_rubbish_title3.setTextColor(desion_color);
        bottom_rubbish_subtitle3.setTextColor(desion_color);
        bottom_rubbish_tion1.setBackgroundColor(desion_color);
        bottom_rubbish_tion2.setBackgroundColor(desion_color);

        //关闭弹窗
        bottom_rubbish_close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_rubbish_dialog.dismiss();
            }
        });


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
