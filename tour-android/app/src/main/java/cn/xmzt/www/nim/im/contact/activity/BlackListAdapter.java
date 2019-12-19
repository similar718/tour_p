package cn.xmzt.www.nim.im.contact.activity;

import android.content.Context;

import cn.xmzt.www.nim.uikit.common.adapter.TAdapter;
import cn.xmzt.www.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import java.util.List;

/**
 * Created by huangjun on 2015/8/12.
 */
public class BlackListAdapter extends TAdapter<UserInfo> {

    BlackListAdapter(Context context, List<UserInfo> items, TAdapterDelegate delegate, ViewHolderEventListener
            viewHolderEventListener) {
        super(context, items, delegate);

        this.viewHolderEventListener = viewHolderEventListener;
    }

    public interface ViewHolderEventListener {
        void onItemClick(UserInfo user);

        void onRemove(UserInfo user);
    }

    private ViewHolderEventListener viewHolderEventListener;

    public ViewHolderEventListener getEventListener() {
        return viewHolderEventListener;
    }
}
