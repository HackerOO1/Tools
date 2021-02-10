package com.nice.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nice.tools.other.LocationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Main_view_pager1 extends Fragment {
    private View mian1view;
    private String TAG = "TAG";
    private RecyclerView main_vp1_recyler;
    private TextView main_vp1_subtitle;
    private String[] main_vp1_recycler_data = null;
    private ImageView main_vp1_wallpic;
    private LinearLayout main_vp1_wallpic_layout;
    private List<Main_view_pager1.Fruit> fruitList = new ArrayList<>();
    private Button main_vp1_horizontal_button1;
    private Button main_vp1_horizontal_button2;
    private Button main_vp1_horizontal_button3;
    private Button main_vp1_horizontal_button4;
    private Button main_vp1_horizontal_button5;

    private CardView main_vp1_weat_cardlayout;
    private TextView main_vp1_weat_dizhi;
    private ImageView main_vp1_weat_shauxin;
    private TextView main_vp1_weat_dusu;
    private TextView main_vp1_weat_tianqi;
    private TextView main_vp1_weat_fengxiang;
    private TextView main_vp1_weat_fengsu;
    private TextView main_vp1_weat_shidu;
    private TextView main_vp1_weat_upshijian;
    private LinearLayout main_vp1_weat_layout;
    private TextView main_vp1_weat_air;
    private TextView main_vp1_weat_rain;
    private TextView main_vp1_weat_feelst;
    private TextView main_vp1_weat_airpressure;
    private String weather_link;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mian1view = View.inflate(inflater.getContext(), R.layout.activity_main_vp1, null);
        intview();
        intdata();
        intonclick();
        return mian1view;
    }


    private void intonclick() {
        //天气刷新
        main_vp1_weat_shauxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.activity_image_xuanzhuan);
                main_vp1_weat_shauxin.startAnimation(operatingAnim);
                intweather();
            }
        });

        //常用功能点击事件
        main_vp1_horizontal_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), Daily_crydear_Activity.class));
            }
        });
        main_vp1_horizontal_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rubbish_onclock(view.getContext());
            }

            private void rubbish_onclock(Context context) {
                View Bottom_edit_view = View.inflate(context, R.layout.bottom_dialog_edit, null);
                final BottomSheetDialog bottom_edit_dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
                bottom_edit_dialog.setContentView(Bottom_edit_view);

                //底部弹窗展开
                BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) Bottom_edit_view.getParent());
                mDialogBehavior.setPeekHeight(mode.getResolution(getActivity(), "h"));

                bottom_edit_dialog.show();
                //标题
                final TextView bottom_edit_title = Bottom_edit_view.findViewById(R.id.bottom_edit_title);
                final android.widget.Button bottom_edit_enter = Bottom_edit_view.findViewById(R.id.bottom_edit_but);
                final TextInputLayout bottom_edit_inputlayout = Bottom_edit_view.findViewById(R.id.bottom_edit_edit);
                //style
                bottom_edit_title.setText("垃圾分类查询");
                bottom_edit_enter.setText("查询");
                //编辑框行数
                bottom_edit_inputlayout.getEditText().setMaxEms(1);
                //编辑框长度
                bottom_edit_inputlayout.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                //编辑框hint
                bottom_edit_inputlayout.setHint("你是什么垃圾");

                //关闭弹窗
                Bottom_edit_view.findViewById(R.id.bottom_edit_close_icon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottom_edit_dialog.dismiss();
                    }
                });
                //DEBUG
                // bottom_edit_inputlayout.getEditText().setText("鸡蛋");
                //按钮点击事件
                bottom_edit_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final String edit_data = bottom_edit_inputlayout.getEditText().getText().toString();
                        if (!edit_data.equals("")) {
                            String link = "http://trash.lhsr.cn/sites/feiguan/trashTypes_3/Handler/Handler.ashx?a=GET_KEYWORDS&kw=" + edit_data;
                            OkGo.<String>get(link)
                                    .tag(this)
                                    .cacheKey("cacheKey")
                                    .cacheMode(CacheMode.NO_CACHE)
                                    .cacheTime(2000)//缓存时间
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {

                                            if (response.body() != null) {
                                                //判断返回数据是不是空数据
                                                Intent intent = new Intent(view.getContext(), Daily_rubbish_Activity.class);
                                                intent.putExtra("get_rubs_data", response.body());
                                                startActivity(intent);
                                                bottom_edit_dialog.dismiss();
                                            } else {
                                                //数据不对的时候
                                                bottom_edit_inputlayout.setError("未知垃圾，请换个关键词重试");
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        });
        main_vp1_horizontal_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), Daily_translation_Activity.class));
            }
        });
        main_vp1_horizontal_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), Tool_led_Activity.class));
            }
        });
        main_vp1_horizontal_button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), Daily_oclock_nu_Activity.class));
            }
        });
    }


    private void intview() {
        main_vp1_recyler = mian1view.findViewById(R.id.main_vp1_recyler);
        main_vp1_subtitle = mian1view.findViewById(R.id.main_vp1_subtitle);
        main_vp1_wallpic = mian1view.findViewById(R.id.main_vp1_wallpic);
        main_vp1_wallpic_layout = mian1view.findViewById(R.id.main_vp1_wallpic_layout);
        main_vp1_horizontal_button1 = mian1view.findViewById(R.id.main_vp1_horizontal_button1);
        main_vp1_horizontal_button2 = mian1view.findViewById(R.id.main_vp1_horizontal_button2);
        main_vp1_horizontal_button3 = mian1view.findViewById(R.id.main_vp1_horizontal_button3);
        main_vp1_horizontal_button4 = mian1view.findViewById(R.id.main_vp1_horizontal_button4);
        main_vp1_horizontal_button5 = mian1view.findViewById(R.id.main_vp1_horizontal_button5);

        //天气相关控件初始化
        main_vp1_weat_cardlayout = mian1view.findViewById(R.id.main_vp1_weat_cardlayout);
        main_vp1_weat_dizhi = mian1view.findViewById(R.id.main_vp1_weat_dizhi);
        main_vp1_weat_shauxin = mian1view.findViewById(R.id.main_vp1_weat_shauxin);
        main_vp1_weat_dusu = mian1view.findViewById(R.id.main_vp1_weat_dusu);
        main_vp1_weat_tianqi = mian1view.findViewById(R.id.main_vp1_weat_tianqi);
        main_vp1_weat_fengxiang = mian1view.findViewById(R.id.main_vp1_weat_fengxiang);
        main_vp1_weat_fengsu = mian1view.findViewById(R.id.main_vp1_weat_fengsu);
        main_vp1_weat_shidu = mian1view.findViewById(R.id.main_vp1_weat_shidu);
        main_vp1_weat_upshijian = mian1view.findViewById(R.id.main_vp1_weat_upshijian);
        main_vp1_weat_layout = mian1view.findViewById(R.id.main_vp1_weat_layout);
        main_vp1_weat_air = mian1view.findViewById(R.id.main_vp1_weat_air);
        main_vp1_weat_rain = mian1view.findViewById(R.id.main_vp1_weat_rain);
        main_vp1_weat_feelst = mian1view.findViewById(R.id.main_vp1_weat_feelst);
        main_vp1_weat_airpressure = mian1view.findViewById(R.id.main_vp1_weat_airpressure);
    }


    private void intdata() {
        //主页天气
        intweather();

        //主页一句话
        OkGo.<String>get("https://v1.hitokoto.cn/")
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(TAG, response.body());
                        if (response.body() != null) {
                            try {
                                JSONObject json1 = new JSONObject(response.body());
                                String hitokoto = json1.optString("hitokoto");
                                main_vp1_subtitle.setText(hitokoto);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        main_vp1_recycler_data = new String[]{"aipay_scon", "aipay_fukuan", "ai_bus", "vx_scon"};
        for ( String card_api : main_vp1_recycler_data ) {
            Drawable card_bg = getActivity().getDrawable(R.drawable.main_vp1_vx_bg);
            Drawable card_icon = getActivity().getDrawable(R.drawable.ic_scon);
            String card_titel = "微信扫一扫";
            if (card_api.equals("aipay_scon")) {
                card_bg = getActivity().getDrawable(R.drawable.main_vp1_aipay_bg);
                card_icon = getActivity().getDrawable(R.drawable.ic_scon);
                card_titel = "支付宝扫一扫";
            }
            if (card_api.equals("aipay_fukuan")) {
                card_bg = getActivity().getDrawable(R.drawable.main_vp1_aipay_bg);
                card_icon = getActivity().getDrawable(R.drawable.ic_aipay_fukuan);
                card_titel = "支付宝付款码";
            }
            if (card_api.equals("ai_bus")) {
                card_bg = getActivity().getDrawable(R.drawable.main_vp1_aipay_bg);
                card_icon = getActivity().getDrawable(R.drawable.ic_aipay_bus);
                card_titel = "支付宝乘车码";
            }
            if (card_api.equals("vx_scon")) {
                card_bg = getActivity().getDrawable(R.drawable.main_vp1_vx_bg);
                card_icon = getActivity().getDrawable(R.drawable.ic_scon);
                card_titel = "微信扫一扫";
            }
            Main_view_pager1.Fruit data = new Main_view_pager1.Fruit(card_api, card_bg, card_icon, card_titel);
            fruitList.add(data);
        }

        //网格布局
        main_vp1_recyler.setLayoutManager(new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false));
        Main_view_pager1.FruitAdapter adapter = new Main_view_pager1.FruitAdapter(fruitList);
        main_vp1_recyler.setAdapter(adapter);

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
                                        main_vp1_wallpic.setImageBitmap(response.body());
                                        //控件显示
                                        mode.viewshow(main_vp1_wallpic_layout);
                                    }
                                });
                    }
                });

        //壁纸点击
        main_vp1_wallpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //别忘了换下壁纸展示方式
                getActivity().startActivity(new Intent(getActivity(), Tool_getwallpic_Activity.class));
            }
        });
    }


    //更新天气
    private void intweather() {
        weather_link = "http://www.tbook.top/iso/ntools/weather/cmcdata.php";
        final Location location = mode.getLocation(getActivity());
        if (location != null) {
            weather_link = "http://www.tbook.top/iso/ntools/weather/new/index.php?x=" + location.getLatitude() + "&y=" + location.getLongitude();
        } else {
            weather_link = "http://www.tbook.top/iso/ntools/weather/cmcdata.php";
        }
        //weather_link = "http://www.tbook.top/iso/ntools/weather/cmcdata.php";
        //Toast.makeText(getActivity(), weather_link, Toast.LENGTH_LONG).show();
        OkGo.<String>get(weather_link)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(TAG, response.body());
                        if (response.body() != null) {
                            try {
                                JSONObject json1 = new JSONObject(response.body());
                                JSONObject json2 = json1.getJSONObject("data");
                                //空气质量
                                String air = json2.getJSONObject("air").getString("text");

                                JSONObject loc_jsonobj = json2.getJSONObject("predict").getJSONObject("station");
                                //ip定位
                                final String province = loc_jsonobj.getString("province");
                                final String city = loc_jsonobj.getString("city");

                                JSONObject weather_jsonobj = json2.getJSONObject("real").getJSONObject("weather");
                                //天气文字描述
                                String weather = weather_jsonobj.optString("info");
                                //温度
                                String temperature = weather_jsonobj.optString("temperature");
                                //大气压
                                String airpressure = weather_jsonobj.optString("airpressure");
                                //体感温度
                                String feelst = weather_jsonobj.optString("feelst");
                                //降水量
                                String rain = weather_jsonobj.optString("rain");
                                //湿度
                                String humidity = weather_jsonobj.optString("humidity");

                                JSONObject wind_jsonobj = json2.getJSONObject("real").getJSONObject("wind");
                                //风向
                                String winddirection = wind_jsonobj.getString("direct");
                                //风速
                                String windpower = wind_jsonobj.getString("power");

                                String reporttime = json2.getJSONObject("real").getString("publish_time");


                                //天气数据添加上去
                                main_vp1_weat_dusu.setText(temperature + "°");
                                main_vp1_weat_tianqi.setText(weather);
                                main_vp1_weat_fengxiang.setText(winddirection);
                                main_vp1_weat_fengsu.setText(windpower);
                                main_vp1_weat_shidu.setText("湿度：" + humidity + "%");
                                main_vp1_weat_upshijian.setText(reporttime + "更新");
                                main_vp1_weat_air.setText("空气质量：" + air);
                                main_vp1_weat_rain.setText("降水量：" + rain + "mm");
                                main_vp1_weat_feelst.setText("体感温度：" + feelst + "°");
                                main_vp1_weat_airpressure.setText("大气压：" + airpressure + "hPa");

                                int loc_pd = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
                                if (loc_pd == PackageManager.PERMISSION_GRANTED) {
                                    LocationUtil.getCurrentLocation(getActivity(), new LocationUtil.LocationCallBack() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                OkGo.<String>get("http://www.tbook.top/iso/ntools/weather/new/locode.php?x=" + location.getLatitude() + "&y=" + location.getLongitude())
                                                        .cacheKey("cacheKey")
                                                        .cacheMode(CacheMode.NO_CACHE)
                                                        .cacheTime(2000)
                                                        .execute(new StringCallback() {
                                                            @Override
                                                            public void onSuccess(Response<String> response) {
                                                                Log.d(TAG, response.body());
                                                                if (response.body() != null) {
                                                                    main_vp1_weat_dizhi.setText(response.body());
                                                                }
                                                            }
                                                        });

                                            } else {
                                                main_vp1_weat_dizhi.setText(province + city);
                                            }
                                        }

                                        @Override
                                        public void onFail(String msg) {
                                            Log.d(TAG, msg);
                                        }
                                    });
                                }else {
                                    main_vp1_weat_dizhi.setText(province + city);
                                }

                                mode.viewshow(main_vp1_weat_layout);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "天气数据更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }

    private class Fruit {
        private final String card_api;
        private final Drawable card_bg;
        private final Drawable card_icon;
        private final String card_titel;

        public Fruit(String card_api, Drawable card_bg, Drawable card_icon, String card_titel) {
            this.card_api = card_api;
            this.card_bg = card_bg;
            this.card_icon = card_icon;
            this.card_titel = card_titel;
        }

        public Drawable getCard_bg() {
            return card_bg;
        }

        public Drawable getCard_icon() {
            return card_icon;
        }

        public String getCard_api() {
            return card_api;
        }

        public String getCard_titel() {
            return card_titel;
        }
    }

    public static class FruitAdapter extends RecyclerView.Adapter<Main_view_pager1.FruitAdapter.ViewHolder> {
        private List<Main_view_pager1.Fruit> mFruitList;

        static class ViewHolder extends RecyclerView.ViewHolder {
            CardView item_main_vp1_card;
            LinearLayout item_main_vp1_linear;
            ImageView item_main_vp1_img;
            TextView item_main_vp1_text;

            public ViewHolder(View view) {
                super(view);
                item_main_vp1_card = view.findViewById(R.id.item_main_vp1_card);
                item_main_vp1_linear = view.findViewById(R.id.item_main_vp1_linear);
                item_main_vp1_img = view.findViewById(R.id.item_main_vp1_img);
                item_main_vp1_text = view.findViewById(R.id.item_main_vp1_text);
            }
        }

        public FruitAdapter(List<Main_view_pager1.Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override
        public Main_view_pager1.FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_vp1_recycler, parent, false);
            Main_view_pager1.FruitAdapter.ViewHolder holder = new Main_view_pager1.FruitAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Main_view_pager1.FruitAdapter.ViewHolder holder, int position) {

            final Main_view_pager1.Fruit fruit = mFruitList.get(position);

            //动画匹配
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(400);
            holder.item_main_vp1_card.startAnimation(aa);

            //数据传输进去
            holder.item_main_vp1_text.setText(fruit.getCard_titel());
            holder.item_main_vp1_img.setImageDrawable(fruit.getCard_icon());
            holder.item_main_vp1_linear.setBackground(fruit.getCard_bg());

            holder.item_main_vp1_card.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(final View views) {
                    //Toast.makeText(views.getContext(), fruit.getCard_api(), Toast.LENGTH_SHORT).show();
                    if (fruit.getCard_api().equals("aipay_scon")) {
                        //支付宝扫一扫
                        try {
                            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            views.getContext().startActivity(intent);
                        } catch (Exception e) {
                            //若无法正常跳转，在此进行错误处理
                            Toast.makeText(views.getContext(), "打开失败，请检查是否安装了支付宝", Toast.LENGTH_SHORT).show();
                        }

                    }
                    if (fruit.getCard_api().equals("aipay_fukuan")) {
                        //支付宝付款码
                        try {
                            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=20000056");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            views.getContext().startActivity(intent);
                        } catch (Exception e) {
                            //若无法正常跳转，在此进行错误处理
                            Toast.makeText(views.getContext(), "打开失败，请检查是否安装了支付宝", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (fruit.getCard_api().equals("vx_scon")) {
                        try {
                            //微信扫一扫
                            Intent intent = views.getContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                            views.getContext().startActivity(intent);
                        } catch (Exception e) {
                            //若无法正常跳转，在此进行错误处理
                            Toast.makeText(views.getContext(), "打开失败，请检查是否安装了微信", Toast.LENGTH_SHORT).show();
                        }

                    }

                    if (fruit.getCard_api().equals("ai_bus")) {
                        try {
                            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=200011235");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            views.getContext().startActivity(intent);
                        } catch (Exception e) {
                            //若无法正常跳转，在此进行错误处理
                            Toast.makeText(views.getContext(), "打开失败，请检查是否安装了微信", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }
}