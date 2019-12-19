package cn.xmzt.www.nim.im.session.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.activity.GroupPersonalInfoActivity;
import cn.xmzt.www.nim.im.session.adapter.AckMsgDetailAdapter;
import cn.xmzt.www.nim.uikit.business.team.helper.TeamHelper;
import cn.xmzt.www.nim.uikit.common.adapter.TViewHolder;
import cn.xmzt.www.nim.uikit.common.ui.imageview.HeadImageView;

public class AckMsgDetailHolder extends TViewHolder {

    private HeadImageView headImageView;

    private TextView nameTextView;

    private AckMsgDetailAdapter.AckMsgDetailItem memberItem;

    protected AckMsgDetailAdapter getAdapter() {
        return (AckMsgDetailAdapter) super.getAdapter();
    }

    @Override
    protected int getResId() {
        return R.layout.ack_msg_detail_item;
    }

    @Override
    protected void inflate() {
        headImageView = view.findViewById(R.id.imageViewHeader);
        nameTextView = view.findViewById(R.id.textViewName);
    }

    @Override
    protected void refresh(Object item) {
        memberItem = (AckMsgDetailAdapter.AckMsgDetailItem) item;
        headImageView.resetImageView();

        refreshTeamMember(memberItem);
    }

    private void refreshTeamMember(final AckMsgDetailAdapter.AckMsgDetailItem item) {
        nameTextView.setText(TeamHelper.getTeamMemberDisplayName(item.getTid(), item.getAccount()));
        headImageView.loadBuddyAvatar(item.getAccount());
        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开群成员信息详细页面
                Intent intent = new Intent((Activity) context, GroupPersonalInfoActivity.class);
                intent.putExtra("teamId", item.getAccount());
                // TODO: 2019/9/7 还需要被点击的用户id
                intent.putExtra("userId", item.getTid());
                ((Activity)context).startActivity(intent);

//                AdvancedTeamMemberInfoActivity.startActivityForResult((Activity) context, item.getAccount(), item.getTid());

            }
        });

    }
}
