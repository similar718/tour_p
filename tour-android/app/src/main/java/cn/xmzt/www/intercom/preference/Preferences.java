package cn.xmzt.www.intercom.preference;

import android.content.Context;
import android.content.SharedPreferences;

import cn.xmzt.www.nim.im.DemoCache;

/**
 * Created by Averies.
 */
public class Preferences {

    private static SharedPreferences getSharedPreferences() {
        return DemoCache.getContext().getSharedPreferences("Tour", Context.MODE_PRIVATE);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    private static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    private static final String KEY_USER_ACCOUNT = "account";               // 用户云信账号
    private static final String KEY_USER_TOKEN = "token";                   // 用户token
    private static final String KEY_USER_PWD = "user_pwd";                  // 用户云信密码
    private static final String KEY_USER_PHONE = "user_phone";              // 用户手机号
    private static final String KEY_USER_ID = "user_id";                    // 用户id
    private static final String KEY_USER_NAME = "user_name";                // 用户名称
    private static final String KEY_USER_AVATAR_URL = "user_avatar_url";    // 用户头像url
    private static final String KEY_USER_APP_URL = "shareapp_url";          // 用户分享AppUrl


    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

/*
    public static void saveUserPwd(String pwd) {
        saveString(KEY_USER_PWD, pwd);
    }

    public static String getUserPwd() {
        return getString(KEY_USER_PWD);
    }
*/

    public static void saveUserPhone(String phone) {
        saveString(KEY_USER_PHONE, phone);
    }

    public static String getUserPhone() {
        return getString(KEY_USER_PHONE);
    }

    public static void saveUserId(String id) {
        saveString(KEY_USER_ID, id);
    }

    public static String getUserId() {
        return getString(KEY_USER_ID);
    }

    public static void saveUserName(String name) {
        saveString(KEY_USER_NAME, name);
    }

    public static String getUserName() {
        return getString(KEY_USER_NAME);
    }

    public static void saveUserAvatarUrl(String url) {
        saveString(KEY_USER_AVATAR_URL, url);
    }

    public static String getUserAvatarUrl() {
        return getString(KEY_USER_AVATAR_URL);
    }

/*
    public static void saveUserAppUrl(String url) {
        saveString(KEY_USER_APP_URL, url);
    }

    public static String getUserAppUrl() {
        return getString(KEY_USER_APP_URL);
    }
*/
}
