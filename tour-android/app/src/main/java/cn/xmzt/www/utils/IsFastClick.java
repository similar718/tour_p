package cn.xmzt.www.utils;

/**
 * 控制点击事件时间间隔
 */
public class IsFastClick {
    public static long lastClickTime = 0;//上次点击的时间
    private static int spaceTime = 500;//时间间隔
    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击
        if (currentTime - lastClickTime > spaceTime) {
            isAllowClick = true;
        } else {
            isAllowClick = false;

        }
        lastClickTime = currentTime;
        return isAllowClick;
    }
    public static boolean isFastClick(int spaceTime) {
        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击
        if (currentTime - lastClickTime > spaceTime) {
            isAllowClick = true;
        } else {
            isAllowClick = false;

        }
        lastClickTime = currentTime;
        return isAllowClick;
    }
}