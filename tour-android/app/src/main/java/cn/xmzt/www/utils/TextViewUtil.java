package cn.xmzt.www.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.TextAppearanceSpan;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import cn.xmzt.www.R;

public class TextViewUtil {

    //动态设置缩进距离的方式
    public static void calculateTag(TextView tag, TextView title, final String text) {
        ViewTreeObserver observer = tag.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                SpannableString spannableString = new SpannableString(text);
                //这里没有获取margin的值，而是直接写死的
                LeadingMarginSpan.Standard what = new LeadingMarginSpan.Standard(tag.getWidth() + DpUtil.dp2px(tag.getContext(), 10), 0);
                spannableString.setSpan(what, 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
                title.setText(spannableString);
                tag.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    /** clc
     * 当前textview从第几个开始后面更换颜色
     * @param content 文字内容
     * @param startNum 从第几个开始
     * @param textView 控件
     */
    public static void setTextViewForeNumColor(String content,int startNum,int endNum,TextView textView){
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan( new ForegroundColorSpan(Color.parseColor("#24A4FF")), startNum,endNum, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    /**
     * 将字符串里面的数字进行加大设置
     * @param textView
     * @param context
     * @param content
     */
    public static void setTextViewSize(TextView textView, Context context,String content,boolean isNight){
        SpannableString styledText = new SpannableString(content);
        for (int i = 0; i < content.length(); i++) {
            char a = content.charAt(i);
            if (a >= '0' && a <= '9') {
                styledText.setSpan(new TextAppearanceSpan(context, isNight ? R.style.style_black : R.style.style_white), i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    /**
     * 将字符串里面的数字进行加大设置
     * @param textView
     * @param context
     * @param content
     */
    public static void setTextViewSignSize(TextView textView, Context context,String content,boolean isLate){
        SpannableString styledText = new SpannableString(content);
        for (int i = 0; i < content.length(); i++) {
            char a = content.charAt(i);
            if (a >= '0' && a <= '9') {
                styledText.setSpan(new TextAppearanceSpan(context, isLate ? R.style.style_late : R.style.style_advance), i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(styledText, TextView.BufferType.SPANNABLE);
    }
}
