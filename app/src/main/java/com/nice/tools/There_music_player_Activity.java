package com.nice.tools;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.AppBarLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.nice.tools.widget.LrcView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class There_music_player_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private AppBarLayout tool_music_player_mAppBarLayout;
    private Toolbar tool_music_player_toolbar;
    private ImageView tool_music_player_bg;
    private LrcView tool_music_player_lrc_lrcview;
    private static DiscreteSeekBar tool_music_player_time_seekbar;
    private TextView tool_music_player_time_start;
    private TextView tool_music_player_time_end;
    private CardView tool_music_player_play_start;
    private CardView tool_music_player_play_dowon;
    private ImageView tool_music_player_play_start_img;

    private String lrc;
    private String mp3js;
    private String title;
    private String author;
    private String pic;
    private String url;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private Timer timer;//定时器
    private boolean isSeekbarChaning;//互斥变量，防止进度条和定时器冲突。
    private boolean isbf;//判断是否播放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_music_player);
        //获取传来的参数，获取源码在获取json
        Intent intent = getIntent();
        mp3js = intent.getStringExtra("mp3js");

        intview();
        initMediaPlayer();
        intdata();
    }

    private void initMediaPlayer() {
        //初始化一些设置
        try {
            JSONObject json1 = new JSONObject(mp3js);
            Log.d(TAG, mp3js);
            url = json1.getString("url");
            lrc = json1.getString("lrc");
            title = json1.getString("title");
            author = json1.getString("author");
            pic = json1.getString("pic");
            //获取的标题设置上去
            tool_music_player_toolbar.setTitle(title);
            tool_music_player_toolbar.setSubtitle(author);
            //获取图像并高斯模糊
            OkGo.<Bitmap>get(pic)
                    .tag(this)
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Response<Bitmap> response) {
                            tool_music_player_bg.setImageBitmap(mode.scriptBlur(There_music_player_Activity.this, response.body(), 4, 6));
                            //透明度动画
                            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
                            aa.setDuration(500);
                            tool_music_player_bg.setVisibility(View.VISIBLE);
                            tool_music_player_bg.startAnimation(aa);
                        }
                    });

            /*草泥马的时间计算到整数的时候不显示，以后再说*/


            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();


            //准备Prepared完成监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    tool_music_player_time_seekbar.setMax(mediaPlayer.getDuration());
                    tool_music_player_time_seekbar.setProgress(0);
                }
            });
            //播放完成代码
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //tool_music_player_time_seekbar.setProgress(0);
                    tool_music_player_play_start_img.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    //播放状态修改
                    isbf = false;
                }
            });

            //适应下格式
            lrc = "\n\n\n\n\n[00:" + mode.Sj(lrc, "[00:", null);
            //歌词控件
            Log.d(TAG, lrc);
            tool_music_player_lrc_lrcview.setLrc(lrc).setPlayer(mediaPlayer).draw();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void intdata() {
        //开始播放
        mediaPlayer.start();
        //播放状态修改
        isbf = true;

        int duration2 = mediaPlayer.getDuration() / 1000;
        int position = mediaPlayer.getCurrentPosition();
        tool_music_player_time_start.setText(calculateTime(position / 1000));
        tool_music_player_time_end.setText(calculateTime(duration2));

        //绑定监听器，监听拖动到指定位置
        tool_music_player_time_seekbar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int progress, boolean fromUser) {
                int duration2 = mediaPlayer.getDuration() / 1000;//获取音乐总时长
                int position = mediaPlayer.getCurrentPosition();//获取当前播放的位置
                tool_music_player_time_start.setText(getCountTimeByLong(position / 1000));//开始时间
                tool_music_player_time_end.setText(getCountTimeByLong(duration2));//总时长
                //设置气泡时间
                tool_music_player_time_seekbar.setIndicatorFormatter(getCountTimeByLong(progress / 1000));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                isSeekbarChaning = true;
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                isSeekbarChaning = false;
                mediaPlayer.seekTo(seekBar.getProgress());//在当前位置播放
                tool_music_player_time_start.setText(getCountTimeByLong(mediaPlayer.getCurrentPosition() / 1000));
            }
        });

        //监听进度给进度条
        mediaPlayer.start();//开始播放
        int duration = mediaPlayer.getDuration();//获取音乐总时间
        tool_music_player_time_seekbar.setMax(duration);//将音乐总时间设置为Seekbar的最大值
        timer = new Timer();//时间监听器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isSeekbarChaning) {
                    runOnUiThread(updateThread);
                }
            }
        }, 0, 50);

        //点击暂停和开始播放
        tool_music_player_play_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isbf) {
                    //暂停播放
                    mediaPlayer.pause();
                    tool_music_player_play_start_img.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    //播放状态修改
                    isbf = false;
                } else {
                    //继续播放
                    mediaPlayer.start();
                    tool_music_player_play_start_img.setImageDrawable(getDrawable(R.drawable.ic_pause_black_24dp));
                    //播放状态修改
                    isbf = true;
                }
            }
        });

        //下载歌曲
        tool_music_player_play_dowon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(v.getContext(), "开始下载：" + title, Toast.LENGTH_LONG).show();
                final String strpath = Environment.getExternalStorageDirectory().getPath() + "/NTools/Mp3";
                final String filenameheaders = title + "_" + author;
                final String filename = filenameheaders + ".mp3";
                OkGo.<File>get(url)
                        .execute(new FileCallback(strpath, filename) {   //指定下载的路径  下载文件名
                            @Override
                            public void onSuccess(Response<File> response) {
                                String namepd = String.valueOf(response.headers());
                                String wjname = filenameheaders + ".mp3";

                                if (mode.ifbhs(namepd, "wav")) {
                                    wjname = filenameheaders + ".wav";
                                }

                                File oldfile = response.body();
                                String newfile = strpath + "/" + wjname;
                                boolean ifd = oldfile.renameTo(new File(newfile));
                                if (ifd) {
                                    Toast.makeText(v.getContext(), "/NTools/Mp3/"+wjname+" 下载成功", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(Response<File> response) {
                                super.onError(response);
                                Toast.makeText(v.getContext(), "抱歉，下载失败", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    Runnable updateThread = new Runnable() {
        @Override
        public void run() {
            //更新UI
            tool_music_player_time_seekbar.setProgress(mediaPlayer.getCurrentPosition());
        }
    };

    private void intview() {
        tool_music_player_mAppBarLayout = findViewById(R.id.tool_music_player_mAppBarLayout);
        tool_music_player_toolbar = findViewById(R.id.tool_music_player_toolbar);
        tool_music_player_bg = findViewById(R.id.tool_music_player_bg);
        tool_music_player_lrc_lrcview = findViewById(R.id.tool_music_player_lrc_lrcview);
        tool_music_player_time_seekbar = findViewById(R.id.tool_music_player_time_seekbar);
        tool_music_player_time_start = findViewById(R.id.tool_music_player_time_start);
        tool_music_player_time_end = findViewById(R.id.tool_music_player_time_end);
        tool_music_player_play_start = findViewById(R.id.tool_music_player_play_start);
        tool_music_player_play_dowon = findViewById(R.id.tool_music_player_play_dowon);
        tool_music_player_play_start_img = findViewById(R.id.tool_music_player_play_start_img);
        setSupportActionBar(tool_music_player_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
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

    //计算播放时间
    public String calculateTime(int time) {
        int minute;
        int second;
        if (time > 60) {
            minute = time / 60;
            second = time % 60;
            //分钟再0~9
            if (minute >= 0 && minute < 10) {
                //判断秒
                if (second >= 0 && second < 10) {
                    return "0" + minute + ":" + "0" + second;
                } else {
                    return "0" + minute + ":" + second;
                }
            } else {
                //分钟大于10再判断秒
                if (second >= 0 && second < 10) {
                    return minute + ":" + "0" + second;
                } else {
                    return minute + ":" + second;
                }
            }
        } else if (time < 60) {
            second = time;
            if (second >= 0 && second < 10) {
                return "00:" + "0" + second;
            } else {
                return "00:" + second;
            }
        }
        return null;
    }

    public static String getCountTimeByLong(int totalTime) {
        //int totalTime = (int) (finishTime);//秒
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

/*        if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }*/
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            isSeekbarChaning = true;
            mediaPlayer.stop();
        }
    }
}

