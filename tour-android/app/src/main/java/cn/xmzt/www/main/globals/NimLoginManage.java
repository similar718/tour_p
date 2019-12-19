package cn.xmzt.www.main.globals;

import android.util.Log;

import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.intercom.preference.Preferences;
import cn.xmzt.www.intercom.preference.UserPreferences;
import cn.xmzt.www.nim.im.DemoCache;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class NimLoginManage {
    private NimLoginManage() {
    }
    //创建单例
    public static class SingleonHolder {
        private static final NimLoginManage instance = new NimLoginManage();
    }

    //获取单例
    public static NimLoginManage getInstance() {
        return NimLoginManage.SingleonHolder.instance;
    }

    private AbortableFuture<LoginInfo> loginRequest;

    public void loginNim(String account, String token) {
        // 登录
        loginRequest = NimUIKit.login(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                Log.e("shiyong", "云信登录成功");
                DemoCache.setAccount(account);
                // 保存用户信息
                saveLoginInfo(account, token);
                // 初始化消息提醒配置
                initNotificationConfig();
            }

            @Override
            public void onFailed(int code) {
                Log.e("shiyong", "云信登录失败");
            }

            @Override
            public void onException(Throwable exception) {
                Log.e("shiyong", "云信登录异常失败");
            }
        });
    }

    private void saveLoginInfo(String account, String token) {
        UserHelper.setToken(token);
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

}
