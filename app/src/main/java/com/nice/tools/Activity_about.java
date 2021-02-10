package com.nice.tools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Activity_about extends AppCompatActivity {
    public Toolbar about_index_toolbar;
    public ImageView about_index_icon;
    public TextView about_index_version;
    public TextView about_index_appname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        intview();
        intdata();
    }

    private void intdata() {
        //图标
        Drawable icon = mode.getIcon(this, this.getPackageName());
        about_index_icon.setImageBitmap(mode.DrawableToBitmap(icon));
        //版本
        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        about_index_version.setText("Version:" + pi.versionName);
        //名称
        //about_index_appname.setText(mode.getVersionName(this, this.getPackageName()));
    }

    private void intview() {
        about_index_toolbar = findViewById(R.id.about_index_toolbar);
        about_index_icon = findViewById(R.id.about_index_icon);
        about_index_version = findViewById(R.id.about_index_version);
        about_index_appname = findViewById(R.id.about_index_appname);
        setSupportActionBar(about_index_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        //返回键事件
        about_index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}