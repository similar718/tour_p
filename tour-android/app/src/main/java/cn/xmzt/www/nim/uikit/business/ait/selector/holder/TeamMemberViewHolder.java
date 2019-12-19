package cn.xmzt.www.nim.uikit.business.ait.selector.holder;

import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.uikit.business.ait.selector.model.AitContactItem;
import cn.xmzt.www.nim.uikit.business.team.helper.TeamHelper;
import cn.xmzt.www.nim.uikit.common.ui.imageview.HeadImageView;
import cn.xmzt.www.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import cn.xmzt.www.intercom.business.session.viewholder.BaseViewHolder;
import cn.xmzt.www.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nimlib.sdk.team.model.TeamMember;

/**
 * Created by hzchenkang on 2017/6/21.
 */

public class TeamMemberViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, AitContactItem<TeamMember>> {

    private HeadImageView headImageView;

    private TextView nameTextView;

    public TeamMemberViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, AitContactItem<TeamMember> data, int position, boolean isScrolling) {
        inflate(holder);
        refresh(data.getModel());
    }

    public void inflate(BaseViewHolder holder) {
        headImageView = holder.getView(R.id.imageViewHeader);
        nameTextView = holder.getView(R.id.textViewName);
    }

    public void refresh(TeamMember member) {
        headImageView.resetImageView();
        nameTextView.setText(TeamHelper.getTeamMemberDisplayName(member.getTid(), member.getAccount()));
        headImageView.loadBuddyAvatar(member.getAccount());
    }
}
