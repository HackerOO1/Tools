package com.nice.tools;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.nice.tools.widget.FlowTagLayout;
import com.nice.tools.widget.OnInitSelectedPosition;

import java.util.ArrayList;
import java.util.List;


public class Main_view_pager2 extends Fragment {
    private String TAG = "TAG";
    private View mian2view;
    private FlowTagLayout mian_vp2_daily_flowayout;
    private Main_view_pager2.TagAdapter<String> mian_vp2_daily_flowayout_TagAdapter;
    private List<String> mian_vp2_daily_flowayout_dataSource;

    private FlowTagLayout mian_vp2_other_flowayout;
    private Main_view_pager2.TagAdapter<String> mian_vp2_other_flowayout_TagAdapter;
    private List<String> mian_vp2_other_flowayout_dataSource;

    private FlowTagLayout mian_vp2_tools_flowayout;
    private Main_view_pager2.TagAdapter<String> mian_vp2_tools_flowayout_TagAdapter;
    private List<String> mian_vp2_tools_flowayout_dataSource;


    private FlowTagLayout mian_vp2_inter_flowayout;
    private Main_view_pager2.TagAdapter<String> mian_vp2_inter_flowayout_TagAdapter;
    private List<String> mian_vp2_inter_flowayout_dataSource;

    private FlowTagLayout mian_vp2_photo_flowayout;
    private Main_view_pager2.TagAdapter<String> mian_vp2_photo_flowayout_TagAdapter;
    private List<String> mian_vp2_photo_flowayout_dataSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mian2view = View.inflate(inflater.getContext(), R.layout.activity_main_vp2, null);
        intdata();
        return mian2view;
    }

    private void intdata() {
        //日常
        mian_vp2_daily_flowayout = mian2view.findViewById(R.id.mian_vp2_daily_flowayout);
        mian_vp2_daily_flowayout_TagAdapter = new Main_view_pager2.TagAdapter<>(getContext());
        mian_vp2_daily_flowayout.setAdapter(mian_vp2_daily_flowayout_TagAdapter);
        mian_vp2_daily_flowayout_dataSource = new ArrayList<>();
        //mian_vp2_daily_flowayout_dataSource.add("快递查询");
        mian_vp2_daily_flowayout_dataSource.add("垃圾分类查询");
        mian_vp2_daily_flowayout_dataSource.add("翻译");
        mian_vp2_daily_flowayout_dataSource.add("亲戚计算器");
        mian_vp2_daily_flowayout_dataSource.add("电子时钟");

        //第三方
        mian_vp2_daily_flowayout_TagAdapter.onlyAddAll(mian_vp2_daily_flowayout_dataSource);
        mian_vp2_other_flowayout = mian2view.findViewById(R.id.mian_vp2_other_flowayout);
        mian_vp2_other_flowayout_TagAdapter = new Main_view_pager2.TagAdapter<>(getContext());
        mian_vp2_other_flowayout.setAdapter(mian_vp2_other_flowayout_TagAdapter);
        mian_vp2_other_flowayout_dataSource = new ArrayList<>();
        mian_vp2_other_flowayout_dataSource.add("BiliBili封面获取");
        mian_vp2_other_flowayout_dataSource.add("微信公众号封面获取");
        mian_vp2_other_flowayout_dataSource.add("QQ强制对话");
        mian_vp2_other_flowayout_dataSource.add("音乐解析");
        mian_vp2_other_flowayout_TagAdapter.onlyAddAll(mian_vp2_other_flowayout_dataSource);

        //小工具
        mian_vp2_tools_flowayout = mian2view.findViewById(R.id.mian_vp2_tools_flowayout);
        mian_vp2_tools_flowayout_TagAdapter = new Main_view_pager2.TagAdapter<>(getContext());
        mian_vp2_tools_flowayout.setAdapter(mian_vp2_tools_flowayout_TagAdapter);
        mian_vp2_tools_flowayout_dataSource = new ArrayList<>();
        mian_vp2_tools_flowayout_dataSource.add("专注模式");
        mian_vp2_tools_flowayout_dataSource.add("指南针");
        mian_vp2_tools_flowayout_dataSource.add("时钟");
        mian_vp2_tools_flowayout_dataSource.add("直尺");
        mian_vp2_tools_flowayout_dataSource.add("LED屏幕");
        mian_vp2_tools_flowayout_dataSource.add("分贝仪");
        mian_vp2_tools_flowayout_dataSource.add("随机数生成");
        mian_vp2_tools_flowayout_dataSource.add("抓取壁纸");
        mian_vp2_tools_flowayout_dataSource.add("获取网页源码");
        mian_vp2_tools_flowayout_dataSource.add("按摩器");
        mian_vp2_tools_flowayout_dataSource.add("历史上的今天");
        mian_vp2_tools_flowayout_dataSource.add("计数器");
        mian_vp2_tools_flowayout_dataSource.add("数字转大写");
        mian_vp2_tools_flowayout_TagAdapter.onlyAddAll(mian_vp2_tools_flowayout_dataSource);

        //图片工具
        mian_vp2_photo_flowayout = mian2view.findViewById(R.id.mian_vp2_photo_flowayout);
        mian_vp2_photo_flowayout_TagAdapter = new Main_view_pager2.TagAdapter<>(getContext());
        mian_vp2_photo_flowayout.setAdapter(mian_vp2_photo_flowayout_TagAdapter);
        mian_vp2_photo_flowayout_dataSource = new ArrayList<>();
        mian_vp2_photo_flowayout_dataSource.add("图片取色");
        mian_vp2_photo_flowayout_dataSource.add("调色板");
        mian_vp2_photo_flowayout_dataSource.add("字符图");
        mian_vp2_photo_flowayout_dataSource.add("文字转图片");
/*        mian_vp2_photo_flowayout_dataSource.add("带壳截图");
        mian_vp2_photo_flowayout_dataSource.add("二维码生成");*/
        mian_vp2_photo_flowayout_dataSource.add("图片转连接");
        mian_vp2_photo_flowayout_TagAdapter.onlyAddAll(mian_vp2_photo_flowayout_dataSource);

    }

    //日常引用下面的流布局适配器
    public class TagAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {
        private final Context mContext;
        private final List<T> mDataList;

        public TagAdapter(Context context) {
            this.mContext = context;
            mDataList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //设置item不点击
        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mian_vp2_flw, null);
            final MaterialButton Button = view.findViewById(R.id.main_vp2_item_button_tag);
            T t = mDataList.get(position);
            if (t instanceof String) {
                Button.setText((String) t);
                Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View views) {
                        Main_click.toclass(views.getContext(), Button.getText().toString());
                    }
                });
            }
            return view;
        }

        public void onlyAddAll(List<T> datas) {
            mDataList.addAll(datas);
            notifyDataSetChanged();
        }

        public int position;

        @Override
        public boolean isSelectedPosition(int poi) {
            if (poi == position) {
                return true;
            }
            return false;
        }
    }

    private int getWindowHeight() {
        Resources res = Main_view_pager2.this.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

}