package cn.xmzt.www.nim.im.chatroom.thridparty;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import cn.xmzt.www.nim.im.DemoCache;
import cn.xmzt.www.nim.im.DemoPrivatizationConfig;
import cn.xmzt.www.nim.im.chatroom.helper.ExtensionHelper;
import cn.xmzt.www.nim.im.config.DemoServers;
import cn.xmzt.www.nim.uikit.common.http.NimHttpClient;
import cn.xmzt.www.intercom.common.util.log.LogUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网易云信Demo聊天室Http客户端。第三方开发者请连接自己的应用服务器。
 * <p/>
 * Created by huangjun on 2016/1/18.
 */
public class ChatRoomHttpClient {
    private static final String TAG = ChatRoomHttpClient.class.getSimpleName();

    // code
    private static final int RESULT_CODE_SUCCESS = 200;

    // api
    private static final String API_NAME_CHAT_ROOM_LIST = "homeList";
    private static final String API_NAME_CHAT_ROOM_ADDRESS = "requestAddress";

    // header
    private static final String HEADER_KEY_APP_KEY = "appkey";

    // request
    private static final String REQUEST_KEY_UID = "uid";
    private static final String REQUEST_KEY_ROOM_ID = "roomid";
    private static final String REQUEST_KEY_TYPE = "type";

    // result
    private static final String RESULT_KEY_RES = "res";
    private static final String RESULT_KEY_MSG = "msg";
    private static final String RESULT_KEY_TOTAL = "total";
    private static final String RESULT_KEY_LIST = "list";
    private static final String RESULT_KEY_NAME = "name";
    private static final String RESULT_KEY_CREATOR = "creator";
    private static final String RESULT_KEY_STATUS = "status";
    private static final String RESULT_KEY_ANNOUNCEMENT = "announcement";
    private static final String RESULT_KEY_EXT = "ext";
    private static final String RESULT_KEY_ROOM_ID = "roomid";
    private static final String RESULT_KEY_BROADCAST_URL = "broadcasturl";
    private static final String RESULT_KEY_ONLINE_USER_COUNT = "onlineusercount";
    private static final String RESULT_KEY_ADDR = "addr";

    public interface ChatRoomHttpCallback<T> {
        void onSuccess(T t);

        void onFailed(int code, String errorMsg);
    }

    private static ChatRoomHttpClient instance;

    public static synchronized ChatRoomHttpClient getInstance() {
        if (instance == null) {
            instance = new ChatRoomHttpClient();
        }

        return instance;
    }

    private ChatRoomHttpClient() {
        NimHttpClient.getInstance().init(DemoCache.getContext());
    }

    /**
     * 向网易云信Demo应用服务器请求聊天室列表
     */
    public void fetchChatRoomList(final ChatRoomHttpCallback<List<ChatRoomInfo>> callback) {

        String url = DemoServers.chatRoomAPIServer() + API_NAME_CHAT_ROOM_LIST;
        if (!DemoPrivatizationConfig.isPrivateDisable(DemoCache.getContext())) {
            url = DemoPrivatizationConfig.getChatListRoomUrl(DemoCache.getContext());
        }

        Map<String, String> headers = new HashMap<>(1);
        String appKey = readAppKey();
        headers.put(HEADER_KEY_APP_KEY, appKey);

        NimHttpClient.getInstance().execute(url, headers, null, false, new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, Throwable exception) {
                if (code != 200 || exception != null) {
                    LogUtil.e(TAG, "fetchChatRoomList failed : code = " + code + ", errorMsg = "
                            + (exception != null ? exception.getMessage() : "null"));
                    if (callback != null) {
                        callback.onFailed(code, exception != null ? exception.getMessage() : "null");
                    }
                    return;
                }

                try {
                    // ret 0
                    JSONObject res = JSONObject.parseObject(response);
                    // res 1
                    int resCode = res.getIntValue(RESULT_KEY_RES);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        // msg 1
                        JSONObject msg = res.getJSONObject(RESULT_KEY_MSG);
                        List<ChatRoomInfo> roomInfoList = null;
                        if (msg != null) {
                            // total 2
                            roomInfoList = new ArrayList<>(msg.getIntValue(RESULT_KEY_TOTAL));

                            // list 2
                            JSONArray rooms = msg.getJSONArray(RESULT_KEY_LIST);
                            for (int i = 0; i < rooms.size(); i++) {
                                // room 3
                                JSONObject room = rooms.getJSONObject(i);
                                ChatRoomInfo roomInfo = new ChatRoomInfo();
                                roomInfo.setName(room.getString(RESULT_KEY_NAME));
                                roomInfo.setCreator(room.getString(RESULT_KEY_CREATOR));
                                roomInfo.setValidFlag(room.getIntValue(RESULT_KEY_STATUS));
                                roomInfo.setAnnouncement(room.getString(RESULT_KEY_ANNOUNCEMENT));
                                roomInfo.setExtension(ExtensionHelper.getMapFromJsonString(room.getString(RESULT_KEY_EXT)));
                                roomInfo.setRoomId(room.getString(RESULT_KEY_ROOM_ID));
                                roomInfo.setBroadcastUrl(room.getString(RESULT_KEY_BROADCAST_URL));
                                roomInfo.setOnlineUserCount(room.getIntValue(RESULT_KEY_ONLINE_USER_COUNT));
                                roomInfoList.add(roomInfo);
                            }
                        }
                        // reply
                        callback.onSuccess(roomInfoList);
                    } else {
                        callback.onFailed(resCode, null);
                    }
                } catch (JSONException e) {
                    callback.onFailed(-1, e.getMessage());
                } catch (Exception e) {
                    callback.onFailed(-2, e.getMessage());
                }
            }
        });
    }


    private String readAppKey() {
        try {
            ApplicationInfo appInfo = DemoCache.getContext().getPackageManager()
                    .getApplicationInfo(DemoCache.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.jm.selfdriving.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
