package com.nice.tools;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

public class Tool_count_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    private Toolbar tool_count_toolbar;
    private TickerView tool_count_numtxt;
    private CardView tool_count_ac;
    private CardView tool_count_less;
    private CardView tool_count_add;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_count);

        //初始化xml数据存储类
        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        intview();
        intdata();
    }

    private void intdata() {
        //清空数据
        tool_count_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tool_count_num = nicetools_prf.getInt("tool_count_num", 0);
                tool_count_numtxt.setText("0");
                editor.putInt("tool_count_num", 0);
                editor.apply();
                editor.commit();
            }
        });
        //加数据
        tool_count_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tool_count_num = nicetools_prf.getInt("tool_count_num", 0);
                tool_count_numtxt.setText(String.valueOf(tool_count_num + 1));
                editor.putInt("tool_count_num", tool_count_num + 1);
                editor.apply();
                editor.commit();
            }
        });
        //减数据
        tool_count_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tool_count_num = nicetools_prf.getInt("tool_count_num", 0);
                int newdata = tool_count_num - 1;
                if (newdata >= 0) {
                    tool_count_numtxt.setText(String.valueOf(newdata));
                    editor.putInt("tool_count_num", tool_count_num - 1);
                    editor.apply();
                    editor.commit();
                }
            }
        });
    }

    private void intview() {
        //计数
        mode.jsphp(this,"tool_count");

        tool_count_toolbar = findViewById(R.id.tool_count_toolbar);
        tool_count_numtxt = findViewById(R.id.tool_count_numtxt);
        tool_count_ac = findViewById(R.id.tool_count_ac);
        tool_count_less = findViewById(R.id.tool_count_less);
        tool_count_add = findViewById(R.id.tool_count_add);

        setSupportActionBar(tool_count_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        tool_count_numtxt.setCharacterLists(TickerUtils.provideNumberList());
        tool_count_numtxt.setAnimationInterpolator(new OvershootInterpolator());

        //默认数据添加上去
        int tool_count_num = nicetools_prf.getInt("tool_count_num", 0);
        tool_count_numtxt.setText(String.valueOf(tool_count_num));

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
