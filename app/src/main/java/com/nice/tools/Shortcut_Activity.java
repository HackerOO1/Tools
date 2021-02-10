package com.nice.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Shortcut_Activity extends AppCompatActivity {
    String TAG = "TAG";
    private Toolbar shortcut_toolbar;
    private RecyclerView shortcut_recycler;
    private List<Shortcut_Activity.Fruit> fruitList = new ArrayList<>();
    private Shortcut_Activity.FruitAdapter shortcut_adapter;
    private String[] shortcut_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        intview();
        intdata();
    }

    private void intdata() {
        shortcut_list = new String[]{"支付宝扫码；Intent_Jump_Activity:aipay_scon", "支付宝付款码；Intent_Jump_Activity:aipay_fukuan", "支付宝乘车码；Intent_Jump_Activity:ai_bus", "微信扫一扫；Intent_Jump_Activity:vx_scon",
                "快递查询；Daily_crydear_Activity:Crydear", /*"垃圾分类查询；Daily_rubbish_Activity:Rubbish",*/ "翻译；Daily_translation_Activity:Translation", "亲戚计算器；Daily_famliy_Activity:Famliy",
                "电子时钟；Daily_oclock_nu_Activity:Oclock_nu", "Bilibili封面获取；There_getbiliimg_Activity:Bili", "微信公众号封面获取；There_getweixinimg_Activity:Weixin",
                "专注模式；Tool_countdown_Activity:Countdown", "指南针；Daily_compass_Activity:Compass", "时钟；Tool_oclock_Activity:Oclock", "直尺；Tool_scaleView_Activity:ScaleView",
                "LED屏幕；Tool_led_open_Activity:Led", "分贝仪；Tool_audio_Activity:Audio", "随机数生成；Tool_sran_number_Activity:sran_number", "抓取壁纸；Tool_getwallpic_Activity:getwallpic",
                "获取网页源码；Tool_gethtml_Activity:Html", "按摩器；Tool_anmo_Activity:anmo", "历史上的今天；Tool_pastoday_Activity:pastoday", "图片取色；Pic_colorqs_Activity:Color_qs",
                "调色板；Pic_colorpaick_Activity:Color_paick", "字符图；Pic_totxtpic_Activity:Pic_totxtpic", "文字转图片；Pic_txtpic_Activity:Pic_txtpic",
                "图片转连接；Pic_imgtorui_Activity:Pic_imgtorui", "计数器；Tool_count_Activity:count", "数字转大写；Tool_tochinanum_Activity:tochinanum"};

        for (String shortcut_list_data : shortcut_list) {
            String shortcut_title = mode.Sj(shortcut_list_data, null, "；");
            String shortcut_class = mode.Sj(shortcut_list_data, "；", ":");
            String shortcut_tion = mode.Sj(shortcut_list_data, ":", null);
            Shortcut_Activity.Fruit data = new Shortcut_Activity.Fruit(shortcut_title, shortcut_class, shortcut_tion);
            fruitList.add(data);
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        shortcut_recycler.setLayoutManager(manager);

        Shortcut_Activity.FruitAdapter adapter = new Shortcut_Activity.FruitAdapter(fruitList);
        shortcut_recycler.setAdapter(adapter);
    }


    private void intview() {
        //计数
        mode.jsphp(this,"shortcut");

        shortcut_toolbar = findViewById(R.id.shortcut_toolbar);
        shortcut_recycler = findViewById(R.id.shortcut_recycler);
        setSupportActionBar(shortcut_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    private class Fruit {
        private final String shortcut_title;
        private final String shortcut_class;
        private final String shortcut_tion;

        public Fruit(String shortcut_title, String shortcut_class, String shortcut_tion) {
            this.shortcut_title = shortcut_title;
            this.shortcut_class = shortcut_class;
            this.shortcut_tion = shortcut_tion;
        }

        public String getShortcut_class() {
            return shortcut_class;
        }

        public String getShortcut_tion() {
            return shortcut_tion;
        }

        public String getShortcut_title() {
            return shortcut_title;
        }
    }

    public static class FruitAdapter extends RecyclerView.Adapter<Shortcut_Activity.FruitAdapter.ViewHolder> {
        private List<Shortcut_Activity.Fruit> mFruitList;
        private String TAG = "TAG";

        static class ViewHolder extends RecyclerView.ViewHolder {
            CardView item_shortcut_card;
            TextView item_shortcut_title;
            RelativeLayout item_shortcut_imglayout;
            TextView item_shortcut_imgtxt;


            public ViewHolder(View view) {
                super(view);
                item_shortcut_card = view.findViewById(R.id.item_shortcut_card);
                item_shortcut_title = view.findViewById(R.id.item_shortcut_title);
                item_shortcut_imglayout = view.findViewById(R.id.item_shortcut_imglayout);
                item_shortcut_imgtxt = view.findViewById(R.id.item_shortcut_imgtxt);
            }
        }

        public FruitAdapter(List<Shortcut_Activity.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public Shortcut_Activity.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shortcut, parent, false);
            Shortcut_Activity.FruitAdapter.ViewHolder holder = new Shortcut_Activity.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Shortcut_Activity.FruitAdapter.ViewHolder holder, int position) {

            final Shortcut_Activity.Fruit fruit = mFruitList.get(position);

            //动画匹配
            Animation animation = AnimationUtils.loadAnimation(holder.item_shortcut_card.getContext(), R.anim.anim_recycler_item_show);
            holder.item_shortcut_card.startAnimation(animation);

            //透明度动画
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(500);
            holder.item_shortcut_imglayout.startAnimation(aa);


            //数据传输进去
            holder.item_shortcut_title.setText(fruit.getShortcut_title());
            holder.item_shortcut_imgtxt.setText(fruit.getShortcut_title());


            //跳转开始查询，传入单号查询！
            holder.item_shortcut_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View views) {
                    //模块读取数据添加快捷方式
                    View bottom_dialog_massage_view = View.inflate(views.getContext(), R.layout.bottom_dialog_massage, null);
                    final BottomSheetDialog bottom_dialog_massage = new BottomSheetDialog(views.getContext(), R.style.BottomSheetDialog);
                    bottom_dialog_massage.setContentView(bottom_dialog_massage_view);
                    //底部弹窗展开
                    BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) bottom_dialog_massage_view.getParent());
                    mDialogBehavior.setPeekHeight(mode.getResolution(views.getContext(), "h"));

                    bottom_dialog_massage.show();
                    //标题
                    final ImageView bottom_dialog_massage_icon = bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_icon);
                    final TextView bottom_dialog_massage_title = bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_title);
                    final TextView bottom_dialog_massage_show = bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_show);

                    final MaterialButton bottom_dialog_massage_button = bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_button);

                    //style
                    bottom_dialog_massage_title.setText("提示");
                    bottom_dialog_massage_show.setText("确认给予本软件添加快捷方式的权限并开始添加快捷方式？");
                    bottom_dialog_massage_button.setText("添加");

                    //关闭弹窗
                    bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_close_icon).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottom_dialog_massage.dismiss();
                        }
                    });
                    bottom_dialog_massage_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            try {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intent.setClass(views.getContext(), Class.forName("com.nice.tools." + fruit.shortcut_class));
                                intent.putExtra("Jump_tion_data", fruit.shortcut_tion);
                                mode.installShortCut(views.getContext(), fruit.shortcut_tion, fruit.shortcut_title, mode.view2toimg(holder.item_shortcut_imglayout), intent);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(view.getContext(), "已尝试添加快捷方式，请返回桌面查看，如果没有添加快捷方式，请到手机设置中打开本软件添加快捷方式权限", Toast.LENGTH_LONG).show();
                            bottom_dialog_massage.dismiss();
                        }
                    });
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
