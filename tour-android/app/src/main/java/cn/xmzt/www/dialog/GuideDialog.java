package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import cn.xmzt.www.R;

import androidx.annotation.NonNull;

/**
 * @author tanlei
 * @date 2019/9/28
 * @describe 引导页弹出框
 */
public class GuideDialog extends Dialog {
    public GuideDialog(@NonNull Context context) {
        this(context,0);
    }

    public GuideDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.dialog_custom1);
        Window window = this.getWindow();
        // 设置dialog 显示的位置 默认center 显示再屏幕中间  现设置显示再顶部
        window.setGravity(Gravity.TOP);
        // 设置dialog 尺寸 和偏移
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 前2 个flag设置dialog 显示到状态栏    第三个设置点击dialog以外的蒙层 不抢夺焦点  响应点击事件
        lp.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.5f;
        window.setAttributes(lp);
    }
}
