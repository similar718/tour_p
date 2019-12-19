package cn.xmzt.www.intercom.cache;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by Averysk
 * <p>
 * 群聊相关业务缓存生命周期管理
 */
public class TeamMessageAudioCacheManager {
    private static boolean enableCache=true;

    /*static {
        boolean teamMessageAudio = false;
        try {
            Class.forName("com.netease.nimlib.sdk.msg.MsgService");
            teamMessageAudio = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        enableCache = teamMessageAudio;
    }*/

    public static void initCache() {
        if (enableCache) {
            TeamMessageAudioCache.getInstance().clearCacheAll();
            TeamMessageAudioCache.getInstance().registerObservers(true);
        }
    }

    public static void clearCacheAll() {
        if (enableCache) {
            TeamMessageAudioCache.getInstance().registerObservers(false);
            TeamMessageAudioCache.getInstance().clearCacheAll();
        }
    }



    public static void saveMessageAudio(IMMessage member) {
        if (enableCache) {
            TeamMessageAudioCache.getInstance().saveMessageAudio(member);
        }
    }


    public static void deleteMessage(IMMessage member) {
        if (enableCache) {
            TeamMessageAudioCache.getInstance().deleteMessage(member);
        }
    }



    public static IMMessage getCacheMessageAudio() {
        if (enableCache) {
            return TeamMessageAudioCache.getInstance().getCacheMessageAudio();
        }
        return null;
    }



    public static List<IMMessage> getCacheMessageAudioList() {
        if (enableCache) {
            return TeamMessageAudioCache.getInstance().getCacheMessageAudioList();
        }
        return null;
    }


    public static void sendReceipt(IMMessage message) {
        if (enableCache) {
            TeamMessageAudioCache.getInstance().sendReceipt(message);
        }
    }
}
