package com.nice.tools;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.IOException;

public class Daily_translation_Activity extends AppCompatActivity {

    private Toolbar translation_toolbar;
    private String TAG = "TAG";
    private TextInputLayout translation_textinput;
    private Spinner translation_spinner;
    private TextView translation_text;
    private ImageView translation_audio;
    private ImageView translation_copy;
    private AppBarLayout translation_mAppBarLayout;
    private Button translation_enterbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        //初始化控件
        intview();
        //初始化数据
        intdata();
    }

    private void intview() {
        //计数
        mode.jsphp(this,"translation");

        translation_toolbar = findViewById(R.id.translation_toolbar);
        translation_textinput = findViewById(R.id.translation_textinput);
        translation_spinner = findViewById(R.id.translation_spinner);
        translation_text = findViewById(R.id.translation_text);
        translation_audio = findViewById(R.id.translation_audio);
        translation_copy = findViewById(R.id.translation_copy);
        translation_mAppBarLayout = findViewById(R.id.translation_mAppBarLayout);
        translation_enterbutton = findViewById(R.id.translation_enterbutton);

        setSupportActionBar(translation_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);


        //下拉选择控件添加数据
        String[] ctype = new String[]{"自动检测", "中文翻译英文", "中文翻译日语", "中文翻译韩语", "中文翻译法语", "中文翻译俄语", "中文翻译西班牙语",
                "英文翻译中文", "日语翻译中文", "韩语翻译中文", "法语翻译中文", "俄语翻译中文", "西班牙语翻译中文"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ctype);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉列表框的下拉选项样式
        translation_spinner.setAdapter(adapter);

    }


    private void intdata() {


        translation_enterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //取消编辑框错误提示
                translation_textinput.setErrorEnabled(false);
                //获取编辑框内容
                String text = translation_textinput.getEditText().getText().toString();
                //Log.d(TAG, text);
                if (!text.equals("")) {
                    //判断类型
                    String type = "AUTO";
                    String xl = translation_spinner.getSelectedItem().toString();
                    if (xl.equals("自动检测")) {
                        type = "AUTO";
                        translation_audio.setVisibility(View.VISIBLE);
                    } else if (xl.equals("中文翻译英语")) {
                        type = "ZH_CN2EN";
                        translation_audio.setVisibility(View.VISIBLE);
                    } else if (xl.equals("中文翻译日语")) {
                        type = "ZH_CN2JA";
                        translation_audio.setVisibility(View.GONE);
                    } else if (xl.equals("中文翻译韩语")) {
                        type = "ZH_CN2KR";
                        translation_audio.setVisibility(View.GONE);
                    } else if (xl.equals("中文翻译法语")) {
                        type = "ZH_CNFR";
                        translation_audio.setVisibility(View.GONE);
                    } else if (xl.equals("中文翻译俄语")) {
                        type = "ZH_CN2RU";
                        translation_audio.setVisibility(View.GONE);
                    } else if (xl.equals("中文翻译西班牙语")) {
                        type = "ZH_CN2SP";
                        translation_audio.setVisibility(View.GONE);
                    } else if (xl.equals("英语翻译中文")) {
                        type = "EN2ZH_CN";
                        translation_audio.setVisibility(View.VISIBLE);
                    } else if (xl.equals("日语翻译中文")) {
                        type = "JA2ZH_CN";
                        translation_audio.setVisibility(View.VISIBLE);
                    } else if (xl.equals("韩语翻译中文")) {
                        type = "KR2ZH_CN";
                        translation_audio.setVisibility(View.VISIBLE);
                    } else if (xl.equals("法语翻译中文")) {
                        type = "FR2ZH_CN";
                        translation_audio.setVisibility(View.VISIBLE);
                    } else if (xl.equals("俄语翻译中文")) {
                        type = "RU2ZH_CN";
                        translation_audio.setVisibility(View.VISIBLE);
                    } else if (xl.equals("西班牙语翻译中文")) {
                        type = "SP2ZH_CN";
                        translation_audio.setVisibility(View.VISIBLE);
                    }
                    String uri = "http://www.tbook.top/iso/ntools/translation/translation.php?inputtext=" + text + "&type=" + type;
                    OkGo.<String>get(uri)
                            .tag(this)
                            .cacheKey("cacheKey")
                            .cacheMode(CacheMode.NO_CACHE)
                            .cacheTime(2000)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    //隐藏键盘
                                    mode.hideSoftInput((Activity) view.getContext(), translation_textinput.getEditText());
                                    translation_text.setText(response.body());
                                }
                            });
                } else {
                    translation_textinput.setError("内容为空");
                }
            }
        });
        //播放
        translation_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //播放音频
                String audio_data = translation_text.getText().toString();
                if (!audio_data.equals("暂时没有翻译内容")) {
                    String link = "http://www.tbook.top/iso/ntools/translation/index.php?text=" + audio_data + "&cuid="+mode.getUniquePsuedoID();
                    OkGo.<String>get(link)
                            .tag(this)
                            .cacheKey("cacheKey")
                            .cacheMode(CacheMode.NO_CACHE)
                            .cacheTime(2000)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    //播放
                                    try {
                                        MediaPlayer mPlayer = new MediaPlayer();
                                        mPlayer.setDataSource(response.body());
                                        mPlayer.prepare();
                                        mPlayer.start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });
        //复制
        translation_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //播放音频
                String copy_data = translation_text.getText().toString();
                mode.copy(view.getContext(), copy_data);
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
