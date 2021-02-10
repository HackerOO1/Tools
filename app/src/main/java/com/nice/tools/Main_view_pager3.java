package com.nice.tools;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class Main_view_pager3 extends Fragment {
    private View mian3view;
    private String TAG = "TAG";
    private RecyclerView main_vp3_recycler;
    private LinearLayout main_vp3_title_bg1;
    private List<Main_view_pager3.Fruit> fruitList = new ArrayList<>();
    private Main_view_pager3.FruitAdapter Main_vp3_recycler_adapter;
    private String[] Main_vp3_mrecdata_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mian3view = View.inflate(inflater.getContext(), R.layout.activity_main_vp3, null);
        ingview();
        intdata();
        return mian3view;
    }

    private void ingview() {

        main_vp3_recycler = mian3view.findViewById(R.id.main_vp3_recycler);
        main_vp3_title_bg1 = mian3view.findViewById(R.id.main_vp3_title_bg1);
    }

    private void intdata() {
        int colors[] = {0xffffffff, 0xfff5f5f5};
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        main_vp3_title_bg1.setBackground(bg);

        //点击事件
        /* main_vp3_widgets1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();
            }
        });*/
        //适配瀑布流

        Main_vp3_mrecdata_list = new String[]{"垃圾分类查询;有效分辨垃圾的分类，并提示相关信息", "翻译;支持中、英、日、韩、法、俄等语言翻译",
                "BiliBili封面获取;查看、下载B站视频封面图片", "电子时钟;废旧淘汰的手机再利用，继续发挥余热",
                "直尺;手机屏幕直接当作尺子使用", "LED屏幕;全屏文字滚动，可以当作广告牌", "分贝仪;直观的暂时当前环境噪音数值",
                "数字转大写;阿拉伯数字转为汉语大写，常用作金额书写", "计数器;做任何事都需要坚持下去，奥里给！",
                "图片转连接;上传图片到云端并返回一个图片的链接"};

        for (String Main_vp3_mrec_data : Main_vp3_mrecdata_list) {
            String subtitle = mode.Sj(Main_vp3_mrec_data, ";", null);
            String title = mode.Sj(Main_vp3_mrec_data, null, ";");
            Main_view_pager3.Fruit data = new Main_view_pager3.Fruit(title, subtitle);
            fruitList.add(data);
        }

        //瀑布流
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        main_vp3_recycler.setLayoutManager(layoutManager);
        Main_vp3_recycler_adapter = new Main_view_pager3.FruitAdapter(fruitList);
        main_vp3_recycler.setAdapter(Main_vp3_recycler_adapter);

    }

    private class Fruit {
        private final String title;
        private final String subtitle;

        public Fruit(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }

    public static class FruitAdapter extends RecyclerView.Adapter<Main_view_pager3.FruitAdapter.ViewHolder> {
        private List<Main_view_pager3.Fruit> mFruitList;
        private String TAG = "TAG";

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_main_vp3_rec_title;
            TextView item_main_vp3_rec_subtitle;
            CardView item_main_vp3_rec_card;

            public ViewHolder(View view) {
                super(view);
                item_main_vp3_rec_title = view.findViewById(R.id.item_main_vp3_rec_title);
                item_main_vp3_rec_subtitle = view.findViewById(R.id.item_main_vp3_rec_subtitle);
                item_main_vp3_rec_card = view.findViewById(R.id.item_main_vp3_rec_card);
            }
        }

        public FruitAdapter(List<Main_view_pager3.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public Main_view_pager3.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_vp3_recycler, parent, false);
            Main_view_pager3.FruitAdapter.ViewHolder holder = new Main_view_pager3.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Main_view_pager3.FruitAdapter.ViewHolder holder, int position) {

            final Main_view_pager3.Fruit fruit = mFruitList.get(position);

            //动画匹配
            Animation animation = AnimationUtils.loadAnimation(holder.item_main_vp3_rec_title.getContext(), R.anim.anim_recycler_item_show);
            //holder.item_main_vp3_rec_card.startAnimation(animation);
            //透明度动画
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(500);
            holder.item_main_vp3_rec_card.startAnimation(aa);

            //数据传输进去
            holder.item_main_vp3_rec_title.setText(fruit.getTitle());
            holder.item_main_vp3_rec_subtitle.setText(fruit.getSubtitle());


            holder.item_main_vp3_rec_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View views) {
                    Main_click.toclass(views.getContext(), fruit.getTitle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }
}