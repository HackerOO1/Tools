package com.nice.tools;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.Gravity.START;

public class Tool_countdown_Activity extends AppCompatActivity {

    private String TAG = "TAG";
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    private static final int WHAT = 101;
    private Context mContext;
    private TickerView mTimerTv;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private static long MAX_TIME = 12000;
    private long curTime = 0;
    private boolean isPause = false;

    private Toolbar countdown_toolbar;
    private AppBarLayout countdown_mAppBarLayout;
    private Button countdown_enter;
    private DiscreteSeekBar countdown_time;
    private TextView countdown_timetxt;

    private View countdown_view;
    private WindowManager countdown_wn;
    private WindowManager.LayoutParams countdown_wmparams;
    private TickerView floating_countdowm_txt;
    private TextView floating_yiju_txt;
    private LinearLayout floating_countdowm_bg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        //检查悬浮窗权限
        loding_ifops();
        //初始化控件
        intview();
        intdata();
    }

    private void intdata() {
        //时间数据
        countdown_time.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                countdown_timetxt.setText("倒计时时间（" + value + "分钟）");
                editor.putInt("countdown_time_long", value);
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


        //打开悬浮窗
        countdown_enter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (mode.ifops(view.getContext())) {
                    countdown_wn = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    countdown_wmparams = new WindowManager.LayoutParams();
                    // 设置LayoutParams(全局变量）相关参数

                    //Android 8  对悬浮窗 进行了 改变
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        countdown_wmparams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    } else {
                        countdown_wmparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
                    }

                    countdown_wmparams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;

                    countdown_wmparams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;


                    countdown_wmparams.gravity = Gravity.TOP | START;
                    countdown_wmparams.x = 0;
                    countdown_wmparams.y = 0;
                    countdown_wmparams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    countdown_wmparams.height = WindowManager.LayoutParams.MATCH_PARENT;

                    countdown_wmparams.format = PixelFormat.RGBA_8888;
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    countdown_view = inflater.inflate(R.layout.floating_countdown, null);

                    //countdown_view = LayoutInflater.from(view.getContext()).inflate(R.layout.floating_countdown, null);
                    floating_countdowm_txt = countdown_view.findViewById(R.id.floating_countdowm_txt);
                    floating_countdowm_txt.setCharacterLists(TickerUtils.provideNumberList());
                    floating_countdowm_txt.setAnimationInterpolator(new OvershootInterpolator());


                    floating_yiju_txt = countdown_view.findViewById(R.id.floating_yiju_txt);
                    floating_countdowm_bg = countdown_view.findViewById(R.id.floating_countdowm_bg);

                    //悬浮窗显示
                    countdown_wn.addView(countdown_view, countdown_wmparams);

                    BitmapDrawable wallper = (BitmapDrawable) WallpaperManager.getInstance(Tool_countdown_Activity.this).getDrawable();//获取的壁纸
                    Bitmap bmp = wallper.getBitmap();
                    floating_countdowm_bg.setBackground(new BitmapDrawable(getResources(), mode.scriptBlur(Tool_countdown_Activity.this, bmp, 10, 100)));


                    //读取数据
                    int nuclock_time_color = nicetools_prf.getInt("countdown_time_long", 5);
                    MAX_TIME = nuclock_time_color * 1000 * 60;
                    mTimerTv = floating_countdowm_txt;
                    initTimer();
                    mTimer.schedule(mTimerTask, 0, 1000);
                    OkGo.<String>get("http://tbook.top/yiju/")
                            .tag(this)
                            .cacheKey("cacheKey")
                            .cacheMode(CacheMode.NO_CACHE)
                            .cacheTime(2000)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    Log.d(TAG, response.body());
                                    if (response.body() != null) {
                                        floating_yiju_txt.setText(response.body());
                                    }
                                }
                            });


                }
            }
        });
    }


    @SuppressLint({"NewApi", "SetTextI18n"})
    private void intview() {
        //计数
        mode.jsphp(this,"tool_countdown");
        //数据存储
        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        mContext = this;

        countdown_toolbar = findViewById(R.id.countdown_toolbar);
        countdown_mAppBarLayout = findViewById(R.id.countdown_mAppBarLayout);
        countdown_enter = findViewById(R.id.countdown_enter);
        countdown_time = findViewById(R.id.countdown_time);
        countdown_timetxt = findViewById(R.id.countdown_timetxt);
        setSupportActionBar(countdown_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        int countdown_time_pgs = nicetools_prf.getInt("countdown_time_long", 5);
        countdown_time.setProgress(countdown_time_pgs);
        countdown_timetxt.setText("倒计时时间（" + countdown_time_pgs + "分钟）");
    }

    /**
     * 初始化Timer
     */
    public void initTimer() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (curTime == 0) {
                    curTime = MAX_TIME;
                } else {
                    curTime -= 1000;
                }
                Message message = new Message();
                message.what = WHAT;
                message.obj = curTime;
                mHandler.sendMessage(message);
            }
        };
        mTimer = new Timer();
    }

    /**
     * destory上次使用的
     */
    public void destroyTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }


/*    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:

                destroyTimer();
                initTimer();
                isPause = false;
                mTimer.schedule(mTimerTask, 0, 1000);

                break;
            case R.id.btn_cancel:
                //如果 curTime == 0，则不需要执行此操
                if (curTime == 0) {
                    break;
                }
                curTime = 0;
                isPause = false;
                mTimer.cancel();
                break;
            case R.id.btn_pause:
                //如果 curTime == 0，则不需要执行此操
                if (curTime == 0) {
                    break;
                }
                if (!isPause) {
                    isPause = true;
                    mTimer.cancel();
                }
                break;

            case R.id.btn_resume:
                //已经结束或者还没有开始时。或者按了暂停标记。
                if (curTime != 0 && isPause) {
                    destroyTimer();
                    initTimer();
                    mTimer.schedule(mTimerTask, 0, 1000);
                    isPause = false;
                }
                break;
            default:
                break;
        }
    }*/

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    long sRecLen = (long) msg.obj;
                    //毫秒换成00:00:00格式的方式，自己写的。
                    mTimerTv.setText(TimeTools.getCountTimeByLong(sRecLen));
                    if (sRecLen <= 0) {
                        mTimer.cancel();
                        curTime = 0;
                        //倒计时结束事件
                        countdown_wn.removeViewImmediate(countdown_view);
                        Toast.makeText(mContext, "专注模式结束", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyTimer();
        if (mHandler != null) {
            mHandler.removeMessages(WHAT);
            mHandler = null;
        }
    }


    private void loding_ifops() {
        //检测悬浮窗权限
        if (mode.ifops(getApplication()) == false) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("提示")
                    .setMessage("检查到可能没有悬浮窗权限,请前往设置打开本软件悬浮窗权限！")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT < 23 && Build.VERSION.SDK_INT >= 21) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, 0);
                            } else if (Build.VERSION.SDK_INT >= 23) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, 0);
                            } else {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, 0);
                            }

                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }
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


class TimeTools {
    //毫秒换成00:00:00
    public static String getCountTimeByLong(long finishTime) {
        int totalTime = (int) (finishTime / 1000);//秒
        int hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();

        if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (minute < 10) {
            sb.append("0").append(minute).append(":");
        } else {
            sb.append(minute).append(":");
        }
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();

    }

}

