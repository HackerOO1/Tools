package com.nice.tools;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.nice.tools.widget.MarqueeText;
import com.ypz.bangscreentools.BangScreenTools;

import java.lang.reflect.Method;

public class Tool_led_open_Activity extends AppCompatActivity {

    private String TAG = "TAG";
    private RelativeLayout led_layout;
    private MarqueeText led_txt;
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_open);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        intview();
    }

    private void intview() {
        led_layout = findViewById(R.id.led_layout);
        led_txt = findViewById(R.id.led_txt);

        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        //安卓P之后的支持官方扩展到刘海屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            //使用全面屏
            BangScreenTools.getBangScreenTools().blockDisplayCutout(getWindow());
            //小米刘海适配
            int flag = 0x00000100 | 0x00000200 | 0x00000400;
            try {
                Method method = Window.class.getMethod("addExtraFlags", int.class);
                method.invoke(getWindow(), flag);
            } catch (Exception e) {
                Log.i("TAG", "addExtraFlags not found.");
            }
        }

        String txt_text = nicetools_prf.getString("led_txt_text", "这是演示的文字");
        int txtcolor = nicetools_prf.getInt("led_txt_color", -1);
        int bgcolor = nicetools_prf.getInt("led_bg_color", -16777216);
        int txtsize = nicetools_prf.getInt("led_txt_size", 100);
        int txtspeed = nicetools_prf.getInt("led_txt_speed", 10);
        //背景颜色
        led_layout.setBackgroundColor(bgcolor);

        //文字的一些属性
        led_txt.setMyContext(txt_text);
        led_txt.setTextColor(txtcolor);

        led_txt.setTextSize(txtsize);
        led_txt.setMySpeed(txtspeed);
        led_txt.setL2r(false);
        //获取焦点就开始滚动
        led_txt.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { start(); }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    public void start() {
        led_txt.startScroll();
    }

    public void stop() {
        led_txt.stopScroll();
    }
}
