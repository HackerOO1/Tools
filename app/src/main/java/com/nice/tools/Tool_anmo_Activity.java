package com.nice.tools;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class Tool_anmo_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    Toolbar tool_anmo_toolbar;
    RadioGroup tool_anmo_radiogroup;
    RadioButton tool_anmo_radiobuttom1;
    RadioButton tool_anmo_radiobuttom2;
    RadioButton tool_anmo_radiobuttom3;
    DiscreteSeekBar tool_anmo_timebar;
    DiscreteSeekBar tool_anmo_logtime_timebar;
    DiscreteSeekBar tool_anmo_logtime_timebars;
    FloatingActionButton tool_anmo_start_fab;
    FloatingActionButton tool_anmo_end_fab;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_anmo);
        intview();
        intdata();
    }

    private void intdata() {

        //开始事件
        tool_anmo_start_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tool_anmo_start_fab.hide();
                int buttonId = tool_anmo_radiogroup.getCheckedRadioButtonId();
                if (buttonId == R.id.tool_anmo_radiobuttom1) {
                    long[] patter = {0, 9000};
                    vibrator.vibrate(patter,0);
                }
                if (buttonId == R.id.tool_anmo_radiobuttom2) {
                    vibrator.vibrate(tool_anmo_timebar.getProgress() * 1000);
                }
                if (buttonId == R.id.tool_anmo_radiobuttom3) {
                    long[] patter = {0, tool_anmo_logtime_timebar.getProgress() * 1000,tool_anmo_logtime_timebars.getProgress() * 1000};
                    vibrator.vibrate(patter, 0);
                }
                tool_anmo_end_fab.show();
            }
        });
        //停止事件
        tool_anmo_end_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tool_anmo_start_fab.show();
                vibrator.cancel();
                tool_anmo_end_fab.hide();
            }
        });
        //间隔振动间隔拖动条
        tool_anmo_logtime_timebars.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                editor.putInt("tool_anmu_interval_cut_time", value);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        //间隔振动时间拖动条
        tool_anmo_logtime_timebar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                editor.putInt("tool_anmu_interval_vibration_time", value);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        //长时间振动拖动条
        tool_anmo_timebar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                editor.putInt("tool_anmu_unlimited_time", value);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        //单选事件
        tool_anmo_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.tool_anmo_radiobuttom1:
                        editor.putInt("tool_anmu_radio", 0);
                        editor.commit();
                        break;
                    case R.id.tool_anmo_radiobuttom2:
                        editor.putInt("tool_anmu_radio", 1);
                        editor.commit();
                        break;
                    case R.id.tool_anmo_radiobuttom3:
                        editor.putInt("tool_anmu_radio", 2);
                        editor.commit();
                        break;
                }
            }
        });
    }

    private void intview() {
        //计数
        mode.jsphp(this,"tool_anmo");

        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        tool_anmo_toolbar = findViewById(R.id.tool_anmo_toolbar);
        tool_anmo_radiogroup = findViewById(R.id.tool_anmo_radiogroup);
        tool_anmo_radiobuttom1 = findViewById(R.id.tool_anmo_radiobuttom1);
        tool_anmo_radiobuttom2 = findViewById(R.id.tool_anmo_radiobuttom2);
        tool_anmo_radiobuttom3 = findViewById(R.id.tool_anmo_radiobuttom3);
        tool_anmo_timebar = findViewById(R.id.tool_anmo_timebar);
        tool_anmo_logtime_timebar = findViewById(R.id.tool_anmo_logtime_timebar);
        tool_anmo_logtime_timebars = findViewById(R.id.tool_anmo_logtime_timebars);
        tool_anmo_start_fab = findViewById(R.id.tool_anmo_start_fab);
        tool_anmo_end_fab = findViewById(R.id.tool_anmo_end_fab);
        //振动器初始化
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //初始toolbar
        setSupportActionBar(tool_anmo_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        //振动模式
        int radiobuttom = nicetools_prf.getInt("tool_anmu_radio", 0);
        if (radiobuttom == 0) {
            tool_anmo_radiobuttom1.setChecked(true);
        } else if (radiobuttom == 1) {
            tool_anmo_radiobuttom2.setChecked(true);
        } else if (radiobuttom == 2) {
            tool_anmo_radiobuttom3.setChecked(true);
        }
        int unlimited_time = nicetools_prf.getInt("tool_anmu_unlimited_time", 10);
        tool_anmo_timebar.setProgress(unlimited_time);

        int interval_vibration_time = nicetools_prf.getInt("tool_anmu_interval_vibration_time", 3);
        tool_anmo_logtime_timebar.setProgress(interval_vibration_time);

        int interval_cut_time = nicetools_prf.getInt("tool_anmu_interval_cut_time", 2);
        tool_anmo_logtime_timebars.setProgress(interval_cut_time);

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
