package cn.xmzt.www.nim.im.login;

import cn.xmzt.www.nim.im.DemoCache;
import cn.xmzt.www.nim.im.redpacket.NIMRedPacketClient;
import cn.xmzt.www.intercom.api.NimUIKit;

/**
 * 注销帮助类
 * Created by Averysk
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        DemoCache.clear();
        NIMRedPacketClient.clear();
    }
}
