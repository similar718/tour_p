package cn.xmzt.www.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public abstract class KeyBoardUtils {

    public static void closeKeyboard(Activity context) {
        try {
            InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
    }

    /**
     * view为接受软键盘输入的视图，SHOW_FORCED表示强制显示
     * @param view
     */
    public static void showKeyboard(View view) {
        try {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            view.findFocus();
            InputMethodManager mInputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
    }

    /**
     * 强制隐藏键盘
     * @param context
     * @param view
     */
    public static void hideKeyBoard(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
        }catch (Exception e){}

    }
    /**
     * 键盘是否显示
     * @param context
     */
    public static  boolean isSoftShowing(Activity context) {
        try {
            //获取当屏幕内容的高度
            int screenHeight = context.getWindow().getDecorView().getHeight();
            //获取View可见区域的bottom
            Rect rect = new Rect();
            //DecorView即为activity的顶级view
            context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
            //选取screenHeight*2/3进行判断
            return screenHeight * 2 / 3 > rect.bottom;
        } catch (Exception e){}
        return false;
    }
}