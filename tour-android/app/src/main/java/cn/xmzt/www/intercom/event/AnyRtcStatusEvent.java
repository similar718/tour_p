package cn.xmzt.www.intercom.event;

import cn.xmzt.www.intercom.session.module.Container;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 操作传递对讲状态eventbus
 */
public class AnyRtcStatusEvent {

    private int code;
    /*
    本地聊天模块容器
     */
    private Container container;
    /*
    消息内容
     */
    private IMMessage message;

    public AnyRtcStatusEvent(int code) {
        this.code = code;
     }

    public AnyRtcStatusEvent(int code, Container container) {
        this.code = code;
        this.container = container;
    }

    public AnyRtcStatusEvent(int code, IMMessage message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public IMMessage getMessage() {
        return message;
    }

    public void setMessage(IMMessage message) {
        this.message = message;
    }
}