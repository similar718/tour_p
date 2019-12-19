package cn.xmzt.www.helper;


import com.blankj.utilcode.util.ActivityUtils;
import cn.xmzt.www.intercom.preference.Preferences;
import cn.xmzt.www.main.globals.AnyRtcMaxManage;
import cn.xmzt.www.main.globals.AudioPlayManage;
import cn.xmzt.www.main.globals.FloatAudioPlayManage;
import cn.xmzt.www.main.globals.FloatIntercomManage;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.nim.im.login.LogoutHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import java.util.UUID;

/**
 * 用户管理类
 * @author  Averies
 * @data 2019/05/08
 */
public class UserHelper {

    public static void createUUID() {
        SPHelper.setUuidLocal(UUID.randomUUID().toString().replaceAll("-", ""));
    }

    public static String getUUID() {
        return SPHelper.getUuidLocal();
    }

    public static void setToken(String token) {
        SPHelper.setUserTokene(token);
    }

    public static String getToken() {
        return SPHelper.getUserTokene();
    }

    public static void setReqId(String reqId) {
        SPHelper.setUserReqId(reqId);
    }

    public static String getReqId() {
        return SPHelper.getUserReqId();
    }

    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NIMClient.getService(AuthService.class).logout();
        clearTalkBusiness(true);
        logoutNim();
        logoutUmeng();
        logoutCache();
    }
    public static void clearTalkBusiness(boolean isLogout) {
        // 隐藏对讲按钮和音频播放器
        TalkManage.isShowTalk = false;
        FloatIntercomManage.getInstance().hideFloatView(ActivityUtils.getTopActivity());
        TalkManage.isShowPlay = false;
        FloatAudioPlayManage.getInstance().hideFloatView(ActivityUtils.getTopActivity());
        if(isLogout){
            // 注销相关业务
            TalkManage.getInstance().logout();
        }else {
            // 清除对讲
            TalkManage.getInstance().clearTalk();
        }
    }
    /**
     * 注销云信
     */
    public static void logoutNim() {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();
    }

    /**
     * 登出友盟
     */
    public static void logoutUmeng() {
        //登出
        //MobclickAgent.onProfileSignOff();
    }

    /**
     * 清除缓存
     */
    public static void logoutCache() {
        Preferences.saveUserAccount("");
        Preferences.saveUserToken("");
        //Preferences.saveUserPwd("");
        Preferences.saveUserPhone("");
        Preferences.saveUserId("");
        Preferences.saveUserName("");
        Preferences.saveUserAvatarUrl("");
        //Preferences.saveUserAppUrl("");
    }

}
