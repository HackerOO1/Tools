package com.nice.tools;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ypz.bangscreentools.BangScreenTools;

import java.lang.reflect.Method;

public class Tool_scaleView_Activity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaleview);
        //计数
        mode.jsphp(this,"chizi");
        //换横屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //安卓P之后的支持官方扩展到刘海屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            //使用全面屏
            BangScreenTools.getBangScreenTools().blockDisplayCutout(getWindow());
            //小米刘海适配
            int flag = 0x00000100 | 0x00000200;
            try {
                Method method = Window.class.getMethod("addExtraFlags", int.class);
                method.invoke(getWindow(), flag);
            } catch (Exception e) {
                Log.i("TAG", "addExtraFlags not found.");
            }
        }
    }
}
