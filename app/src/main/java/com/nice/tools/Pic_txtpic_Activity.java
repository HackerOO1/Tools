package com.nice.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Pic_txtpic_Activity extends AppCompatActivity {
    private String TAG = "TAG";
    private SharedPreferences nicetools_prf;
    private SharedPreferences.Editor editor;
    Toolbar pic_txtpic_toolbar;
    TextInputLayout pic_txtpic_edit;
    MaterialButton pic_txtpic_buttom;
    ImageView pic_txtpic_image;
    CardView pic_txtpic_card;
    Bitmap txtbmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_txtpic);
        intview();
        intdata();
    }

    private void intdata() {
        //默认的文字，存为空的
        String txtpic_text = nicetools_prf.getString("pic_txtpic_text", "");
        pic_txtpic_edit.getEditText().setText(txtpic_text);
        pic_txtpic_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String actxtpic_text = pic_txtpic_edit.getEditText().getText().toString();
                if (!actxtpic_text.equals("")) {
                    txtbmp = textAsBitmap(actxtpic_text, 100);
                    pic_txtpic_edit.setErrorEnabled(false);
                    pic_txtpic_image.setImageBitmap(txtbmp);
                    pic_txtpic_image.setVisibility(View.VISIBLE);
                    //透明度动画
                    AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
                    aa.setDuration(500);
                    pic_txtpic_image.startAnimation(aa);
                } else {
                    pic_txtpic_edit.setError("不能为空");
                }
            }
        });

    }

    private void intview() {
        //计数
        mode.jsphp(this,"pic_txtpic");

        nicetools_prf = this.getSharedPreferences("nicetools_prf", MODE_PRIVATE);
        editor = nicetools_prf.edit();

        pic_txtpic_toolbar = findViewById(R.id.pic_txtpic_toolbar);
        pic_txtpic_edit = findViewById(R.id.pic_txtpic_edit);
        pic_txtpic_buttom = findViewById(R.id.pic_txtpic_buttom);
        pic_txtpic_image = findViewById(R.id.pic_txtpic_image);

        setSupportActionBar(pic_txtpic_toolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    //文字转图片
    public static Bitmap textAsBitmap(String text, float textSize) {

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        //textPaint.setARGB(0x31, 0x31, 0x31, 0);
        StaticLayout layout = new StaticLayout(text, textPaint, 1000,
                Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,
                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        layout.draw(canvas);
        return bitmap;
    }

    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pic_txtpic, menu);
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
        if (id == R.id.pic_txtpic_save) {
            if (txtbmp != null) {
                String filePath = Environment.getExternalStorageDirectory().getPath() + "/NTools/Txtpic";
                String fileName = "Txtpic_" + mode.getTime() + ".png";
                saveWallpaper(this, txtbmp, filePath, fileName);
            }
        }
        return super.onOptionsItemSelected(item);
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
            boolean pan = bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
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
}
