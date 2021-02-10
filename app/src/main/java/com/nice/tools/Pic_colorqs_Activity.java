package com.nice.tools;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nice.tools.other.ColorUtils2;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.flag.FlagView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Pic_colorqs_Activity extends AppCompatActivity {
    public int GALLERY_REQUEST_CODE = 2;
    private ColorPickerView color_qs_view;
    private LinearLayout color_qs_bg;
    private Toolbar color_qs_toobar;
    private String colorHex = "未选择颜色";
    private String colorHex2 = "未选择颜色";
    private int colorInt;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_qs);
        intview();
    }

    private void intview() {
        //计数
        mode.jsphp(this,"color_qs");
        //初始化控件
        color_qs_view = findViewById(R.id.color_qs_view);
        color_qs_bg = findViewById(R.id.color_qs_bg);
        color_qs_toobar = findViewById(R.id.color_qs_toolbar);

        setSupportActionBar(color_qs_toobar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        color_qs_view.setFlagView(new CustomFlag(this, R.layout.color_qs_flagview));

        color_qs_view.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                color_qs_bg.setBackgroundColor(envelope.getColor());
                colorHex = "#" + envelope.getHexCode().substring(2);
                colorHex2 = "#" + envelope.getHexCode();
                colorInt = envelope.getColor();
            }
        });

    }

    //检查权限
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            //发现没有权限，调用requestPermissions方法向用户申请权限，requestPermissions接收三个参数，第一个是context，第二个是一个String数组，我们把要申请的权限
            //名放在数组中即可，第三个是请求码，只要是唯一值就行
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }
    }

    //展示图片
    private void displayImage(Bitmap picturePath) {
        if (picturePath != null) {
            Drawable drawable = new BitmapDrawable(picturePath);
            color_qs_view.setPaletteDrawable(drawable);
        } else {
            Toast.makeText(this, "获取图片失败,请换一个图片选择器试试!", Toast.LENGTH_LONG).show();
        }
    }

    //相册回调事件
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data == null) {//相册
                return;
            }
            Uri uri;
            uri = data.getData();
            final Bitmap newimg = convertUri(uri);
            if (newimg != null) {
                displayImage(newimg);
                //bgLayout.setBackground(new BitmapDrawable(getResources(),newimg));
            }
        }
    }

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

    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_color_qs, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.color_qs_open:
                checkPermission();
                break;
            case R.id.color_qs_copy:
                //复制颜色 hex1 hex2 rgb argb 4种
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String[] cities = {colorHex, colorHex2, ColorUtils2.hex2Rgb(colorHex), ColorUtils2.hex2aRgb(colorHex2), "0x" + colorHex2.replace("#", "").toLowerCase()};
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                copytext(colorHex);
                                break;
                            case 1:
                                copytext(colorHex2);
                                break;
                            case 2:
                                copytext(ColorUtils2.hex2Rgb(colorHex));
                                break;
                            case 3:
                                copytext(ColorUtils2.hex2aRgb(colorHex2));
                                break;
                            case 4:
                                copytext("0x" + colorHex2.replace("#", "").toLowerCase());
                                break;
                        }

                    }
                });
                builder.show();
                Objects.requireNonNull(builder.create().getWindow()).setGravity(Gravity.BOTTOM);
                break;
            case android.R.id.home:
                //返回键
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //复制文本
    public void copytext(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mclipData = ClipData.newPlainText("Label", text);
        clipboardManager.setPrimaryClip(mclipData);
        Toast.makeText(this, "已复制 : " + text, Toast.LENGTH_LONG).show();
    }

}


class CustomFlag extends FlagView {
    private TextView textView;

    public CustomFlag(Context context, int layout) {
        super(context, layout);
        textView = findViewById(R.id.flag_color_code);
    }

    @Override
    public void onRefresh(ColorEnvelope colorEnvelope) {
        textView.setText("#" + colorEnvelope.getHexCode());
    }
}
