package cn.xmzt.www.nim.uikit.impl.provider;

import cn.xmzt.www.nim.uikit.api.model.SimpleCallback;
import cn.xmzt.www.nim.uikit.api.model.robot.RobotInfoProvider;
import cn.xmzt.www.nim.uikit.impl.cache.RobotInfoCache;
import com.netease.nimlib.sdk.robot.model.NimRobotInfo;

import java.util.List;

/**
 * Created by hzchenkang on 2017/11/2.
 */

public class DefaultRobotProvider implements RobotInfoProvider {

    @Override
    public NimRobotInfo getRobotByAccount(String account) {
        return RobotInfoCache.getInstance().getRobotByAccount(account);
    }

    @Override
    public List<NimRobotInfo> getAllRobotAccounts() {
        return RobotInfoCache.getInstance().getAllRobotAccounts();
    }

    @Override
    public void fetchRobotList(SimpleCallback<List<NimRobotInfo>> callback) {
        RobotInfoCache.getInstance().fetchRobotList(callback);
    }

    @Override
    public void fetchRobotListIndependent(String roomId, SimpleCallback<List<NimRobotInfo>> callback) {
        RobotInfoCache.getInstance().pullRobotListIndependent(roomId, callback);
    }
}
