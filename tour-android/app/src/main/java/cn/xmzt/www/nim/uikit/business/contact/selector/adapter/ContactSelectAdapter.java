package cn.xmzt.www.nim.uikit.business.contact.selector.adapter;

import android.content.Context;

import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.nim.uikit.business.contact.core.item.AbsContactItem;
import cn.xmzt.www.nim.uikit.business.contact.core.item.ContactItem;
import cn.xmzt.www.nim.uikit.business.contact.core.item.ItemTypes;
import cn.xmzt.www.nim.uikit.business.contact.core.model.ContactDataAdapter;
import cn.xmzt.www.nim.uikit.business.contact.core.model.ContactGroupStrategy;
import cn.xmzt.www.nim.uikit.business.contact.core.model.IContact;
import cn.xmzt.www.nim.uikit.business.contact.core.query.IContactDataProvider;
import cn.xmzt.www.nim.uikit.business.contact.core.util.ContactHelper;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ContactSelectAdapter extends ContactDataAdapter {
    private HashSet<String> selects = new HashSet<String>();

    public ContactSelectAdapter(Context context,
                                ContactGroupStrategy groupStrategy, IContactDataProvider dataProvider) {
        super(context, groupStrategy, dataProvider);
    }

    public final void setAlreadySelectedAccounts(List<String> accounts) {
        selects.addAll(accounts);
    }

    public final List<ContactItem> getSelectedItem() {
        if (selects.isEmpty()) {
            return null;
        }

        List<ContactItem> res = new ArrayList<>();
        for (String account : selects) {
            final UserInfo user = NimUIKit.getUserInfoProvider().getUserInfo(account);
            if (user != null) {
                res.add(new ContactItem(ContactHelper.makeContactFromUserInfo(user), ItemTypes.FRIEND));
            }
        }

        return res;
    }

    public final void selectItem(int position) {
        AbsContactItem item = (AbsContactItem) getItem(position);
        if (item != null && item instanceof ContactItem) {
            selects.add(((ContactItem) item).getContact().getContactId());
        }
        notifyDataSetChanged();
    }

    public final boolean isSelected(int position) {
        AbsContactItem item = (AbsContactItem) getItem(position);
        if (item != null && item instanceof ContactItem) {
            return selects.contains(((ContactItem) item).getContact().getContactId());
        }
        return false;
    }

    public final void cancelItem(int position) {
        AbsContactItem item = (AbsContactItem) getItem(position);
        if (item != null && item instanceof ContactItem) {
            selects.remove(((ContactItem) item).getContact().getContactId());
        }
        notifyDataSetChanged();
    }

    public final void cancelItem(IContact iContact) {
        selects.remove(iContact.getContactId());
    }
}
