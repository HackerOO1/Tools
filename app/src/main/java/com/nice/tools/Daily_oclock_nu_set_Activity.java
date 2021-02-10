package com.nice.tools;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.appbar.AppBarLayout;

public class Daily_oclock_nu_set_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    private Toolbar oclock_nu_set_toolbar;
    private AppBarLayout oclock_nu_set_mAppBarLayout;
    private RelativeLayout oclock_nu_set_time_color;
    private RelativeLayout oclock_nu_set_date_color;
    private RelativeLayout oclock_nu_set_bg_color;
    private CardView oclock_nu_set_time_card;
    private CardView oclock_nu_set_date_card;
    private CardView oclock_nu_set_bg_card;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oclock_nu_set);
        //初始化xml数据存储类
        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();
        //初始化控件
        intview();
        //设置数据
        intdata();
    }



    private void intview() {
        oclock_nu_set_toolbar = findViewById(R.id.oclock_nu_set_toolbar);
        oclock_nu_set_mAppBarLayout = findViewById(R.id.oclock_nu_set_mAppBarLayout);
        oclock_nu_set_time_color = findViewById(R.id.oclock_nu_set_time_color);
        oclock_nu_set_date_color = findViewById(R.id.oclock_nu_set_date_color);
        oclock_nu_set_bg_color = findViewById(R.id.oclock_nu_set_bg_color);
        oclock_nu_set_time_card = findViewById(R.id.oclock_nu_set_time_card);
        oclock_nu_set_date_card = findViewById(R.id.oclock_nu_set_date_card);
        oclock_nu_set_bg_card = findViewById(R.id.oclock_nu_set_bg_card);
        //设置toolbar
        setSupportActionBar(oclock_nu_set_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        //默认数据添加上去
        int nuclock_time_color = nicetools_prf.getInt("nuclock_time_color", -1);
        oclock_nu_set_time_card.setCardBackgroundColor(nuclock_time_color);
        int nuclock_date_color = nicetools_prf.getInt("nuclock_date_color", -1);
        oclock_nu_set_date_card.setCardBackgroundColor(nuclock_date_color);
        int nuclock_bg_color = nicetools_prf.getInt("nuclock_bg_color", -16777216);
        oclock_nu_set_bg_card.setCardBackgroundColor(nuclock_bg_color);
    }

    private void intdata() {
        //设置背景颜色
        oclock_nu_set_bg_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuclock_bg_color = nicetools_prf.getInt("nuclock_bg_color", -16777216);
                ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle("背景颜色")//设置标题
                        //传入的默认color
                        .initialColor(nuclock_bg_color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)//设置花型
                        .showAlphaSlider(false)
                        //.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)//设置圆形也可以
                        .density(10)//设置距离
                        //监听
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                oclock_nu_set_bg_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("nuclock_bg_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .setPositiveButton("确定", new ColorPickerClickListener() {//设置确定事件
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                oclock_nu_set_bg_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("nuclock_bg_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .build().show();
            }
        });
        //设置日期颜色
        oclock_nu_set_date_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuclock_date_color = nicetools_prf.getInt("nuclock_date_color", -1);
                ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle("日期颜色")//设置标题
                        //传入的默认color
                        .initialColor(nuclock_date_color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)//设置花型
                        .showAlphaSlider(true)
                        //.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)//设置圆形也可以
                        .density(10)//设置距离
                        //监听
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                oclock_nu_set_date_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("nuclock_date_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .setPositiveButton("确定", new ColorPickerClickListener() {//设置确定事件
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                oclock_nu_set_date_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("nuclock_date_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .build().show();
            }
        });
        //设置时间颜色
        oclock_nu_set_time_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuclock_time_color = nicetools_prf.getInt("nuclock_time_color", -1);
                ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle("时钟颜色")//设置标题
                        //传入的默认color
                        .initialColor(nuclock_time_color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)//设置花型
                        .showAlphaSlider(true)
                        //.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)//设置圆形也可以
                        .density(10)//设置距离
                        //监听
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                oclock_nu_set_time_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("nuclock_time_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .setPositiveButton("确定", new ColorPickerClickListener() {//设置确定事件
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                oclock_nu_set_time_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("nuclock_time_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .build().show();
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
