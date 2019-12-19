package cn.xmzt.www.intercom.bean;

/**
 * 群语音消息是还自动接听播放状态信息
 *  @author Averysk
 */
public class TeamAudioPlayBean {

    private String sessionId;              // 当前群id
    private boolean isPlay;                // 当前群是否自动接听播放

    public TeamAudioPlayBean(){}

    public TeamAudioPlayBean(String sessionId, boolean isPlay){
        this.sessionId = sessionId;
        this.isPlay = isPlay;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
