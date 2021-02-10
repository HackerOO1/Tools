package com.nice.tools;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;

public class Pic_colorpaick_Activity extends AppCompatActivity {
    private ColorPickerView color_picker_view;
    private Toolbar color_picker_toolbar;
    private Button color_picker_copy;
    private TextView color_picker_tiontxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_paick);
        color_picker_toolbar = findViewById(R.id.color_picker_toolbar);
        color_picker_copy = findViewById(R.id.color_picker_copy);
        color_picker_tiontxt = findViewById(R.id.color_picker_tiontxt);
        color_picker_view = findViewById(R.id.color_picker_view);
        setSupportActionBar(color_picker_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        //设置文字
        color_picker_view.addOnColorChangedListener(new OnColorChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onColorChanged(int selectedColor) {
                color_picker_tiontxt.setText("#" + Integer.toHexString(selectedColor));
            }
        });
        //复制
        color_picker_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode.copy(getApplicationContext(), color_picker_tiontxt.getText().toString());
            }
        });

        //计数
        mode.jsphp(this,"color_picker");
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
