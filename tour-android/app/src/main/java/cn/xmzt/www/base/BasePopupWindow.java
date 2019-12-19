package cn.xmzt.www.base;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.xmzt.www.R;

/**
 * @author tanlei
 * @date 2019/8/1
 * @describe
 */

public abstract class BasePopupWindow extends PopupWindow {
    private final int height;
    private final int width;
    protected View view;
    private Activity activity;

    public BasePopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = context;
        view = LayoutInflater.from(context).inflate(setPopupWindowLayoutId(), null);
        setContentView(view);
        //所有的弹出框的弹出动画
        setAnimationStyle(R.style.popup_window_anim_style);
        //设置点击空白处消失
        this.setMyOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        height = context.getWindowManager().getDefaultDisplay().getHeight();
        width = context.getWindowManager().getDefaultDisplay().getWidth();
    }

    protected abstract int setPopupWindowLayoutId();

    /**
     * 设置弹出框的大小，子类可以从写此方法
     *
     * @param h 高度（0-1之间）
     * @param w 宽度（0-1之间）
     */
    protected void setPopupWindowSize(double h, double w) {
        if (h == 0) {
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            this.setHeight((int) (height * h));
        }
        if (w == 0) {
            this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            this.setWidth((int) (width * w));
        }
    }

    /**
     * 子类通过调用此方法设置点击空白处是否可以使popupwindow消失
     *
     * @param isDismiss
     */
    protected void setMyOutsideTouchable(boolean isDismiss) {
        this.setOutsideTouchable(isDismiss);
    }

    public Activity getActivity() {
        return activity;
    }
}
