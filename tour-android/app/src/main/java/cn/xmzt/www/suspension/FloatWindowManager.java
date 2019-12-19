package cn.xmzt.www.suspension;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.navi.view.NextTurnTipView;
import com.blankj.utilcode.util.ActivityUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.utils.ToastUtils;

import java.lang.reflect.Field;

/**
 * @author tanlei
 * @date 2019/9/4
 * @describe
 */

public class FloatWindowManager implements FloatCallBack,FloatLayout.LongClickListener {
    /**
     * 悬浮窗
     */
    private static FloatLayout mFloatLayout;
    private static WindowManager windowManager;
    private static WindowManager.LayoutParams wmParams;
    private static boolean showRecord, showNavigation, showMessage;
    private static FloatWindowManager floatWindowManager;
    private static int screenWidth;
    private static int screenHeight;
    private View navigationView, messageView;
    private int w, h;

    private ImageView mGuideIv;
    private TextView mGuideTv;
    private NextTurnTipView mGuideIv1;

    private FloatWindowManager() {
        createFloatWindow(TourApplication.context);
        w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    }

    public static FloatWindowManager getInstance() {
        if (floatWindowManager == null) {
            floatWindowManager = new FloatWindowManager();
        }
        return floatWindowManager;
    }

    public static void commonROMPermissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public void createFloatWindow(Context context) {
        wmParams = new WindowManager.LayoutParams();
        windowManager = getWindowManager(context);
        mFloatLayout = new FloatLayout(context);
        if (Build.VERSION.SDK_INT >= 24) { /*android7.0不能用TYPE_TOAST*/
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            /*if (isMIUI("huawei") || isMIUI("meizu") || isMIUI("oppo")|| isMIUI("OnePlus")){
                wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }*/
            if (Build.VERSION.SDK_INT>=26) {//8.0新特性
                wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else{
                wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
        } else { /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
            String packname = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
            if (permission) {
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        windowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        screenWidth = dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = screenWidth;
        wmParams.y = screenHeight;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mFloatLayout.setParams(wmParams);
        mFloatLayout.setLongClickListener(this);
    }

    public static boolean isMIUI(String type) {
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu xiaomi
        if (type.equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    /**
     * 移除悬浮窗
     */
    public static void removeFloatWindowManager() {
        //移除悬浮窗口
        boolean isAttach = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isAttach = mFloatLayout.isAttachedToWindow();
        }
        if (showRecord && isAttach && windowManager != null)
            windowManager.removeView(mFloatLayout);
    }

    /**
     * 返回当前已创建的WindowManager。
     */
    private static WindowManager getWindowManager(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    @Override
    public void showRecordView() {
        if (!showRecord) {
            wmParams.x = screenWidth / 3;
            wmParams.y = screenHeight;
            windowManager.addView(mFloatLayout, wmParams);
            showRecord = true;

        }
    }

    @Override
    public void hideRecordView() {
        if (showRecord) {
            windowManager.removeViewImmediate(mFloatLayout);
            showRecord = false;
        }
    }

    @Override
    public void showNavigationView(View view) {
        if (!showNavigation) {
            this.navigationView = view;
            view.measure(w, h);
            //获得宽高
            int viewHeight = view.getMeasuredHeight();
            wmParams.x = screenWidth;
            wmParams.y = screenHeight / 2 - viewHeight / 2;
            windowManager.addView(view, wmParams);
            showNavigation = true;
            if (navigationView != null) {
                navigationView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TourApplication.playClickVoiceMAX();
                        IntentManager.getInstance().goSharedNavigationGuideActivityBackUp(ActivityUtils.getTopActivity(),true,0.0,0.0,0.0,0.0,"",0);
                    }
                });
            }
        }
    }
    @Override
    public void updateShowandHide(boolean isShow) {
        if (navigationView != null) {
            if (!isShow) {
                navigationView.setVisibility(View.GONE);
            } else {
                navigationView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void updateNavigationView(Bitmap bitmap, int icontype, String content) {
        if (showNavigation && navigationView != null) {
            if (mGuideIv == null) {
                mGuideIv = navigationView.findViewById(R.id.iv);
            }
            if (mGuideTv == null) {
                mGuideTv = navigationView.findViewById(R.id.tv);
            }
            if (mGuideIv1 == null) {
                mGuideIv1 = navigationView.findViewById(R.id.iv1);
            }
            // update interface imageview
            if (bitmap != null) {
                mGuideIv1.setIconBitmap(bitmap);
            } else {
                mGuideIv1.setIconType(icontype);
            }
            // update interface content
            mGuideTv.setText(content);
        }
    }

    @Override
    public void hideNavigationView() {
        if (showNavigation && navigationView != null) {
            windowManager.removeViewImmediate(navigationView);
            mGuideIv1 = null;
            mGuideIv = null;
            mGuideTv = null;
            showNavigation = false;
        }
    }

    @Override
    public void showMessageView(View view) {
        if (!showMessage) {
            this.messageView = view;
            view.measure(w, h);
            //获得宽高
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();
            wmParams.x = screenWidth / 2 - viewWidth / 2;
            wmParams.y = screenHeight / 2 - viewHeight / 2;
            windowManager.addView(view, wmParams);
            showMessage = true;
            if (messageView != null) {
                messageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtils.showText(TourApplication.context, "点击的消息");
                    }
                });
            }
        }
    }

    @Override
    public void hideMessageView() {
        if (showMessage && messageView != null) {
            windowManager.removeViewImmediate(messageView);
            showMessage = false;
        }
    }

    @Override
    public void onUpClick() {
    }

    @Override
    public void onTalkClick() {
    }

    // 对讲组弹出框显示(讲话方)
    public void setAudioRecordShow() {
        if (mFloatLayout != null){
            mFloatLayout.setAudioRecordTimerShow();
        }
    }

    // 对讲组弹出框开始记时(讲话方)
    public void setAudioRecordStartTimer() {
        if (mFloatLayout != null){
            mFloatLayout.setAudioRecordTimerStart();
        }
    }

    // 对讲组弹出框隐藏(讲话方)
    public void setAudioRecordHide() {
        if (mFloatLayout != null){
            mFloatLayout.setAudioRecordTimerHide();
        }
    }

    // 对讲组弹出框是否能够按住对讲(收听方)
    public void setAudioRecordCanTalk(boolean canTalk) {
        if (mFloatLayout != null){
            mFloatLayout.setCanTalk(canTalk);
        }
    }

}
