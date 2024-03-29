package cn.xmzt.www.nim.uikit.business.contact.core.viewholder;

import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.nim.uikit.business.contact.core.item.ContactItem;
import cn.xmzt.www.nim.uikit.business.contact.core.model.ContactDataAdapter;
import cn.xmzt.www.nim.uikit.business.contact.core.model.IContact;
import cn.xmzt.www.intercom.impl.NimUIKitImpl;

/**
 * Created by hzchenkang on 2017/4/6.
 */

public class OnlineStateContactHolder extends ContactHolder {

    @Override
    public void refresh(ContactDataAdapter adapter, int position, ContactItem item) {
        super.refresh(adapter, position, item);
        IContact contact = item.getContact();
        // 在线状态
        if (contact.getContactType() != IContact.Type.Friend || !NimUIKitImpl.enableOnlineState()) {
            desc.setVisibility(View.GONE);
        } else {
            String onlineStateContent = NimUIKitImpl.getOnlineStateContentProvider().getSimpleDisplay(contact.getContactId());
            if (TextUtils.isEmpty(onlineStateContent)) {
                desc.setVisibility(View.GONE);
            } else {
                desc.setVisibility(View.VISIBLE);
                desc.setText(onlineStateContent);
            }
        }
    }
}
