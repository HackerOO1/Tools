package com.nice.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class Prefs_setting extends PreferenceFragmentCompat {
    public static androidx.preference.Preference pref_setting_updata;
    public static androidx.preference.Preference pref_setting_github;
    public static androidx.preference.Preference pref_setting_privacy;
    public static androidx.preference.Preference pref_setting_vx;
    public String TAG = "TAG";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_setting);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化
        intview();
        intdata();
        intsubtitle();
    }

    private void intview() {
        //计数
        mode.jsphp(getActivity(), "settings");

        //初始化控件
        pref_setting_updata = findPreference("pref_setting_updata");
        pref_setting_github = findPreference("pref_setting_github");
        pref_setting_privacy = findPreference("pref_setting_privacy");
        pref_setting_vx = findPreference("pref_setting_vx");
    }

    private void intdata() {
        //检查更新
        pref_setting_updata.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(androidx.preference.Preference preference) {
                pref_updata(getActivity(), "MT");
                return true;
            }
        });
        //开源相关
        pref_setting_github.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(androidx.preference.Preference preference) {
                getActivity().startActivity(new Intent(getActivity(), Activity_github.class));
                return true;
            }
        });
        //隐私
        pref_setting_privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mode.inter(getActivity(), "http://tbook.top/iso/ntools/privacy/");
                return true;
            }
        });
        //微信公众号
        pref_setting_vx.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mode.inter(getActivity(), "http://tbook.top/zxzs/fov/");
                return true;
            }
        });
    }

    private void intsubtitle() {
        //相关副标题
        //版本
        PackageInfo pi = null;
        try {
            pi = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pref_setting_updata.setSummary("Version:" + pi.versionName);
    }

    //更新的事件代码
    public static void pref_updata(final Context context, final String string) {
        OkGo.<String>get("http://tbook.top/iso/ntools/")
                .tag(context)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("TAG", response.body());
                        if (response.body() != null) {
                            try {
                                JSONObject json1 = new JSONObject(response.body());
                                int ver = Integer.parseInt(json1.optString("ver"));
                                final String link = json1.optString("link");
                                String show = mode.Sr(json1.optString("show"), "</br>", "\n");
                                String size = json1.optString("size");
                                String vername = json1.optString("vername");
                                Boolean swtich = Boolean.valueOf(json1.optString("swtich"));

                                PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
                                int versionCode = pi.versionCode;
                                //Log.d("TAG", String.valueOf(versionCode));
                                if (ver > versionCode) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("发现更新-(" + vername + ")");
                                    builder.setMessage(show);

                                    builder.setPositiveButton("更新(" + size + ")", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mode.inter(context, link);
                                        }
                                    });
                                    AlertDialog builders = builder.create();
                                    builders.setCancelable(swtich);
                                    builders.show();
                                } else if (string.equals("MT")) {
                                    //手动的，没有更新的情况下就给个反馈
                                    Toast.makeText(context, "已是最新版", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException | PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}

