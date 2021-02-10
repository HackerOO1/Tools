package com.nice.tools;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class Activity_note extends AppCompatActivity {
    public AppBarLayout note_appbar;
    public Toolbar note_toolbar;
    public TabLayout note_tablayout;
    public ViewPager note_viewpager;
    private String[] note_titles = {"公告","更新记录"};
    private String[] note_mTitles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        intview();
    }

    private void intview() {
        note_appbar = findViewById(R.id.note_appbar);
        note_toolbar = findViewById(R.id.note_toolbar);
        note_tablayout = findViewById(R.id.note_tablayout);
        note_viewpager = findViewById(R.id.note_viewpager);
        setSupportActionBar(note_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        //返回键事件
        note_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        note_tablayout.addTab(note_tablayout.newTab().setText(note_titles[0]));
        note_tablayout.addTab(note_tablayout.newTab().setText(note_titles[1]));
        MyFragmentViewPager myFragmentViewPager = new MyFragmentViewPager(getSupportFragmentManager(), note_titles);
        note_viewpager.setAdapter(myFragmentViewPager);

        //表示将TabLayout 和Viewpager 进行关联
        note_tablayout.setupWithViewPager(note_viewpager);

        note_viewpager.setOffscreenPageLimit(2);
    }

    //适配器
    private class MyFragmentViewPager extends FragmentPagerAdapter {

        public MyFragmentViewPager(FragmentManager fm, String[] titles) {
            super(fm);
            note_mTitles = titles;
        }

        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Activity_note_vp1();
                case 1:
                    return new Activity_note_vp2();
            }
            return new Activity_note_vp1();
        }

        @Override
        public int getCount() {
            return note_mTitles.length;
        }

        /**
         * 该函数是搭配TabLayout 布局所需重写的 ,如若不绑定TabLayout 布局，那么可以不重写
         *   mTl.setupWithViewPager(mVp);
         * */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return note_mTitles[position];
        }
    }
}