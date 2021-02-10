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

public class There_getweixinimg_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    private AppBarLayout weixin_index_mAppBarLayout;
    private Toolbar weixin_index_toolbar;
    private TextInputLayout weixin_index_editor;
    private Button weixin_index_getbutton;
    private ImageView weixin_index_image;
    private TextView weixin_index_title, weixin_index_subtitle1, weixin_index_subtitle2;
    private Button weixin_index_savebutton;
    private CardView weixin_index_card;
    private ProgressBar weixin_index_progressBar;
    private String weixin_title, weixin_imguri, weixin_upuesr, weixin_bmption;
    private RelativeLayout weixin_index_editor_layout;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_index);
        //初始化控件
        intview();
        intdata();

    }


    private void lodimage(String shareduri) {
        mode.viewshow(weixin_index_progressBar);
        OkGo.<String>get(shareduri)
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //显示控件
                        weixin_index_card.setVisibility(View.VISIBLE);

                        weixin_title = mode.Sj(mode.Sj(response.body(), "og:title", "og:url"), "content=\"", "\" />");
                        weixin_imguri = mode.Sj(mode.Sj(response.body(), "og:image", "og:description"), "content=\"", "\" />");
                        weixin_upuesr = mode.Sj(mode.Sj(response.body(), "class=\"profile_inner", "profile_avatar"), "profile_nickname\">", "</strong>");
                        weixin_bmption = mode.getTime() + ".jpg";

                        //设置标题等
                        weixin_index_title.setText(weixin_title);
                        weixin_index_subtitle1.setText("公众号：" + weixin_upuesr);
                        weixin_index_subtitle2.setText("图片链接:" + weixin_imguri);
                        //设置图片
                        OkGo.<Bitmap>get(weixin_imguri)
                                .tag(this)
                                .execute(new BitmapCallback() {
                                    @Override
                                    public void onSuccess(Response<Bitmap> response) {
                                        weixin_index_image.setImageBitmap(response.body());
                                        mode.viewshow(weixin_index_card);
                                        mode.viewhide(weixin_index_progressBar);
                                    }
                                });
                    }
                });
    }

    private void intdata() {
        //替换hint
        weixin_index_editor.setHint("请输入文章连接");

        //事件
        weixin_index_getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weixin_word_link = weixin_index_editor.getEditText().getText().toString();
                if (!weixin_word_link.equals("")) {
                    //关闭键盘
                    mode.hideSoftInput((Activity) view.getContext(), weixin_index_editor.getEditText());
                    //取消编辑框错误提示
                    weixin_index_editor.setErrorEnabled(false);
                    String link = weixin_word_link;

                    if (!mode.ifbhs(weixin_word_link, "http")) {
                        link = "https://" + weixin_word_link;
                    }
                    lodimage(link);
                } else {
                    weixin_index_editor.setError("连接不能为空");
                }
            }
        });
        //保存
        weixin_index_savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String strpath = Environment.getExternalStorageDirectory().getPath() + "/NTools/Weixin";
                OkGo.<File>get(weixin_imguri)
                        .execute(new FileCallback(strpath, weixin_bmption) {   //指定下载的路径  下载文件名
                            @Override
                            public void onSuccess(Response<File> response) {
                                File file = new File(strpath + "/" + weixin_bmption);
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

    private void intview() {
        //计数
        mode.jsphp(this,"get_weixin_index");

        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        weixin_index_mAppBarLayout = findViewById(R.id.weixin_index_mAppBarLayout);
        weixin_index_toolbar = findViewById(R.id.weixin_index_toolbar);
        weixin_index_editor = findViewById(R.id.weixin_index_editor);
        weixin_index_getbutton = findViewById(R.id.weixin_index_getbutton);
        weixin_index_image = findViewById(R.id.weixin_index_image);
        weixin_index_title = findViewById(R.id.weixin_index_title);
        weixin_index_subtitle1 = findViewById(R.id.weixin_index_subtitle1);
        weixin_index_subtitle2 = findViewById(R.id.weixin_index_subtitle2);
        weixin_index_savebutton = findViewById(R.id.weixin_index_savebutton);
        weixin_index_card = findViewById(R.id.weixin_index_card);
        weixin_index_progressBar = findViewById(R.id.weixin_index_progressBar);
        weixin_index_editor_layout = findViewById(R.id.weixin_index_editor_layout);

        setSupportActionBar(weixin_index_toolbar);

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
