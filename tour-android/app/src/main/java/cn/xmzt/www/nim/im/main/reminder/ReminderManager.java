package cn.xmzt.www.nim.im.main.reminder;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * TAB红点提醒管理器
 * Created by huangjun on 2015/3/18.
 */
public class ReminderManager {
    // callback
    public interface UnreadNumChangedCallback {
        void onUnreadNumChanged(ReminderItem item);
    }

    // singleton
    private static ReminderManager instance;

    public static synchronized ReminderManager getInstance() {
        if (instance == null) {
            instance = new ReminderManager();
        }

        return instance;
    }

    // observers
    private SparseArray<ReminderItem> items = new SparseArray<>();

    private List<UnreadNumChangedCallback> unreadNumChangedCallbacks = new ArrayList<>();

    private ReminderManager() {
        populate(items);
    }

    // interface
    public final void updateSessionUnreadNum(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.SESSION);
    }

    public final void updateSessionDeltaUnreadNum(int delta) {
        updateUnreadMessageNum(delta, true, ReminderId.SESSION);
    }

    public final void updateContactUnreadNum(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.CONTACT);
    }
    public final void updateUnreadMessageNumHome(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.HOME);
    }

    public final void updateUnreadMessageNumRoute(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.ROUTE);
    }

    public final void updateUnreadMessageNumElectric(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.ELECTRIC);
    }

    public final void updateUnreadMessageNumIntercom(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.INTERCOM);
    }

    public final void updateUnreadMessageNumMine(int unreadNum) {
        updateUnreadMessageNum(unreadNum, false, ReminderId.MINE);
    }

    public void registerUnreadNumChangedCallback(UnreadNumChangedCallback cb) {
        if (unreadNumChangedCallbacks.contains(cb)) {
            return;
        }

        unreadNumChangedCallbacks.add(cb);
    }

    public void unregisterUnreadNumChangedCallback(UnreadNumChangedCallback cb) {
        if (!unreadNumChangedCallbacks.contains(cb)) {
            return;
        }

        unreadNumChangedCallbacks.remove(cb);
    }

    // inner
    private final void populate(SparseArray<ReminderItem> items) {
        items.put(ReminderId.SESSION, new ReminderItem(ReminderId.SESSION));
        items.put(ReminderId.CONTACT, new ReminderItem(ReminderId.CONTACT));
        items.put(ReminderId.HOME, new ReminderItem(ReminderId.HOME));
        items.put(ReminderId.ROUTE, new ReminderItem(ReminderId.ROUTE));
        items.put(ReminderId.ELECTRIC, new ReminderItem(ReminderId.ELECTRIC));
        items.put(ReminderId.INTERCOM, new ReminderItem(ReminderId.INTERCOM));
        items.put(ReminderId.MINE, new ReminderItem(ReminderId.MINE));
    }

    private final void updateUnreadMessageNum(int unreadNum, boolean delta, int reminderId) {
        ReminderItem item = items.get(reminderId);
        if (item == null) {
            return;
        }

        int num = item.getUnread();

        // 增量
        if (delta) {
            num = num + unreadNum;
            if (num < 0) {
                num = 0;
            }
        } else {
            num = unreadNum;
        }

        item.setUnread(num);
        item.setIndicator(false);

        for (UnreadNumChangedCallback cb : unreadNumChangedCallbacks) {
            cb.onUnreadNumChanged(item);
        }
    }
}
