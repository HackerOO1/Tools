package com.nice.tools;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.scwang.wave.MultiWaveHeader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "TAG";
    private Toolbar main_toolbar;
    private DrawerLayout main_drawerLayout;
    private ViewPager main_viewpager;
    private BottomNavigationView main_bottom_navigation;
    private FragmentPagerAdapter mPagerAdapter;
    private AppBarLayout main_mAppBarLayout;
    private MultiWaveHeader waveHeader;
    private CoordinatorLayout main_coordinatorlayout;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    //数据存储——所有数据
    private SharedPreferences preferences_index;
    private SharedPreferences.Editor preferences_editor_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intview();
        intdata();
        intpermission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {//开启定位权限
            mode.getLocation_test(this);
        }

    }

    private void intdata() {
        //检查更新
        boolean pref_setting_atupdata = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_setting_atupdata", false);
        if (pref_setting_atupdata) {
            Prefs_setting.pref_updata(MainActivity.this, "AT");
        }
        final int preferences_index_jsphp = preferences_index.getInt("preferences_index_jsphp", 0);
        //统计开启次数并显示隐私说明
        if (preferences_index_jsphp == 0) {
            AlertDialog.Builder massage_dialog = new AlertDialog.Builder(this);
            massage_dialog.setCancelable(false);
            massage_dialog.setTitle("服务协议与隐私说明");
            massage_dialog.setMessage("请你务必谨慎阅读、充分理解“隐私政策”各项说明。\n包括但不限于：\n为了提供【获取B站视屏封面】功能申请的手机存储权限\n为了更好功能体验，我们" +
                    "需要收集你的设备信息等个人信息。\n你可以在设置-『隐私政策』选项中详细阅读本说明。" +
                    "\n如果你同意，请点击“同意”开始接受我们的服务");
            massage_dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preferences_editor_index.putInt("preferences_index_jsphp", preferences_index_jsphp + 1);
                    preferences_editor_index.apply();
                    preferences_editor_index.commit();
                }
            });
            massage_dialog.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            massage_dialog.setNeutralButton("阅读", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mode.inter(MainActivity.this, "http://tbook.top/iso/ntools/privacy/");
                    finish();
                }
            });
            massage_dialog.create().setCanceledOnTouchOutside(false);
            massage_dialog.show();
        } else {
            preferences_editor_index.putInt("preferences_index_jsphp", preferences_index_jsphp + 1);
            preferences_editor_index.apply();
            preferences_editor_index.commit();
        }
        //在线计数
        mode.jsphp_index(this);
    }

    private void intpermission() {
        //申请权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    private void intview() {
        main_toolbar = findViewById(R.id.main_toolbar);
        main_drawerLayout = findViewById(R.id.main_drawer_layout);
        main_viewpager = findViewById(R.id.main_view_pager);
        main_bottom_navigation = findViewById(R.id.main_bottom_navigation);
        main_mAppBarLayout = findViewById(R.id.main_appbar);
        main_coordinatorlayout = findViewById(R.id.main_coordinatorlayout);
        //水波背纹
        //waveHeader = findViewById(R.id.waveHeader);
        //通过APPBAR监听底部tabbar上下滑动隐藏！
        main_mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                main_bottom_navigation.setTranslationY(-verticalOffset);
                //waveHeader.setTranslationY(-verticalOffset);
            }
        });

        //设置导航切换监听
        main_bottom_navigation.setOnNavigationItemSelectedListener(changeFragment);
        setViewPagerListener();
        initFragment();

        //设置适配器
        main_viewpager.setAdapter(mPagerAdapter);

        //设置缓存
        main_viewpager.setOffscreenPageLimit(4);

        //设置工具栏
        setSupportActionBar(main_toolbar);

        //侧滑绑定按钮动画
        setSupportActionBar(main_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, main_drawerLayout, main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        main_drawerLayout.addDrawerListener(toggle);

        //获取侧滑view绑定item事件
        NavigationView navigationView = findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //通过view获取里面的控件id可设置事件
        View headerView = navigationView.getHeaderView(0);
        LinearLayout nav_header = headerView.findViewById(R.id.main_nav_header);

        //数据存储——所有数据
        preferences_index = this.getSharedPreferences("preferences_index", MODE_PRIVATE);
        preferences_editor_index = preferences_index.edit();
    }


    private void initFragment() {
        //底部导航栏有几项就有几个Fragment
        final ArrayList<Fragment> Main_Lists = new ArrayList<>(3);
        Main_Lists.add(new Main_view_pager1());
        Main_Lists.add(new Main_view_pager2());
        Main_Lists.add(new Main_view_pager3());

        //设置适配器用于装载Fragment,ViewPager的好朋友
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return Main_Lists.get(position);  //得到Fragment
            }

            @Override
            public int getCount() {
                return Main_Lists.size();  //得到数量
            }
        };
    }

    //这里有3中滑动过程,我们用点击后就可以
    private void setViewPagerListener() {
        main_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //滑动页面后做的事，这里与BottomNavigationView结合，使其与正确page对应
                main_bottom_navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    //判断选择的菜单,点击哪个就设置到对应的Fragment
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_blue: {
                    main_viewpager.setCurrentItem(0);
                    return true;
                }
                case R.id.bottom_navigation_green: {
                    main_viewpager.setCurrentItem(1);
                    return true;
                }
                case R.id.bottom_navigation_yellow: {
                    main_viewpager.setCurrentItem(2);
                    return true;
                }
            }
            return false;
        }
    };


    //加载右边菜单按键
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //获取ID，判断id，设置事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.menu_setting:
                this.startActivity(new Intent(this, Activity_Setting.class));
                break;
            case R.id.menu_about:
                this.startActivity(new Intent(this, Activity_about.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem Naviitem) {
        switch (Naviitem.getItemId()) {
            case R.id.main_nav_setting:
                this.startActivity(new Intent(this, Activity_Setting.class));
                break;
            case R.id.main_nav_shortcut:
                this.startActivity(new Intent(this, Shortcut_Activity.class));
                break;
            case R.id.main_nav_about:
                this.startActivity(new Intent(this, Activity_about.class));
                break;
            case R.id.main_nav_note:
                this.startActivity(new Intent(this, Activity_note.class));
                break;
        }
        main_drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}