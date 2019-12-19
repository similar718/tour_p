package cn.xmzt.www.utils;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.xmzt.www.base.TourApplication;

/**
 * 微信授权
 */
public class WxLoginHelper {
    public static String STATE_WX_LOGIN = "state_wx_login";
    public static String STATE_WX_BOUND = "state_wx_bound";

    public static final String WX_APP_ID = "wx3b08b3acfc7dbcb9";

    public static final String WX_SECRET = "5a213cbbe951b9cd6a3beba237aac1be";
    private static IWXAPI wx_api; //全局的微信api对象

    public static IWXAPI getWx_api() {
        if (null == wx_api) {
            wx_api = WXAPIFactory.createWXAPI(TourApplication.context, WX_APP_ID, true);
            wx_api.registerApp(WX_APP_ID);
        }
        return wx_api;
    }

    /**
     * 微信授权
     * @param state
     */
    public static void wxAuth(String state) {
        //发起登录请求
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = state;
        getWx_api().sendReq(req);
    }
}
