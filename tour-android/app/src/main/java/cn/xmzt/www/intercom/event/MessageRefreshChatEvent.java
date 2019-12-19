package cn.xmzt.www.intercom.event;

import cn.xmzt.www.intercom.session.module.Container;

/**
 * 操作对讲后刷新界面eventbus
 */
public class MessageRefreshChatEvent {

    /*
    1: 加入对讲组成功
    2: 加入对讲组失败
    3: 离开对讲组
    4: 申请对讲准备中
    5: 我正在发言
    6: 用户某某正在发言
    7: 对点对用户某某正在发言
    8: 对点对用户某某结束发言
    9: 讲话被打断
    10: 监看请求
    11: 停止监看
    12: 监看结果
    13: 报告请求
    14: 报告关闭
    15: 拨打电话成功
    16: 接听电话
    17: 拒接电话
    18: 挂断电话
    19: 发启电话
    20: 正在通话中
    21: 主叫方已挂断本次通话
    22: 打开远程视频
    23: 关闭远程视频
    24: 打开远程音频
    25: 关闭远程音频
    26: 远程音视频状态
    27: 本地音视频状态
    28: 远程音频激活
    29: 本地音频激活
    30: 远程网络状态
    31: 本地网络状态
    32: 用户消息
    33: 当前在线人数
    34: 获取历史记录文件
    101: 本地开启对讲准备
    102: 本地开启正在对讲
    103: 本地传送聊天内容
     */
    private int code;
    /*
    用户code
     */
    private int userCode;
    /*
    用户Num
     */
    private int userNum;
    /*
    用户id
     */
    private String userId;
    /*
    用户数据
     */
    private String userData;
    /*
    本地聊天模块容器
     */
    private Container container;
    /*
    当前群id
     */
    private String groupId;
    /*
    当前事件类型
    1001: 群主/领队发起了广播,但没有开启对讲
    1002: 群主/领队发起了广播,开启了对讲说话
    1003: 群主/领队发起了广播,结束了对讲说话
    1004: 群主/领队关闭了广播
    1011: 收到群主/领队开启广播通知,但还没有收听到开启对讲说话
    1012: 收到群主/领队开启广播通知,也收到了对方正在对讲说话了
    1013: 收到群主/领队开启广播通知,也收到了对方结束对讲说话了
    1014: 收到群主/领队关闭广播通知
     */
    private int type;

    public MessageRefreshChatEvent(int code) {
        this.code = code;
    }

    public MessageRefreshChatEvent(int code, String userData) {
        this.code = code;
        this.userData = userData;
    }

    public MessageRefreshChatEvent(int code, String groupId, int type) {
        this.code = code;
        this.groupId = groupId;
        this.type = type;
    }

    public MessageRefreshChatEvent(int code, String userId, String userData) {
        this.code = code;
        this.userId = userId;
        this.userData = userData;
    }

    public MessageRefreshChatEvent(int code, String userId, String userData, int userCode) {
        this.code = code;
        this.userId = userId;
        this.userData = userData;
        this.userCode = userCode;
    }

    public MessageRefreshChatEvent(int code, int userNum) {
        this.code = code;
        this.userNum = userNum;
    }

    public MessageRefreshChatEvent(int code, Container container) {
        this.code = code;
        this.container = container;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}