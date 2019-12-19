package cn.xmzt.www.mine.event;

import java.util.Map;

/**
 * 推送消息
 */
public class PushMessageEvent {
    private boolean isClickNotification;
    private Map<String,Object> extraMap;

    public PushMessageEvent() {
    }

    public PushMessageEvent(boolean isClickNotification, Map<String, Object> extraMap) {
        this.isClickNotification = isClickNotification;
        this.extraMap = extraMap;
    }

    public boolean isClickNotification() {
        return isClickNotification;
    }

    public void setClickNotification(boolean clickNotification) {
        isClickNotification = clickNotification;
    }

    public Map<String, Object> getExtraMap() {
        return extraMap;
    }

    public void setExtraMap(Map<String, Object> extraMap) {
        this.extraMap = extraMap;
    }
}
