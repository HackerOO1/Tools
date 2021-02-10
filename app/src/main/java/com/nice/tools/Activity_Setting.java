package com.nice.tools;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Activity_Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        intview();
    }

    private void intview() {
        Toolbar setting_index_toolbar = findViewById(R.id.setting_index_toolbar);
        setSupportActionBar(setting_index_toolbar);
        //设置主界面
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_index_mi, new Prefs_setting())
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        //返回键事件
        setting_index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}