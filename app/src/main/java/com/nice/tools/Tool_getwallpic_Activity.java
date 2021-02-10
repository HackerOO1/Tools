package com.nice.tools;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Tool_getwallpic_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private Toolbar getwallpic_toolbar;
    private String filePath, fileName;
    private BitmapDrawable bitmapDrawable;
    private Drawable wallper;
    private ImageView getwallpic_imageview;
    private Bitmap bitmap_image;
    private CardView getwallpic_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_getwallpic);
        intview();
    }

    private void intview() {
        //计数
        mode.jsphp(this,"getwallpic");

        getwallpic_imageview = findViewById(R.id.getwallpic_imageview);
        getwallpic_toolbar = findViewById(R.id.getwallpic_toolbar);
        getwallpic_card= findViewById(R.id.getwallpic_card);
        setSupportActionBar(getwallpic_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        wallper = WallpaperManager.getInstance(this).getDrawable();//获取的壁纸

        bitmapDrawable = (BitmapDrawable) wallper;
        bitmap_image = bitmapDrawable.getBitmap();//壁纸转为Bitmap

        /*int newwidth = mode.getResolution(Tool_getwallpic_Activity.this, "w");
        int newheight = newwidth * bitmap_image.getHeight() / bitmap_image.getWidth();
        bitmap_image = Bitmap.createScaledBitmap(bitmap_image, newwidth - 88, newheight, true);
        wallper = new BitmapDrawable(getResources(), bitmap_image);//获取到的bitmap转为drawable*/

        getwallpic_imageview.setImageBitmap(bitmap_image);

        //透明度动画
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(500);
        getwallpic_card.setVisibility(View.VISIBLE);
        getwallpic_card.startAnimation(aa);

    }

    //保存
    private File saveWallpaper(Context context, Bitmap bm, String path, String fileName) {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            //多级目录不存在就创建
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(path, fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            boolean pan = bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            if (pan) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + myCaptureFile)));
                Toast.makeText(context, "保存到手机成功", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }

    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tool_getwallpaper, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //获取ID，判断id，设置事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //返回键事件
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.tool_getwallpaper_refresh) {
            OkGo.<String>get("http://tbook.top/iso/ntools/bingwallpaper/")
                    .tag(this)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.NO_CACHE)
                    .cacheTime(2000)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> picuri) {
                            OkGo.<Bitmap>get(picuri.body())
                                    .tag(this)
                                    .execute(new BitmapCallback() {
                                        @Override
                                        public void onSuccess(Response<Bitmap> response) {
                                            bitmap_image = response.body();//获取到的bitmap
                                            getwallpic_imageview.setImageBitmap(bitmap_image);
                                            //透明度动画
                                            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
                                            aa.setDuration(500);
                                            getwallpic_card.setVisibility(View.VISIBLE);
                                            getwallpic_card.startAnimation(aa);
                                        }
                                    });
                        }
                    });
        }
        if (id == R.id.tool_getwallpaper_save) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                //发现没有权限，调用requestPermissions方法向用户申请权限，requestPermissions接收三个参数，第一个是context，第二个是一个String数组，我们把要申请的权限
                //名放在数组中即可，第三个是请求码，只要是唯一值就行
            } else {
                filePath = Environment.getExternalStorageDirectory().getPath() + "/NTools/Wallpaper";
                fileName = "Wallpaper_" + mode.getTime() + ".jpg";
                saveWallpaper(this, bitmap_image, filePath, fileName);
            }

        }
        if (id == R.id.tool_getwallpaper_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("说明");

            builder.setMessage("无法截取动态视屏壁纸。\n关于MIUI小米画报的动态壁纸，可以试试以下方法：\n1.画报滚动到你喜欢的壁纸，" +
                    "确保手机桌面是你喜欢的壁纸\n2.进入小米画报设置，在进入画报轮播设置\n" +
                    "3.关闭桌面壁纸轮播开关。\n4.确保这时桌面壁纸还是你中意的壁纸，再次进入本功能截取桌面壁纸，即可保存");

            builder.setPositiveButton("确定", null);

            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
