package com.nice.tools;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


public class Tool_led_Activity extends AppCompatActivity {
    private Toolbar led_toolbar;
    private String TAG = "TAG";
    private AppBarLayout led_mAppBarLayout;
    private TextInputLayout led_textinput;
    private RelativeLayout led_txtcolor;
    private RelativeLayout led_bgcolor;
    private CardView led_txtcolor_card;
    private CardView led_bgcolor_card;
    private DiscreteSeekBar led_size;
    private DiscreteSeekBar led_speed;
    private MaterialButton led_enter;
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);

        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();
        /*
       //取数据，没有时默认后面数据
        String coord = nicetools_prf.getString("coord", "0,120");

        //存储数据进去
        SharedPreferences.Editor editor = nicetools_prf.edit();
        editor.putString("coord", "哈哈哈哈哈哈哈");
        editor.commit();
        */

        intview();
        intdata();
    }

    private void intview() {
        //计数
        mode.jsphp(this,"led");

        led_toolbar = findViewById(R.id.led_toolbar);
        led_mAppBarLayout = findViewById(R.id.led_mAppBarLayout);
        led_textinput = findViewById(R.id.led_textinput);
        led_txtcolor = findViewById(R.id.led_txtcolor);
        led_bgcolor = findViewById(R.id.led_bgcolor);
        led_txtcolor_card = findViewById(R.id.led_txtcolor_card);
        led_bgcolor_card = findViewById(R.id.led_bgcolor_card);
        led_size = findViewById(R.id.led_size);
        led_speed = findViewById(R.id.led_speed);
        led_enter = findViewById(R.id.led_enter);
        setSupportActionBar(led_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        String txt_text = nicetools_prf.getString("led_txt_text", "这是演示的文字");
        led_textinput.getEditText().setText(txt_text);
        int txtcolor = nicetools_prf.getInt("led_txt_color", -1);
        led_txtcolor_card.setCardBackgroundColor(txtcolor);
        int bgcolor = nicetools_prf.getInt("led_bg_color", -16777216);
        led_bgcolor_card.setCardBackgroundColor(bgcolor);
        int txtsize = nicetools_prf.getInt("led_txt_size", 100);
        led_size.setProgress(txtsize);
        int txtspeed = nicetools_prf.getInt("led_txt_speed", 10);
        led_speed.setProgress(txtspeed);
    }

    private void intdata() {

        //跳转
        led_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), Tool_led_open_Activity.class));
            }
        });

        //监听编辑框
        led_textinput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editor.putString("led_txt_text", charSequence.toString());
                editor.apply();
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //文字大小
        led_size.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                editor.putInt("led_txt_size", value);
                editor.apply();
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });
        //滚动速度
        led_speed.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                editor.putInt("led_txt_speed", value);
                editor.apply();
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        //文本颜色
        led_txtcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int txtcolor = nicetools_prf.getInt("led_txt_color", -1);
                ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle("文本颜色")//设置标题
                        //传入的默认color
                        .initialColor(txtcolor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)//设置花型
                        .showAlphaSlider(true)
                        //.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)//设置圆形也可以
                        .density(10)//设置距离
                        //监听
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                led_txtcolor_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("led_txt_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .setPositiveButton("确定", new ColorPickerClickListener() {//设置确定事件
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                led_txtcolor_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("led_txt_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .build().show();
            }
        });
        //背景颜色
        led_bgcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bgcolor = nicetools_prf.getInt("led_bg_color", -16777216);
                ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle("背景颜色")//设置标题
                        //传入的默认color
                        .initialColor(bgcolor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)//设置花型
                        .showAlphaSlider(false)
                        //.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)//设置圆形也可以
                        .density(10)//设置距离
                        //监听
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                led_bgcolor_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("led_bg_color", selectedColor);
                                editor.apply();
                                editor.commit();
                            }
                        })
                        .setPositiveButton("确定", new ColorPickerClickListener() {//设置确定事件
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                led_bgcolor_card.setCardBackgroundColor(selectedColor);
                                editor.putInt("led_bg_color", selectedColor);
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
