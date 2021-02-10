package com.nice.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.Objects;

public class Main_click {
    /**
     * 关键字跳转页面
     */
    public static void toclass(final Context context, String string) {
        if (string.equals("指南针")) Objects.requireNonNull(context).startActivity(new Intent(context, Daily_compass_Activity.class));
        if (string.equals("时钟")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_oclock_Activity.class));
        if (string.equals("直尺")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_scaleView_Activity.class));
        if (string.equals("快递查询")) Objects.requireNonNull(context).startActivity(new Intent(context, Daily_crydear_Activity.class));
        if (string.equals("垃圾分类查询")) {
            View Bottom_edit_view = View.inflate(context, R.layout.bottom_dialog_edit, null);
            final BottomSheetDialog bottom_edit_dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
            bottom_edit_dialog.setContentView(Bottom_edit_view);

            //底部弹窗展开
            BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) Bottom_edit_view.getParent());
            mDialogBehavior.setPeekHeight(mode.getResolution(context, "h"));

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
                                            Intent intent = new Intent(context, Daily_rubbish_Activity.class);
                                            intent.putExtra("get_rubs_data", response.body());
                                            context.startActivity(intent);
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
        if (string.equals("翻译")) Objects.requireNonNull(context).startActivity(new Intent(context, Daily_translation_Activity.class));
        if (string.equals("LED屏幕")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_led_Activity.class));
        if (string.equals("亲戚计算器")) Objects.requireNonNull(context).startActivity(new Intent(context, Daily_famliy_Activity.class));
        if (string.equals("电子时钟")) Objects.requireNonNull(context).startActivity(new Intent(context, Daily_oclock_nu_Activity.class));
        if (string.equals("分贝仪")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_audio_Activity.class));
        if (string.equals("BiliBili封面获取")) Objects.requireNonNull(context).startActivity(new Intent(context, There_getbiliimg_Activity.class));
        if (string.equals("微信公众号封面获取")) Objects.requireNonNull(context).startActivity(new Intent(context, There_getweixinimg_Activity.class));
        if (string.equals("QQ强制对话")) {
            View Bottom_qqedit_view = View.inflate(context, R.layout.bottom_dialog_edit, null);
            final BottomSheetDialog bottom_qqedit_dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
            bottom_qqedit_dialog.setContentView(Bottom_qqedit_view);

            //底部弹窗展开
            BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) Bottom_qqedit_view.getParent());
            mDialogBehavior.setPeekHeight(mode.getResolution(context, "h"));

            bottom_qqedit_dialog.show();
            //标题
            final TextView bottom_qqedit_title = Bottom_qqedit_view.findViewById(R.id.bottom_edit_title);
            final android.widget.Button bottom_qqedit_enter = Bottom_qqedit_view.findViewById(R.id.bottom_edit_but);
            final TextInputLayout bottom_edit_qqinputlayout = Bottom_qqedit_view.findViewById(R.id.bottom_edit_edit);
            //style
            bottom_qqedit_title.setText("QQ强制对话");
            bottom_qqedit_enter.setText("对话");
            //编辑框行数
            bottom_edit_qqinputlayout.getEditText().setMaxEms(1);
            //编辑框类型
            bottom_edit_qqinputlayout.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            //编辑框长度
            bottom_edit_qqinputlayout.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
            //编辑框hint
            bottom_edit_qqinputlayout.setHint("QQ号码");

            //关闭弹窗
            Bottom_qqedit_view.findViewById(R.id.bottom_edit_close_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottom_qqedit_dialog.dismiss();
                }
            });
            //debug
            // bottom_edit_inputlayout.getEditText().setText("773025818976244");
            //按钮点击事件
            bottom_qqedit_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    String qq_number = bottom_edit_qqinputlayout.getEditText().getText().toString();
                    if (!qq_number.equals("")) {
                        bottom_edit_qqinputlayout.setErrorEnabled(false);
                        try {
                            Uri uri = Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qq_number + "&version=1");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            view.getContext().startActivity(intent);
                        } catch (Exception e) {
                            //若无法正常跳转，在此进行错误处理
                            Toast.makeText(view.getContext(), "打开失败，请检查是否安装了QQ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        bottom_edit_qqinputlayout.setError("QQ号码不能为空");
                    }
                }
            });
        }
        if (string.equals("专注模式")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_countdown_Activity.class));
        if (string.equals("获取网页源码")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_gethtml_Activity.class));
        if (string.equals("图片取色")) Objects.requireNonNull(context).startActivity(new Intent(context, Pic_colorqs_Activity.class));
        if (string.equals("调色板")) Objects.requireNonNull(context).startActivity(new Intent(context, Pic_colorpaick_Activity.class));
        if (string.equals("字符图")) Objects.requireNonNull(context).startActivity(new Intent(context, Pic_totxtpic_Activity.class));
        if (string.equals("文字转图片")) Objects.requireNonNull(context).startActivity(new Intent(context, Pic_txtpic_Activity.class));
        if (string.equals("带壳截图")) Objects.requireNonNull(context).startActivity(new Intent(context, Pic_daike_src_Activity.class));
        if (string.equals("二维码生成")) Objects.requireNonNull(context).startActivity(new Intent(context, Pic_qrcode_Activity.class));
        if (string.equals("图片转连接")) Objects.requireNonNull(context).startActivity(new Intent(context, Pic_imgtorui_Activity.class));
        if (string.equals("随机数生成")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_sran_number_Activity.class));
        if (string.equals("抓取壁纸")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_getwallpic_Activity.class));
        if (string.equals("按摩器")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_anmo_Activity.class));
        if (string.equals("历史上的今天")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_pastoday_Activity.class));
        if (string.equals("计数器")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_count_Activity.class));
        if (string.equals("数字转大写")) Objects.requireNonNull(context).startActivity(new Intent(context, Tool_tochinanum_Activity.class));
        if (string.equals("音乐解析")) Objects.requireNonNull(context).startActivity(new Intent(context, There_music_Activity.class));
    }
}
