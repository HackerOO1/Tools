package com.nice.tools;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Daily_oclock_nu_Activity extends AppCompatActivity {

    private String TAG = "TAG";
    private int tion = 0;
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    private TickerView oclock_nutime;
    private TickerView oclock_nudate;
    private RelativeLayout oclock_nuclock_bg;
    private Thread TimeThread;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @SuppressLint({"SourceLockedOrientationActivity", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oclock_nu);
        //旋转横屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        //不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        intview();
        intdata();
    }

    protected void onStart() {
        super.onStart();
        intview();
        //intdata();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (tion == 1) {
            //结束进程
            timer.cancel();
            tion = 0;
        }
    }

    private void intview() {
        //计数
        mode.jsphp(this,"oclock");

        oclock_nutime = findViewById(R.id.oclock_nutime);
        oclock_nudate = findViewById(R.id.oclock_nudate);
        oclock_nuclock_bg = findViewById(R.id.oclock_nuclock_bg);

        oclock_nutime.setCharacterLists(TickerUtils.provideNumberList());
        oclock_nutime.setAnimationInterpolator(new OvershootInterpolator());

        oclock_nudate.setCharacterLists(TickerUtils.provideNumberList());
        oclock_nudate.setAnimationInterpolator(new OvershootInterpolator());

        intcolor();

    }

    private void intcolor() {
        //重新加在颜色
        int nuclock_time_color = nicetools_prf.getInt("nuclock_time_color", -1);
        oclock_nutime.setTextColor(nuclock_time_color);
        int nuclock_date_color = nicetools_prf.getInt("nuclock_date_color", -1);
        oclock_nudate.setTextColor(nuclock_date_color);
        int nuclock_bg_color = nicetools_prf.getInt("nuclock_bg_color", -16777216);
        oclock_nuclock_bg.setBackgroundColor(nuclock_bg_color);
    }


    private void intdata() {
        if (tion == 0) {
            //进入是判断是不是第一次进入，然后启动日期
            timer.schedule(timerTask, 0, 1000);
            tion = 1;
        }

        //日期点击进入设置
        oclock_nudate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), Daily_oclock_nu_set_Activity.class));
            }
        });
    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            @SuppressLint("SimpleDateFormat") final String times = new SimpleDateFormat("HH:mm:ss").format(new Date());
            @SuppressLint("SimpleDateFormat") final String dates = new SimpleDateFormat("yyyy-MM-dd E").format(new Date());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    oclock_nutime.setText(times);
                    oclock_nudate.setText(dates);
                    //晚间亮度mode
                    if (mode.getTimeh() >= 18 || mode.getTimeh() < 6) {
                        Window window = getWindow();
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.screenBrightness = (float) 0.1;
                        window.setAttributes(lp);
                    } else {
                        Window window = getWindow();
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.screenBrightness = (float) 1;
                        window.setAttributes(lp);
                    }
                }
            });
        }
    };


    class TimeThread extends Thread {
        @Override
        public void run() {

            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();//获取系统时间
                    CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime);//时间显示格式
                    CharSequence sysDateStr = DateFormat.format("yyyy-MM-dd E", sysTime);//时间显示格式
                    Log.d(TAG, (String) sysDateStr + sysTimeStr);
                    break;
                default:
                    break;
            }
        }
    };
}
