package com.nice.tools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Intent_Jump_Activity extends AppCompatActivity {

    private String Jump_tion_data;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent__jump);
        //跳转页面的载体,获取传来的参数
        Intent main_intent = getIntent();
        Jump_tion_data = main_intent.getStringExtra("Jump_tion_data");
        Log.d(TAG, Jump_tion_data);


        if (Jump_tion_data.equals("aipay_scon")) {
            //计数
            mode.jsphp(this,"aipay_scon");
            //支付宝扫一扫
            try {
                Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(intent);
            } catch (Exception e) {
                //若无法正常跳转，在此进行错误处理
                Toast.makeText(this, "打开失败，请检查是否安装了支付宝", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        if (Jump_tion_data.equals("aipay_fukuan")) {
            //计数
            mode.jsphp(this,"aipay_fukuan");
            //支付宝付款码
            try {
                Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=20000056");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(intent);
            } catch (Exception e) {
                //若无法正常跳转，在此进行错误处理
                Toast.makeText(this, "打开失败，请检查是否安装了支付宝", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        if (Jump_tion_data.equals("vx_scon")) {
            //计数
            mode.jsphp(this,"vx_scon");
            try {
                //微信扫一扫
                Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                this.startActivity(intent);
            } catch (Exception e) {
                //若无法正常跳转，在此进行错误处理
                Toast.makeText(this, "打开失败，请检查是否安装了微信", Toast.LENGTH_SHORT).show();
            }
            finish();
        }

        if (Jump_tion_data.equals("ai_bus")) {
            //计数
            mode.jsphp(this,"ai_bus");
            try {
                Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=200011235");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(intent);
            } catch (Exception e) {
                //若无法正常跳转，在此进行错误处理
                Toast.makeText(this, "打开失败，请检查是否安装了微信", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
