package cn.xmzt.www.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.math.BigDecimal;

import cn.xmzt.www.base.TourApplication;

/**
 * 剪切板管理
 */
public class ClipboardUtils {
    /**
     * 获取剪切板的数据
     * @return
     */
    public static String getClipboardData(){
        try {
            ClipboardManager cm = (ClipboardManager) TourApplication.context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData=cm.getPrimaryClip();
            if(mClipData!=null&&mClipData.getItemCount()>0){
                ClipData.Item mItem=mClipData.getItemAt(0);
                if(mItem!=null){
                    return mItem.getText().toString();
                }
            }
        }catch (Exception e){}
        return "";
    }
    /**
     * 获取剪切板的数据中的邀请码
     * @return
     */
    public static String getClipboardRefCode(){
        //xmzt#85C23H43#xmzt
        String clipboardData=ClipboardUtils.getClipboardData();
        String refCode=null;
        if(!TextUtils.isEmpty(clipboardData)&&clipboardData.startsWith("xmzt#")&&clipboardData.endsWith("#xmzt")){
            int startIndex=clipboardData.indexOf("xmzt#")+5;
            int endIndex=clipboardData.indexOf("#xmzt");
            refCode=clipboardData.substring(startIndex,endIndex);
        }
        return refCode;
    }

}