package com.nice.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nice.tools.other.LocationUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class mode {
    private volatile static boolean mHasCheckAllScreen;
    private volatile static boolean mIsAllScreenDevice;
    private static String[] hanArr = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static String[] unitArrs = {"", "拾", "佰", "仟"};
    private static String[] unit = {"", "万", "亿", "兆"};
    private static boolean lin = false;

    //    打印
    public static void syso(String err) {
        System.out.println(err);
    }

    /**
     * 存储数据
     */
    public static boolean setDataInfo(Context mContext, String name, String text) {
        //封装
        try {
            //获取私有目录,开启文件写入流:参1:写入到本地的文件名称,参2:文件操作模式:私有,追加,读写等.
            FileOutputStream stream = mContext.openFileOutput(name, Context.MODE_PRIVATE);
            //将文件写入到本地
            stream.write(text.getBytes());
            stream.close();//关闭流
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 追加存储数据
     */
    public static boolean setGameInfo(Context mContext, String name, String text) {
        //封装
        try {
            //获取私有目录,开启文件写入流:参1:写入到本地的文件名称,参2:文件操作模式:私有,追加,读写等.
            FileOutputStream stream = mContext.openFileOutput(name, Context.MODE_APPEND);
            //将文件写入到本地
            stream.write(text.getBytes());
            stream.close();//关闭流
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * * 只能读取文件
     */
    public static String getDataInfo(Context context, String name) {
        try {
            //通过context对象获取私有目录
            FileInputStream stream = context.openFileInput(name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            //读取数据,解析数据
            String line = reader.readLine();

            //关闭流
            stream.close();
            reader.close();
            return line;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * * 读取多行文件
     */
    public static String getDataInfos(Context context, String name) {
        try {
            FileInputStream stream = context.openFileInput(name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            //读取数据,解析数据
            //String line = reader.readLine();
            String line = null;
            String lines = "";
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                lines = lines + "\n" + line;
            }

            //关闭流
            stream.close();
            reader.close();
            return lines.trim();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 删除文件
     */
    public static boolean delDatainfo(Context context, String name) {
        File file = new File(context.getFilesDir() + File.separator + name);
        return file.delete();
    }

    /**
     * 判断文件存在
     */
    public static boolean isDatainfo(Context context, String name) {
        File file = new File(context.getFilesDir() + File.separator + name);
        return file.exists();
    }


    /**
     * 复制准星图片到data做默认准星
     *
     * @return
     */
    public static boolean saveImg(Context context, Bitmap bmp) {
        try {
            OutputStream os = new FileOutputStream(context.getFilesDir() + File.separator + "imgzx.png");
            boolean pd = bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            return pd;
        } catch (Exception e) {
            Log.e("TAG", "", e);
        }
        return false;
    }


    /**
     * 加法
     */
    public static String s1(String one, String two) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);

        BigDecimal s1s = b1.add(b2);
        return s1s.toString();
    }

    /**
     * 减法
     */
    public static String s2(String one, String two) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);

        BigDecimal s1s = b1.subtract(b2);
        return s1s.toString();
    }

    /**
     * 乘法
     */
    public static String s3(String one, String two) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);

        BigDecimal s1s = b1.multiply(b2);
        return s1s.toString();
    }

    /**
     * 除法
     */
    public static String s4(String one, String two) {
        BigDecimal b1 = new BigDecimal(one);
        BigDecimal b2 = new BigDecimal(two);

        BigDecimal s1s = b1.divide(b2);
        return s1s.toString();
    }

    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssetsFile(Context mContext, String fileName) {
        Bitmap image = null;
        AssetManager am = mContext.getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    //dp转pix
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //pix转dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取程序的名字
     */
    public static String getAppName(Context context, String packname) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息
     */
    public static synchronized String getVersionName(Context context, String packname) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息
     */

    public static synchronized int getVersionCode(Context context, String packname) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取应用程序最后更新时间
     *
     * @return
     */

    public static synchronized String getPackTime(Context context, String packname) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            SimpleDateFormat tm = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
            long time = packinfo.lastUpdateTime;
            String uptime = tm.format(new Date(time));
            return uptime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序包名？？？
     */

    public static synchronized String getPackageName(Context context, String packname) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标
     *
     * @return
     */
    public static synchronized Drawable getIcon(Context context, String packname) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable icon = applicationInfo.loadIcon(packageManager);
        return icon;
    }

    /**
     * 判断包含文本
     */
    public static boolean ifbhs(String str1, String str2) {
        if (str1 == null) {
            return false;
        } else {
            return str1.contains(str2);
        }
    }

    /**
     * 替换文本
     */
    public static String Sr(String str1, String str2, String str3) {
        return str1.replace(str2, str3);
        //System.out.println( str.replaceFirst("He", "") );
        //System.out.println( str.replaceAll("He", "Ha") );
    }

    /**
     * 截取文本
     */
    public static String Sj(String str1, String str2, String str3) {

        int int1 = 0;
        int int2 = str1.length();

        if (str2 == null) {
            int1 = 0;
        } else {
            int1 = str1.indexOf(str2) + str2.length();
        }
        if (str3 == null) {
            int2 = str1.length();
        } else {
            int2 = str1.lastIndexOf(str3);
        }
        String retu = str1.substring(int1, int2);
        return retu;
    }

    /**
     * 分割数据为数组
     */
    public static String[] Sl(String str1, String str2) {
        //String delimeter1 = "\\.";  // 指定分割字符， . 号需要转义
        return str1.split(str2); // 分割字符串
    }

    /**
     * 删除字符
     */
    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }


    /**
     * 判断是否全面屏
     */
    public static boolean isAllScreenDevice(Context context) {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }

    /**
     * 判断悬浮窗权限
     */
    public static boolean ifops(Context context) {
        boolean canDraw = false;
        if (Build.VERSION.SDK_INT >= 23) {
            canDraw = Settings.canDrawOverlays(context);
            return canDraw;
        } else if (Build.VERSION.SDK_INT < 19) {
            canDraw = true;
        }
        if (!canDraw && Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if (appOpsMgr == null) {
                return true;
            } else {
                try {
                    Class cls = Class.forName("android.content.Context");
                    Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                    declaredField.setAccessible(true);
                    Object obj = declaredField.get(cls);
                    if (!(obj instanceof String)) {
                        return false;
                    }
                    String str2 = (String) obj;
                    obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                    cls = Class.forName("android.app.AppOpsManager");
                    Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                    declaredField2.setAccessible(true);
                    Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                    int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                    return result == declaredField2.getInt(cls);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return canDraw;
    }

    /**
     * rec适配
     */
    public static int getScreenWidthDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    /****************
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }


    /**
     * 保存文件
     */
    public static void saveBitmap(Context context, Bitmap bitmap) {
        String fileName;
        File file;
        String bitName = "vxqr.png";
        fileName = Environment.getExternalStorageDirectory().getPath() + "/" + bitName;
        file = new File(fileName);
        mode.syso(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));

    }

    /**
     * 获取本地软件版本
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(ctx.getPackageName(), 0);
            //PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * Drawable转为Bitmap
     */
    public static Bitmap DrawableToBitmap(Drawable drawable) {

        // 获取 drawable 长宽
        int width = drawable.getIntrinsicWidth();
        int heigh = drawable.getIntrinsicHeight();

        drawable.setBounds(0, 0, width, heigh);

        // 获取drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 创建bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, heigh, config);
        // 创建bitmap画布
        Canvas canvas = new Canvas(bitmap);
        // 将drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 把流inputstream 转换成一个string
     */
    public static String readStream(InputStream in) throws IOException {
        //定义一个内存输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;

        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            baos.write(buffer, 0, len);

        }
        in.close();
        String content = new String(baos.toByteArray());
        return content;
    }

    //获取CPU温度
    public static String getCpuTemp() {
        String temp = "Unknow";
        BufferedReader br = null;
        FileReader fr = null;
        try {
            File dir = new File("/sys/class/thermal/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            final int SIZE = files.length;
            String line = "";
            String type = "";
            for ( int i = 0; i < SIZE; i++ ) {
                fr = new FileReader("/sys/class/thermal/thermal_zone" + i + "/type");
                br = new BufferedReader(fr);
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                fr = new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp");
                br = new BufferedReader(fr);
                line = br.readLine();
                if (line != null) {
                    // MTK CPU
                    if (type.contains("cpu")) {
                        long temperature = Long.parseLong(line);
                        if (temperature < 0) {
                            temp = "Unknow";
                        } else {
                            temp = (float) (temperature / 1000.0) + "";
                        }
                    } else if (type.contains("tsens_tz_sensor")) {
                        // Qualcomm CPU
                        long temperature = Long.parseLong(line);
                        if (temperature < 0) {
                            temp = "Unknow";
                        } else if (temperature > 100) {
                            temp = (float) (temperature / 10.0) + "";
                        } else {
                            temp = temperature + "";
                        }
                    }

                }
            }

            if (fr != null) {
                fr.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception e) {

        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {

                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {

                }
            }
        }

        return temp;
    }

    /**
     * 外部存储是否可用 (存在且具有读写权限)
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部可用空间大小
     *
     * @return
     */
    static public String getAvailableInternalMemorySize(Context ctx) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(ctx, availableBlocks * blockSize);
    }

    /**
     * 获取手机内部空间大小
     *
     * @return
     */
    public static String getTotalInternalMemorySize(Context ctx) {
        File path = Environment.getDataDirectory();//Gets the Android data directory
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();      //每个block 占字节数
        long totalBlocks = stat.getBlockCount();   //block总数
        return Formatter.formatFileSize(ctx, totalBlocks * blockSize);
    }


    /**
     * 获取手机外部可用空间大小
     *
     * @return
     */
    public static String getAvailableExternalMemorySize(Context ctx) {
        if (isExternalStorageAvailable()) {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return Formatter.formatFileSize(ctx, availableBlocks * blockSize);
        } else {
            return "-1";
        }
    }

    /**
     * 获取手机外部总空间大小
     *
     * @return
     */
    public static String getTotalExternalMemorySize(Context ctx) {
        if (isExternalStorageAvailable()) {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return Formatter.formatFileSize(ctx, totalBlocks * blockSize);
        } else {
            return "-1";
        }
    }


    /**
     * 计算已使用内存的百分比，并返回。
     */
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    /**
     * 获取当前时间的小时。
     */
    public static int getTimeh() {
        SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
        return Integer.parseInt(df.format(new Date()));
    }

    /**
     * 获取当前时间
     */
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return dateFormat.format(date.getTime());
    }

    /**
     * 获得独一无二的Psuedo ID
     */
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    /**
     * 跳转浏览器
     */
    public static void inter(Context context, String link) {
        Uri uri = Uri.parse(link);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 获取主题颜色
     */
    public static int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 隐藏键盘
     */
    public static void hideSoftInput(Activity bas, EditText et) {
        try {
            ((InputMethodManager) bas.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文本
     */
    public static void copy(Context context, String string) {
        ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", string);
        cbm.setPrimaryClip(mClipData);
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 高斯模糊图片
     *
     * @return
     */
    public static Bitmap scriptBlur(Context context, Bitmap bitmap, int radius, int scale) {

        //直接复用传入的bitmap，回收还给用户处理
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale, false);

        long startTime = System.currentTimeMillis();

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur sb = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation inp = Allocation.createFromBitmap(rs, bitmap);
        Allocation otp = Allocation.createTyped(rs, inp.getType());

        sb.setRadius(radius);
        sb.setInput(inp);
        sb.forEach(otp);
        otp.copyTo(bitmap);

        rs.destroy();
        sb.destroy();
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获取分辨率。activiy、取高度或者宽度
     */
    public static int getResolution(Context context, String is) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (is.equals("h")) {
                return (int) height;
            } else {
                return (int) width;
            }

        }
        return 0;
    }

    /**
     * 返回一个随机数
     */
    public static int RandomTest(int START, int END) {
        //创建Random类对象
        Random random = new Random();
        //产生随机数
        int number = random.nextInt(END - START + 1) + START;
        return number;
    }

    /**
     * 通过uri获取path
     */
    public static String getRealPath(Context context, Uri uri) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // content类型普通方式处理
            imagePath = getImagePath(context, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // file类型直接获取图片路径
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    public static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    //使用当前APP的启动图标创建快捷方式
    public static void installShortCut(Context context, String shortcutID, String name, Intent intent) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            intent.setAction(Intent.ACTION_MAIN);
            //新的创建机制，判断唯一性的方法更改为ShortcutID了，默认不允许重复创建ID相同的快捷方式
            ShortcutInfoCompat pinShortcutInfo =
                    new ShortcutInfoCompat.Builder(context, shortcutID)
                            .setShortLabel(name)
                            .setIntent(intent)
                            .build();
            Intent pinnedShortcutCallbackIntent = ShortcutManagerCompat.createShortcutResultIntent(context, pinShortcutInfo);

            PendingIntent successCallback = PendingIntent.getBroadcast(context, 0, pinnedShortcutCallbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            ShortcutManagerCompat.requestPinShortcut(context, pinShortcutInfo, successCallback.getIntentSender());
        }
    }

    public static IconCompat generateIcon(Bitmap bitmap) {
        return IconCompat.createWithBitmap(bitmap);
    }

    //使用自定义图标创建快捷方式
    public static void installShortCut(Context context, String shortcutID, String name, Bitmap bitmap, Intent intent) {
        IconCompat iconCompat = IconCompat.createWithBitmap(bitmap);
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            intent.setAction(Intent.ACTION_VIEW);
            ShortcutInfoCompat pinShortcutInfo =
                    new ShortcutInfoCompat.Builder(context, shortcutID).setShortLabel(name).setIntent(intent).setIcon(iconCompat).build();

            PendingIntent successCallback = PendingIntent.getBroadcast(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            ShortcutManagerCompat.requestPinShortcut(context, pinShortcutInfo,
                    successCallback.getIntentSender());
        }
    }

    /**
     * 控件转图片
     */
    public static Bitmap viewtoimg(View mdzz) {
        Bitmap imgs = Bitmap.createBitmap(mdzz.getWidth(), mdzz.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas imgh = new Canvas(imgs);
        mdzz.draw(imgh);
        return imgs;
    }

    /**
     * 控件转图片2
     */
    public static Bitmap view2toimg(View view) {
        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * Bitmap转Drawable
     */
    public static BitmapDrawable BitmaptoDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * Drawable转Bitmap
     */
    public static Bitmap DrawabletoBitmap(Context context, Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 处理4为基数转换（个，十，百，千）
     *
     * @param number
     * @param top
     * @return
     */
    private static String getStr(String number, boolean top) {
        String result = "";
        lin = false;
// number = Integer.valueOf(number) + "";
        int numLen = number.length();
        for ( int i = 0; i < numLen; i++ ) {
            int num = number.charAt(i) - 48;
            if (i != 0 && num == 0 && num == (number.charAt(i - 1) - 48)) {
                if (i == numLen - 1) {
                    lin = true;
                    if (!"".equals(result)) {
                        result = result.substring(0, result.length() - 1);
                    }
                }
                continue;
            } else if (i == 0 && num == 0 && top) {
                continue;
            }
            if (num != 0 || i != numLen - 1) {
                result += hanArr[num];
            }
            if (num != 0) {
                result += unitArrs[numLen - 1 - i];
            }


        }
        if (Long.valueOf(number) >= 10 && Long.valueOf(number) < 20) {
            result = result.substring(1);
        }
        return result;
    }

    /**
     * 循环取4字基数并加单位（万，亿，兆）
     *
     * @param number
     * @return
     */
    public static String toHanStr(long number) {
        String result = "";
        String numStr = number + "";
        int numLen = numStr.length();
        int num = numLen / 4;
        int n = numLen % 4;
        if (n != 0) {
            result = getStr(numStr.substring(0, n), lin) + unit[num];
            if (lin) {
                result += hanArr[0];
            }
            numStr = numStr.substring(n);
        }
        for ( int i = 0; i < num && Long.valueOf(numStr) != 0; i++ ) {
            result += getStr(numStr.substring(i * 4, (i + 1) * 4), lin);
            if (Long.valueOf(numStr.substring(i * 4, (i + 1) * 4)) != 0) {
                result += unit[num - i - 1];
                if (lin) {
                    result += hanArr[0];
                }
            }
        }
        if (hanArr[0].equals(result.substring(result.length() - 1))) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    //大写数字
    private static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    // 整数部分的单位
    private static final String[] IUNIT = {"圆", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};
    //小数部分的单位
    private static final String[] DUNIT = {"角", "分", "厘"};

    //转成中文的大写金额
    public static String toChinese(String str) {


        if ("0".equals(str) || "0.00".equals(str) || "0.0".equals(str)) {
            return "零圆";
        }

        //判断是否存在负号"-"
        boolean flag = false;
        if (str.startsWith("-")) {
            flag = true;
            str = str.replaceAll("-", "");
        }

        str = str.replaceAll(",", "");//去掉","
        String integerStr;//整数部分数字
        String decimalStr;//小数部分数字


        //初始化：分离整数部分和小数部分
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."));
            decimalStr = str.substring(str.indexOf(".") + 1);
        } else if (str.indexOf(".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        }

        //beyond超出计算能力，直接返回
        if (integerStr.length() > IUNIT.length) {
            System.out.println(str + "：超出计算能力");
            return "超出计算能力";
        }

        int[] integers = toIntArray(integerStr);//整数部分数字
        //判断整数部分是否存在输入012的情况
        if (integers.length > 1 && integers[0] == 0) {
            System.out.println("抱歉，请输入数字！");
            if (flag) {
                str = "-" + str;
            }
            return str;
        }
        boolean isWan = isWan5(integerStr);//设置万单位
        int[] decimals = toIntArray(decimalStr);//小数部分数字
        String result = getChineseInteger(integers, isWan) + getChineseDecimal(decimals);//返回最终的大写金额
        if (flag) {
            return "负" + result;//如果是负数，加上"负"
        } else {
            return result;
        }
    }

    //将字符串转为int数组
    private static int[] toIntArray(String number) {
        int[] array = new int[number.length()];
        for ( int i = 0; i < number.length(); i++ ) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }

    //将整数部分转为大写的金额
    public static String getChineseInteger(int[] integers, boolean isWan) {
        StringBuffer chineseInteger = new StringBuffer("");
        int length = integers.length;
        if (length == 1 && integers[0] == 0) {
            return "";
        }
        for ( int i = 0; i < length; i++ ) {
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13)//万（亿）
                    key = IUNIT[4];
                else if ((length - i) == 9) {//亿
                    key = IUNIT[8];
                } else if ((length - i) == 5 && isWan) {//万
                    key = IUNIT[4];
                } else if ((length - i) == 1) {//元
                    key = IUNIT[0];
                }
                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += NUMBERS[0];
                }
            }
            chineseInteger.append(integers[i] == 0 ? key : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
        }
        return chineseInteger.toString();
    }

    //将小数部分转为大写的金额
    private static String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");
        for ( int i = 0; i < decimals.length; i++ ) {
            if (i == 3) {
                break;
            }
            chineseDecimal.append(decimals[i] == 0 ? "" : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return chineseDecimal.toString();
    }

    //判断当前整数部分是否已经是达到【万】
    private static boolean isWan5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }

    /**
     * UNICODE转中文
     */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for ( int i = 0; i < maxLoop; i++ ) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }


    /**
     * 动画显示控件
     */
    public static void viewshow(final View view) {
        //透明度动画
        AlphaAnimation animationshow = new AlphaAnimation(0.1f, 1.0f);
        animationshow.setDuration(500);
        view.startAnimation(animationshow);
        //动画监听
        animationshow.setAnimationListener(new Animation.AnimationListener() {
            //动画开始的时候触发
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            //动画结束的时候触发
            @Override
            public void onAnimationEnd(Animation animation) {

            }

            //动画重新执行的时候触发
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 动画隐藏控件
     */
    public static void viewhide(final View view) {
        //透明度动画
        AlphaAnimation animationhide = new AlphaAnimation(1.0f, 0.1f);
        animationhide.setDuration(500);
        view.startAnimation(animationhide);
        //动画监听
        animationhide.setAnimationListener(new Animation.AnimationListener() {
            //动画开始的时候触发
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束的时候触发
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            //动画重新执行的时候触发
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 判断手机网络
     */
    public boolean isWebConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 获取经纬度
     */
    @SuppressLint("MissingPermission")
    public static Location getLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {//开启定位权限

            String locationProvider;
            //获取地理位置管理器
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //获取所有可用的位置提供器
            List<String> providers = locationManager.getProviders(true);
            if (providers == null) return null;
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER;
            } else {
                //Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
                Log.i("wxbnb", "getLocation: 没有可用的位置提供器");
                return null;
            }

            //获取Location
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                Log.i("wxbnb", "经纬度：" + location.getLongitude() + "," + location.getLatitude());
                //Toast.makeText(context, location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG).show();
                return location;
            } else {
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };
                locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
                location = locationManager.getLastKnownLocation(locationProvider);
            /*if (location != null) {
                //不为空,显示地理位置经纬度
                Log.i("wxbnb", "2纬度：" + location.getLatitude() + "经度：" + location.getLongitude());
            }*/
                return location;
            }
        }
        return null;
    }

    /**
     * 计数
     */
    public static void jsphp(Context context, String string) {
        boolean isvpn = false;
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            if (nis != null) {
                ArrayList<NetworkInterface> list = Collections.list(nis);
                for ( NetworkInterface ni : list ) {
                    if (ni.isUp() && ni.getInterfaceAddresses().size() != 0) {
                        String name = ni.getName();
                        if (name.equals("tun0") || name.equals("ppp0")) {
                            isvpn = true;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (!isvpn) {
            String link = "http://tbook.top/iso/nmdzzs/jsphp/mdzz/count.php?name=";
            link = Sr(link, "mdzz", "jsphp");
            OkGo.<String>get(link + string)
                    .tag(context)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.NO_CACHE)
                    .cacheTime(2000)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.d("TAG", response.body());
                        }
                    });
        }
    }

    /**
     * 主页计数
     */
    public static void jsphp_index(Context context) {
        OkGo.<String>get("http://tbook.top/iso/ntools/jsphp/")
                .tag(context)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("TAG", response.body());
                    }
                });
    }

    //新的测试GPS
    public static void getLocation_test(final Context context) {
        int loc_pd = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (loc_pd == PackageManager.PERMISSION_GRANTED) {
            LocationUtil.getCurrentLocation(context, new LocationUtil.LocationCallBack() {
                @Override
                public void onSuccess(Location location) {
                    String test = "经度: " + location.getLongitude() + " 纬度: " + location.getLatitude();
                    Log.d("MDZZ", test);
                }

                @Override
                public void onFail(String msg) {
                    //Log.d("MDZZ", msg);
                }
            });
        }
    }
}