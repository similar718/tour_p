package cn.xmzt.www.intercom.session.module;

import android.app.Activity;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * 发送消息容器
 * Created by Averysk
 */
public class Container {

    public final Activity activity;

    public final String account;

    public final SessionTypeEnum sessionType;

    public final ModuleProxy proxy;

    public final boolean proxySend;

    public Container(Activity activity, String account, SessionTypeEnum sessionType, ModuleProxy proxy) {
        this.activity = activity;
        this.account = account;
        this.sessionType = sessionType;
        this.proxy = proxy;
        this.proxySend = false;
    }

    public Container(Activity activity, String account, SessionTypeEnum sessionType, ModuleProxy proxy, boolean proxySend) {
        this.activity = activity;
        this.account = account;
        this.sessionType = sessionType;
        this.proxy = proxy;
        this.proxySend = proxySend;
    }
}
