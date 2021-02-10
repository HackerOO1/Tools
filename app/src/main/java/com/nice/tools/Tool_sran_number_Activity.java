package com.nice.tools;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.nice.tools.widget.WaveView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;


public class Tool_sran_number_Activity extends AppCompatActivity {
    private AppBarLayout tool_sran_mappbar;
    private Toolbar tool_sran_toolbar;
    private TickerView tool_sran_edit_txt;
    private WaveView tool_sran_wave_view;
    private TextInputLayout tool_sran_edit_min, tool_sran_edit_max;
    private MaterialButton tool_sran_enter_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_sran_number);
        intview();
        intdata();
    }

    private void intdata() {
        //生成事件
        tool_sran_enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String min_txt = tool_sran_edit_min.getEditText().getText().toString();
                String max_txt = tool_sran_edit_max.getEditText().getText().toString();
                if (!min_txt.equals("") && !max_txt.equals("")) {
                    tool_sran_edit_txt.setText(String.valueOf(mode.RandomTest(Integer.parseInt(min_txt), Integer.parseInt(max_txt))));
                } else {
                    Snackbar.make(view, "输入框不能为空", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }


    private void intview() {
        //计数
        mode.jsphp(this, "tool_sran");

        tool_sran_mappbar = findViewById(R.id.tool_sran_mappbar);
        tool_sran_toolbar = findViewById(R.id.tool_sran_toolbar);
        tool_sran_edit_txt = findViewById(R.id.tool_sran_edit_txt);
        tool_sran_wave_view = findViewById(R.id.tool_sran_wave_view);
        tool_sran_edit_min = findViewById(R.id.tool_sran_edit_min);
        tool_sran_edit_max = findViewById(R.id.tool_sran_edit_max);
        tool_sran_enter_button = findViewById(R.id.tool_sran_enter_button);

        setSupportActionBar(tool_sran_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        tool_sran_wave_view.setInitialRadius(200);
        tool_sran_wave_view.setDuration(3000);
        tool_sran_wave_view.setStyle(Paint.Style.FILL);
        tool_sran_wave_view.setColor(mode.getColorPrimary(this));
        tool_sran_wave_view.setInterpolator(new LinearOutSlowInInterpolator());
        tool_sran_wave_view.start();

        tool_sran_edit_txt.setCharacterLists(TickerUtils.provideNumberList());
        tool_sran_edit_txt.setAnimationInterpolator(new OvershootInterpolator());
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
