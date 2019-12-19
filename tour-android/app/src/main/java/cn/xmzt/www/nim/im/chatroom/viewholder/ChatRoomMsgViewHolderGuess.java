package cn.xmzt.www.nim.im.chatroom.viewholder;

import android.widget.ImageView;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.im.session.extension.GuessAttachment;
import cn.xmzt.www.nim.uikit.business.chatroom.viewholder.ChatRoomMsgViewHolderBase;
import cn.xmzt.www.intercom.adapter.list.BaseMultiItemFetchLoadAdapter;
import cn.xmzt.www.nim.uikit.common.util.sys.ScreenUtil;

/**
 * Created by hzliuxuanlin on 17/9/15.
 */
public class ChatRoomMsgViewHolderGuess extends ChatRoomMsgViewHolderBase {

    private GuessAttachment guessAttachment;
    private ImageView imageView;

    public ChatRoomMsgViewHolderGuess(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.chatroom_rock_paper_scissors;
    }

    @Override
    protected void inflateContentView() {
        imageView = (ImageView) view.findViewById(R.id.message_rock_paper_scissors_body);
    }

    @Override
    protected boolean isShowBubble() {
        return false;
    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        guessAttachment = (GuessAttachment) message.getAttachment();
        switch (guessAttachment.getValue().getDesc()) {
            case "石头":
                imageView.setImageResource(R.drawable.message_view_rock);
                break;
            case "剪刀":
                imageView.setImageResource(R.drawable.message_view_scissors);
                break;
            case "布":
                imageView.setImageResource(R.drawable.message_view_paper);
                break;
            default:
                break;
        }
        imageView.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);

    }
}
