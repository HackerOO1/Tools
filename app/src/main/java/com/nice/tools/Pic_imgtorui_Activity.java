package com.nice.tools;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Pic_imgtorui_Activity extends AppCompatActivity {
    String TAG = "TAG";
    Toolbar pic_imgtouri_toolbar;
    MaterialButton pic_imgtouri_getbutton;
    CardView pic_imgtouri_card;
    ImageView pic_imgtouri_image;
    TextView pic_imgtouri_subtitle1;
    MaterialButton pic_imgtouri_copybutton;
    ProgressBar pic_imgtouri_progressbar;
    AppBarLayout pic_imgtouri_appbar;
    FloatingActionButton pic_imgtouri_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_imgtorui);

        //申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            //发现没有权限，调用requestPermissions方法向用户申请权限，requestPermissions接收三个参数，第一个是context，第二个是一个String数组，我们把要申请的权限
            //名放在数组中即可，第三个是请求码，只要是唯一值就行
        }
        intview();
        intdata();
    }

    private void intdata() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("可以帮助你把本地图片上传至网络服务器，并获得一个高速、有效、免费的图片链接，违规图片会被定时删除，非违规图片永久有效！我们保证不会泄露你的图片及图片相关的任何隐私数据");
        builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        builder.show();

        //上传事件
        pic_imgtouri_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
        //复制文本
        pic_imgtouri_copybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uritxt = pic_imgtouri_subtitle1.getText().toString();
                mode.copy(view.getContext(), uritxt);
            }
        });
    }

    private void intview() {
        //计数
        mode.jsphp(this,"imgtorui");

        pic_imgtouri_toolbar = findViewById(R.id.pic_imgtouri_toolbar);
        pic_imgtouri_getbutton = findViewById(R.id.pic_imgtouri_getbutton);
        pic_imgtouri_card = findViewById(R.id.pic_imgtouri_card);
        pic_imgtouri_image = findViewById(R.id.pic_imgtouri_image);
        pic_imgtouri_subtitle1 = findViewById(R.id.pic_imgtouri_subtitle1);
        pic_imgtouri_copybutton = findViewById(R.id.pic_imgtouri_copybutton);
        pic_imgtouri_progressbar = findViewById(R.id.pic_imgtouri_progressbar);
        pic_imgtouri_appbar = findViewById(R.id.pic_imgtouri_appbar);
        pic_imgtouri_fab = findViewById(R.id.pic_imgtouri_fab);

        setSupportActionBar(pic_imgtouri_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        //通过APPBAR监听底部fab上下滑动隐藏！
        pic_imgtouri_appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                pic_imgtouri_fab.setTranslationY(-verticalOffset - verticalOffset);
            }
        });
    }

    //相册回调事件
    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data == null) {//相册
                return;
            }
            pic_imgtouri_progressbar.setVisibility(View.VISIBLE);
            Uri uri;
            uri = data.getData();
            final Bitmap bitmap = convertUri(uri);
            String path = mode.getRealPath(this, uri);
            Log.d(TAG, path);
            //post上传文件

            OkGo.<String>post("http://pic.sogou.com/pic/upload_pic.jsp")
                    .isMultipart(true)
                    .params("file", new File(path))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.d(TAG, response.body());
                            //透明度动画
                            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
                            aa.setDuration(500);
                            pic_imgtouri_card.setVisibility(View.VISIBLE);
                            pic_imgtouri_card.startAnimation(aa);
                            pic_imgtouri_progressbar.setVisibility(View.GONE);

                            pic_imgtouri_subtitle1.setText(response.body());
                            pic_imgtouri_image.setImageBitmap(bitmap);
                            Toast.makeText(Pic_imgtorui_Activity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Toast.makeText(Pic_imgtorui_Activity.this, "上传出错", Toast.LENGTH_SHORT).show();
                            pic_imgtouri_subtitle1.setText("Error");
                            pic_imgtouri_card.setVisibility(View.GONE);
                            pic_imgtouri_progressbar.setVisibility(View.GONE);
                        }
                    });


        }
    }

    //uri转bitmap
    private Bitmap convertUri(Uri uri) {
        InputStream is = null;
        try {
            is = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
