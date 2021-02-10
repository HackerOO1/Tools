package com.nice.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;

public class There_getbiliimg_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    private AppBarLayout bili_index_mAppBarLayout;
    private Toolbar bili_index_toolbar;
    private TextInputLayout bili_index_editor;
    private Button bili_index_getbutton;
    private ImageView bili_index_image;
    private TextView bili_index_title;
    private TextView bili_index_subtitle1;
    private TextView bili_index_subtitle2;
    private Button bili_index_savebutton;
    private CardView bili_index_card;
    private RelativeLayout bili_index_editor_layout;
    private ProgressBar bili_index_editor_progressBar;
    private String bili_code_data, bili_title, bili_imguri, bili_upuesr, bili_av_link, bili_av_num;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bili_index);
        //初始化控件
        intview();
        intdata();
        intshort();
    }

    private void intshort() {
        // 获得 intent, action 和 MIME type
        Intent intent = this.getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    //分享过来的文字
                    String shareduri = "http" + mode.Sj(sharedText, "http", null);
                    if (mode.ifbhs(shareduri, "bilibili")) {
                        bili_index_editor_layout.setVisibility(View.GONE);
                        lodimage(shareduri);
                    } else {
                        Toast.makeText(this, "获取B站封面出错", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void intdata() {
        //替换hint
        bili_index_editor.setHint("请输入BV号");

        //事件
        bili_index_getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String av_number_data = bili_index_editor.getEditText().getText().toString();
                if (!av_number_data.equals("")) {
                    //关闭键盘
                    mode.hideSoftInput((Activity) view.getContext(), bili_index_editor.getEditText());
                    //取消编辑框错误提示
                    bili_index_editor.setErrorEnabled(false);
                    if (av_number_data.substring(0, 2).equals("AV")) {
                        av_number_data = av_number_data.toLowerCase();
                    }
                    String link = "https://www.bilibili.com/video/" + av_number_data;
                    if (mode.ifbhs(av_number_data, "AV") && mode.ifbhs(av_number_data, "av") && mode.ifbhs(av_number_data, "BV")) {
                        link = "https://www.bilibili.com/video/" + av_number_data;
                    }
                    if (mode.ifbhs(av_number_data, "http")) {
                        link = av_number_data;
                    }
                    lodimage(link);
                } else {
                    bili_index_editor.setError("BV号不能为空");
                }
            }
        });
        //保存
        bili_index_savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String strpath = Environment.getExternalStorageDirectory().getPath() + "/NTools/Blbili";
                final String filename = bili_av_num + ".jpg";
                OkGo.<File>get(bili_imguri)
                        .execute(new FileCallback(strpath, filename) {   //指定下载的路径  下载文件名
                            @Override
                            public void onSuccess(Response<File> response) {
                                File file = new File(strpath + "/" + filename);
                                view.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
                                Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Response<File> response) {
                                super.onError(response);
                                Toast.makeText(getApplicationContext(), "抱歉，下载失败", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
    }

    //通过连接显示图片
    private void lodimage(String link) {
        //显示控件
        mode.viewshow(bili_index_editor_progressBar);

        OkGo.<String>get(link)
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        bili_code_data = mode.Sj(response.body(), "<head", "data-vue-meta");
                        bili_title = mode.Sj(bili_code_data, "<title data-vue-meta=\"true\">", "</title>");
                        bili_imguri = mode.Sj(bili_code_data, "itemprop=\"image\" content=\"", "\"><meta data-vue-meta=\"true\" itemprop=\"thumbnailUrl\"");
                        bili_upuesr = mode.Sj(mode.Sj(bili_code_data, "name=\"author\" content=\"", "itemprop=\"name\" name=\"title\""), null, "\"><meta");
                        bili_av_link = mode.Sj(bili_code_data, "itemprop=\"url\" content=\"", "\"><meta data-vue-meta=\"true\" itemprop=\"image\"");
                        bili_av_num = mode.Sj(bili_av_link, "https://www.bilibili.com/video/", "/");

                        //设置标题等
                        bili_index_title.setText(bili_title);
                        bili_index_subtitle1.setText("UP主：" + bili_upuesr);
                        bili_index_subtitle2.setText("图片链接:" + bili_imguri);
                        //设置图片
                        OkGo.<Bitmap>get(bili_imguri)
                                .tag(this)
                                .execute(new BitmapCallback() {
                                    @Override
                                    public void onSuccess(Response<Bitmap> response) {
                                        //隐藏控件
                                        mode.viewhide(bili_index_editor_progressBar);
                                        //显示控件
                                        mode.viewshow(bili_index_card);
                                        bili_index_image.setImageBitmap(response.body());
                                    }
                                });
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.d(TAG, response.body());
                    }
                });
    }

    private void intview() {
        //计数
        mode.jsphp(this, "get_bili_index");

        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        bili_index_mAppBarLayout = findViewById(R.id.bili_index_mAppBarLayout);
        bili_index_toolbar = findViewById(R.id.bili_index_toolbar);
        bili_index_editor = findViewById(R.id.bili_index_editor);
        bili_index_getbutton = findViewById(R.id.bili_index_getbutton);
        bili_index_image = findViewById(R.id.bili_index_image);
        bili_index_title = findViewById(R.id.bili_index_title);
        bili_index_subtitle1 = findViewById(R.id.bili_index_subtitle1);
        bili_index_subtitle2 = findViewById(R.id.bili_index_subtitle2);
        bili_index_savebutton = findViewById(R.id.bili_index_savebutton);
        bili_index_card = findViewById(R.id.bili_index_card);
        bili_index_editor_layout = findViewById(R.id.bili_index_editor_layout);
        bili_index_editor_progressBar = findViewById(R.id.bili_index_editor_progressBar);
        setSupportActionBar(bili_index_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        //申请权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
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
