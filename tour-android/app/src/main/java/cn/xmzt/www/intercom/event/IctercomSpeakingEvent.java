package cn.xmzt.www.intercom.event;

public class IctercomSpeakingEvent {

    /*
    群id
     */
    private String groupId;
    /*
    是否正在讲话中
     */
    private boolean isSpeaking;

    public IctercomSpeakingEvent(String groupId, boolean isSpeaking) {
        this.groupId = groupId;
        this.isSpeaking = isSpeaking;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }
}
