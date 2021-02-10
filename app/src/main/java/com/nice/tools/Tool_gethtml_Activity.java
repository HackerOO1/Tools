package com.nice.tools;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


public class Tool_gethtml_Activity extends AppCompatActivity {
    private AppBarLayout html_get_mAppBarLayout;
    private Toolbar html_get_toolbar;
    private TextInputLayout html_get_editor;
    private Button html_get_getbutton;
    private TextView html_get_htmltxt;
    private ProgressBar html_get_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        intview();
        intdata();
    }

    private void intdata() {
        //替换hint
        html_get_editor.setHint("请输入连接");

        html_get_getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String html_link = html_get_editor.getEditText().getText().toString();
                if (!html_link.equals("")) {
                    //显示控件
                    mode.viewshow(html_get_progressBar);

                    //关闭键盘
                    mode.hideSoftInput((Activity) view.getContext(), html_get_editor.getEditText());
                    //取消编辑框错误提示
                    html_get_editor.setErrorEnabled(false);
                    String link = html_link;

                    if (!mode.ifbhs(html_link, "http")) {
                        html_get_editor.setError("请输入https://或http://");
                    } else {
                        OkGo.<String>get(link)
                                .tag(this)
                                .cacheKey("cacheKey")
                                .cacheMode(CacheMode.NO_CACHE)
                                .cacheTime(2000)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        html_get_htmltxt.setText(response.body());
                                        html_get_progressBar.setVisibility(View.VISIBLE);

                                        //隐藏控件
                                        mode.viewhide(html_get_progressBar);
                                        //显示控件
                                        mode.viewshow(html_get_htmltxt);
                                    }
                                });
                    }
                } else {
                    html_get_editor.setError("连接不能为空");
                }
            }
        });
    }

    private void intview() {
        //计数
        mode.jsphp(this,"html_get");

        html_get_mAppBarLayout = findViewById(R.id.html_get_mAppBarLayout);
        html_get_toolbar = findViewById(R.id.html_get_toolbar);
        html_get_editor = findViewById(R.id.html_get_editor);
        html_get_getbutton = findViewById(R.id.html_get_getbutton);
        html_get_htmltxt = findViewById(R.id.html_get_htmltxt);
        html_get_progressBar = findViewById(R.id.html_get_progressBar);

        setSupportActionBar(html_get_toolbar);
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
}
