package cn.xmzt.www.nim.uikit.business.contact.core.item;

import android.text.TextUtils;

import cn.xmzt.www.nim.uikit.business.contact.core.model.ContactGroupStrategy;
import cn.xmzt.www.nim.uikit.business.contact.core.model.IContact;
import cn.xmzt.www.nim.uikit.business.contact.core.query.TextComparator;

public class ContactItem extends AbsContactItem implements Comparable<ContactItem> {
    private final IContact contact;

    private final int dataItemType;

    public ContactItem(IContact contact, int type) {
        this.contact = contact;
        this.dataItemType = type;
    }

    public IContact getContact() {
        return contact;
    }

    @Override
    public int getItemType() {
        return dataItemType;
    }

    @Override
    public int compareTo(ContactItem item) {
        // TYPE
        int compare = compareType(item);
        if (compare != 0) {
            return compare;
        } else {
            return TextComparator.compareIgnoreCase(getCompare(), item.getCompare());
        }
    }

    @Override
    public String belongsGroup() {
        IContact contact = getContact();
        if (contact == null) {
            return ContactGroupStrategy.GROUP_NULL;
        }

        String group = TextComparator.getLeadingUp(getCompare());
        return !TextUtils.isEmpty(group) ? group : ContactGroupStrategy.GROUP_SHARP;
    }

    private String getCompare() {
        IContact contact = getContact();
        return contact != null ? contact.getDisplayName() : null;
    }
}
