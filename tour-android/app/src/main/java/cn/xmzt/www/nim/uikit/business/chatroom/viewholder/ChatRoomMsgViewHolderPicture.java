package cn.xmzt.www.nim.uikit.business.chatroom.viewholder;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.uikit.business.session.activity.WatchMessagePictureActivity;
import cn.xmzt.www.intercom.adapter.list.BaseMultiItemFetchLoadAdapter;

public class ChatRoomMsgViewHolderPicture extends ChatRoomMsgViewHolderThumbBase {

    public ChatRoomMsgViewHolderPicture(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_picture;
    }

    @Override
    protected void onItemClick() {
        WatchMessagePictureActivity.start(context, message);
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        return path;
    }
}
