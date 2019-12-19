package cn.xmzt.www.intercom.cache;

import cn.xmzt.www.intercom.bean.MyTalkGroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 群聊消息(语音)缓存
 * @author Averysk
 */
public class TalkGroupListCache {

    private static final String TAG = "AnyRtc";

    public static TalkGroupListCache getInstance() {
        return InstanceHolder.instance;
    }

    /** ************************************ 单例 *************************************** */
    private static class InstanceHolder {
        private final static TalkGroupListCache instance = new TalkGroupListCache();
    }

    /** ************************************ 缓存 *************************************** */
    private List<MyTalkGroupBean> cacheTalkGroupInfo = new ArrayList<>();

    /** ************************************ 清除所有 *************************************** */
    public void clearCacheAll() {
        cacheTalkGroupInfo.clear();
    }

    /** ************************************ 保存 *************************************** */
    public void saveTalkGroupList(List<MyTalkGroupBean> list) {
        cacheTalkGroupInfo.addAll(list);
    }

    /** ************************************ 获取列表 *************************************** */
    public List<MyTalkGroupBean> getTalkGroupList(){
        return cacheTalkGroupInfo;
    }

}
