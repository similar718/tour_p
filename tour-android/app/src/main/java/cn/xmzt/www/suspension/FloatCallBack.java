package cn.xmzt.www.suspension;

import android.graphics.Bitmap;
import android.view.View;

/**
 * @author tanlei
 * @date 2019/9/4
 * @describe
 */

public interface FloatCallBack {
    /**
     * 展示录音的悬浮按钮
     */
    void showRecordView();

    /**
     * 隐藏录音的悬浮按钮
     */
    void hideRecordView();

    /**
     * 展示导航的按钮
     *
     * @param view
     */
    void showNavigationView(View view);

    /**
     * update navigation window
     * @param bitmap updating bitmap
     * @param icontype updating icontype
     * @param content updating distance
     */
    void updateNavigationView(Bitmap bitmap,int icontype,String content);

    // 是否显示导航最小化控件
    void updateShowandHide(boolean isShow);

    /**
     * 隐藏导航的按钮
     */
    void hideNavigationView();

    /**
     * 展示消息的控件
     *
     * @param view
     */
    void showMessageView(View view);

    /**
     * 隐藏消息的控件
     */
    void hideMessageView();
}
