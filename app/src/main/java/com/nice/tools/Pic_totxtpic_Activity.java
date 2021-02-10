package com.nice.tools;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Pic_totxtpic_Activity extends AppCompatActivity {

    public int GALLERY_REQUEST_CODE = 2;
    private String TAG = "TAG";
    private Toolbar pic_txttopic_toolbar;
    private Bitmap old_img;//图库选择的图
    private Bitmap new_img;//转换好的图
    private ImageView pic_txttopic_oldimg, pic_txttopic_newimg;
    private CardView pic_txttopic_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txttopic);

        //申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            //发现没有权限，调用requestPermissions方法向用户申请权限，requestPermissions接收三个参数，第一个是context，第二个是一个String数组，我们把要申请的权限
            //名放在数组中即可，第三个是请求码，只要是唯一值就行
        }

        intview();

    }

    private void intview() {
        //计数
        mode.jsphp(this,"pic_txttopic");

        pic_txttopic_oldimg = findViewById(R.id.pic_txttopic_oldimg);
        pic_txttopic_newimg = findViewById(R.id.pic_txttopic_newimg);
        pic_txttopic_toolbar = findViewById(R.id.pic_txttopic_toolbar);
        pic_txttopic_card = findViewById(R.id.pic_txttopic_card);
        setSupportActionBar(pic_txttopic_toolbar);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
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

            old_img = convertUri(uri);
            new_img = createAsciiPic(this, old_img);

            /*int newwidth = mode.getResolution(this, "w");
            int newheight = newwidth * old_img.getHeight() / old_img.getWidth();

            old_img = Bitmap.createScaledBitmap(old_img, newwidth - 88, newheight, true);
            //new_img = Bitmap.createScaledBitmap(new_img, newwidth - 88, newheight, true);*/

            pic_txttopic_oldimg.setImageBitmap(old_img);
            //pic_txttopic_oldimg.setBackground(new BitmapDrawable(getResources(), old_img));

            pic_txttopic_newimg.setImageBitmap(new_img);
            //pic_txttopic_newimg.setBackground(new BitmapDrawable(getResources(), new_img));

            //透明度动画
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(500);
            pic_txttopic_card.setVisibility(View.VISIBLE);
            pic_txttopic_card.startAnimation(aa);
        }
    }

    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pic_totxtpic, menu);
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
        if (id == R.id.pic_txttopic_gallery) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }
        if (id == R.id.pic_txttopic_save) {
            if (new_img != null) {
                String filePath = Environment.getExternalStorageDirectory().getPath() + "/NTools/Ansipic";
                String fileName = "Ansipic_" + mode.getTime() + ".jpg";
                saveWallpaper(this, new_img, filePath, fileName);
            }
        }
        return super.onOptionsItemSelected(item);
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

    //图片转字符图
    public static Bitmap createAsciiPic(Context context, Bitmap bmp) {
        final String base = "@#&$%*o!;.";
        // 字符串由复杂到简单
        StringBuilder text = new StringBuilder();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //Bitmap image = BitmapFactory.decodeFile(path);  //读取图片
        Bitmap image = bmp;  //读取图片
        int width0 = image.getWidth();
        int height0 = image.getHeight();
        int width1, height1;
        int scale = 7;
        if (width0 <= width / scale) {
            width1 = width0;
            height1 = height0;
        } else {
            width1 = width / scale;
            height1 = width1 * height0 / width0;
        }
        image = scale(bmp, width1, height1);  //读取图片

        //输出到指定文件中
        for (int y = 0; y < image.getHeight(); y += 2) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int pixel = image.getPixel(x, y);
                final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                final int index = Math.round(gray * (base.length() + 1) / 255);
                String s = index >= base.length() ? " " : String.valueOf(base.charAt(index));
                text.append(s);
            }
            text.append("\n");
        }
        return textAsBitmap(context, text);
    }

    //字符图内部调用
    public static Bitmap scale(Bitmap src, int newWidth, int newHeight) {
        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        return ret;
    }

    //字符图内部调用
    public static Bitmap textAsBitmap(Context context, StringBuilder text) {
        TextPaint textPaint = new TextPaint();
        // textPaint.setARGB(0x31, 0x31, 0x31, 0);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(12);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        StaticLayout layout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_CENTER, 1f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20, layout.getHeight() + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        layout.draw(canvas);
        return bitmap;
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
            boolean pan = bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
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
