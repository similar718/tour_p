package cn.xmzt.www.nim.im.session.viewholder;

import cn.xmzt.www.intercom.attachment.DefaultCustomAttachment;
import cn.xmzt.www.nim.uikit.business.session.viewholder.MsgViewHolderText;
import cn.xmzt.www.intercom.adapter.list.BaseMultiItemFetchLoadAdapter;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderDefCustom extends MsgViewHolderText {

    public MsgViewHolderDefCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected String getDisplayText() {
        DefaultCustomAttachment attachment = (DefaultCustomAttachment) message.getAttachment();

        return "type: " + attachment.getType() + ", data: " + attachment.getContent();
    }
}
