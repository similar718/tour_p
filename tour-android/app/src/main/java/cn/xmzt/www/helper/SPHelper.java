package cn.xmzt.www.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
    static SharedPreferences sp;

    public static void init(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static final String UUID_LOCAL = "UUID_LOCAL";

    public static void setUuidLocal(String uuidLocal) {
        if (null != sp) {
            sp.edit().putString(UUID_LOCAL, uuidLocal).apply();
        }
    }

    public static String getUuidLocal() {
        if (null != sp) {
            return sp.getString(UUID_LOCAL, "");
        }
        return null;
    }

    private static final String User_Token = "Authorization";

    public static void setUserTokene(String token) {
        if (null != sp) {
            sp.edit().putString(User_Token, token).apply();
        }
    }

    public static String getUserTokene() {
        if (null != sp) {
            return sp.getString(User_Token, "");
        }
        return null;
    }

    private static final String User_ReqId = "ReqId";

    public static void setUserReqId(String reqId) {
        if (null != sp) {
            sp.edit().putString(User_ReqId, reqId).apply();
        }
    }

    public static String getUserReqId() {
        if (null != sp) {
            return sp.getString(User_ReqId, "");
        }
        return null;
    }

    //============================================================================================================

    private static final String User_Id = "User-Id";

    public static void setUserId(String userId) {
        if (null != sp) {
            sp.edit().putString(User_Id, userId).apply();
        }
    }

    public static String getUserId() {
        if (null != sp) {
            return sp.getString(User_Id, null);
        }
        return null;
    }

    private static final String APP_Platform = "APP-Platform";

    public static void setAppPlatform(String platform) {
        if (null != sp) {
            sp.edit().putString(APP_Platform, platform).apply();
        }
    }

    public static String getAppPlatform() {
        if (null != sp) {
            return sp.getString(APP_Platform, null);
        }
        return null;
    }

    private static final String APP_Version = "APP-Version";

    public static void setAppVersion(String version) {
        if (null != sp) {
            sp.edit().putString(APP_Version, version).apply();
        }
    }

    public static String getAppVersion() {
        if (null != sp) {
            return sp.getString(APP_Version, null);
        }
        return null;
    }

}
