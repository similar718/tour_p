package cn.xmzt.www.suspension;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * @author tanlei
 * @date 2019/9/4
 * @describe
 */

public class FloatMonkService extends Service {
    /**
     * home键监听
     */
    private HomeWatcherReceiver mHomeKeyReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
//        FloatActionController.getInstance().registerCallLittleMonk(this);
        //注册广播接收者
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeKeyReceiver, homeFilter);
        //初始化悬浮窗UI
        initWindowData();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 初始化WindowManager
     */
    private void initWindowData() {
//        FloatWindowManager.createFloatWindow(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除悬浮窗
        FloatWindowManager.removeFloatWindowManager();
        //注销广播接收者
        if (null != mHomeKeyReceiver) {
            unregisterReceiver(mHomeKeyReceiver);
        }
    }
}