package cn.xmzt.www.selfdrivingtools.event;

public class PlayVoiceTypeEvent {
    private int type;

    public PlayVoiceTypeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
