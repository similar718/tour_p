package cn.xmzt.www.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;

import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;

public class SPUtils {
    public static final String COMMON_KEY_VALUE = "tour";
    public static final String KEY_IS_FIRST = "KEY_IS_FIRST";
    public static final String KEY_TOKEN = "TOUR_TOKEN";

    public static final String SHOW_TIME_NEAR = "SHOW_TIME_NEAR"; // 近期不再显示

    public static final String GUIDE_DBYD = "GUIDE_DBYD"; // 躲避拥堵 0 false 1 true
    public static final String GUIDE_DBGS = "GUIDE_DBGS"; // 躲避高速 0 false 1 true
    public static final String GUIDE_DBSF = "GUIDE_DBSF"; // 躲避收费 0 false 1 true
    public static final String GUIDE_GSYX = "GUIDE_GSYX"; // 高速优先 0 false 1 true
    public static final String GUIDE_VOICE_MODE = "GUIDE_VOICE_MODE"; // 导航语音播报 0 详细播报 1 简介播报 2 静音
    public static final String GUIDE_SHOW_MODE = "GUIDE_SHOW_MODE"; // 导航视角 0 车头朝北 1 地图朝北 2 3D
    public static final String GUIDE_DAY_NIGHT_MODE = "GUIDE_DAY_NIGHT_MODE"; // 日夜模式 0 自动 1 白天 2 夜间

    public static void putString(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        return sharedPreferences.getString(key, "");
    }

    public static void putInt(String key, int value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        return sharedPreferences.getInt(key, 0);
    }

    public static void putBoolean(String key, boolean value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defualtValue) {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        return sharedPreferences.getBoolean(key, defualtValue);
    }

    public static String getToken() {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        String token = sharedPreferences.getString(KEY_TOKEN, "");
        return token;
    }

    public static void setToken(String token) {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(TextUtils.isEmpty(token)){
            editor.putString("sign_dialog_time","");
        }
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public static void setPaypassword(boolean isPaypassword) {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        sharedPreferences.edit().putBoolean("isPaypassword", isPaypassword).commit();
    }

    public static boolean isPaypassword() {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        return sharedPreferences.getBoolean("isPaypassword", false);
    }

    public static boolean iSLoginLive() {
        String token = getToken();
        if (TextUtils.isEmpty(token)) {
            return false;
        } else {
            return true;
        }
    }

//    public static void clear() {
//        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        SPUtils.putBoolean("isFirst",true);
//        editor.commit();
//    }

    public static void putUser(String userJson) {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        sharedPreferences.edit().putString("userJson", userJson).commit();
    }
    public static UserBasicInfoBean getUser() {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        String userJson = sharedPreferences.getString("userJson", "");
        if(TextUtils.isEmpty(userJson)){
            return null;
        }else {
            UserBasicInfoBean user=FastJsonUtil.parseObject(userJson,UserBasicInfoBean.class);
            return user;
        }
    }
    public static void putAutoPlayGroup(String groupJson) {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        sharedPreferences.edit().putString("groupJson", groupJson).commit();
    }
    public static String getAutoPlayGroup() {
        SharedPreferences sharedPreferences = TourApplication.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        String groupJson = sharedPreferences.getString("groupJson", "");
        return groupJson;
    }
}
