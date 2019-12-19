package cn.xmzt.www.main.globals;

/**
 * 消息扩展类型
 */
public class MsgExtensionType {

    // 对讲语音录制按钮悬浮窗
    public static final String Float_AUDIO_RECORD = "audio_record";
    // 对讲语音播放显示悬浮窗
    public static final String Float_AUDIO_PLAY = "audio_play";
    // 扩展消息类型
    public static final String Extension_Type = "type";
    // 扩展消息群id
    public static final String Extension_GroupId = "groupId";

    // 扩展消息类型(位置消息)
    public static final String Extension_Type_001 = "1";
    // 扩展消息类型(对讲录制)
    public static final String Extension_Type_101 = "101";
    // 扩展消息类型(语音录制)
    public static final String Extension_Type_102 = "102";
    // 扩展消息类型(发起广播)
    public static final String Extension_Type_301 = "301";
    // 扩展消息类型(结束广播)
    public static final String Extension_Type_302 = "302";

    // 扩展消息类型(修改群名称)
    public static final String Extension_Type_201 = "201";

    public static String groupId = "";
    public static long entry_system_time = 0;
}
