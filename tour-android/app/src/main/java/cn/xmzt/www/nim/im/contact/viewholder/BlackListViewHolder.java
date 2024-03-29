package cn.xmzt.www.nim.im.contact.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.im.contact.activity.BlackListAdapter;
import cn.xmzt.www.nim.uikit.business.uinfo.UserInfoHelper;
import cn.xmzt.www.nim.uikit.common.adapter.TViewHolder;
import cn.xmzt.www.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

/**
 * Created by huangjun on 2015/9/22.
 */
public class BlackListViewHolder extends TViewHolder {
    private HeadImageView headImageView;
    private TextView accountText;
    private Button removeBtn;
    private UserInfo user;

    @Override
    protected int getResId() {
        return R.layout.black_list_item;
    }

    @Override
    protected void inflate() {
        headImageView = findView(R.id.head_image);
        accountText = findView(R.id.account);
        removeBtn = findView(R.id.remove);
    }

    @Override
    protected void refresh(Object item) {
        user = (NimUserInfo) item;

        accountText.setText(UserInfoHelper.getUserDisplayName(user.getAccount()));
        headImageView.loadBuddyAvatar(user.getAccount());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAdapter().getEventListener().onItemClick(user);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAdapter().getEventListener().onRemove(user);
            }
        });
    }

    protected final BlackListAdapter getAdapter() {
        return (BlackListAdapter) adapter;
    }
}
