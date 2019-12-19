package cn.xmzt.www.mine.event;

/**
 * @author tanlei
 * @date 2019/8/8
 * @describe 
 */

public class NicknameEvent {
    public String nickName;

    public NicknameEvent() {
    }

    public NicknameEvent(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
