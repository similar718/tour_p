package cn.xmzt.www.nim.uikit.business.team.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.nim.uikit.business.team.adapter.TeamMemberAdapter;
import cn.xmzt.www.nim.uikit.business.team.helper.TeamHelper;
import cn.xmzt.www.nim.uikit.common.adapter.TViewHolder;
import cn.xmzt.www.nim.uikit.common.ui.imageview.HeadImageView;

public class TeamMemberHolder extends TViewHolder {

    public interface TeamMemberHolderEventListener {
        void onHeadImageViewClick(String account, String userId);
    }

    protected TeamMemberHolderEventListener teamMemberHolderEventListener;

    public void setEventListener(TeamMemberHolderEventListener eventListener) {
        this.teamMemberHolderEventListener = eventListener;
    }

    private HeadImageView headImageView;
    private ImageView deleteImageView;
    private TextView nameTextView;
    private TextView roleTextView;
    private TeamMemberAdapter.TeamMemberItem memberItem;

    public final static String admin = "Admin";
    public final static String leader = "Leader";
    public final static String driver = "Driver";
    public final static String normal = "Normal";
    public final static String owner = "Owner";

    protected TeamMemberAdapter getAdapter() {
        return (TeamMemberAdapter) super.getAdapter();
    }

    @Override
    protected int getResId() {
        return R.layout.item_intercom_team_member;
    }

    @Override
    protected void inflate() {
        headImageView = view.findViewById(R.id.imageViewHeader);
        nameTextView = view.findViewById(R.id.textViewName);
        roleTextView = view.findViewById(R.id.tv_team_member_role);
        deleteImageView = view.findViewById(R.id.imageViewDeleteTag);
    }

    @Override
    protected void refresh(Object item) {
        memberItem = (TeamMemberAdapter.TeamMemberItem) item;
        headImageView.resetImageView();
        deleteImageView.setVisibility(View.GONE);

        if (getAdapter().getMode() == TeamMemberAdapter.Mode.NORMAL) {
            view.setVisibility(View.VISIBLE);
            if (memberItem.getTag() == TeamMemberAdapter.TeamMemberItemTag.ADD) {
                // add team member
                headImageView.setBackgroundResource(R.drawable.ic_chat_selector_add);
                //nameTextView.setText(context.getString(R.string.add));
                nameTextView.setText("");
                headImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAdapter().getAddMemberCallback().onAddMember();
                    }
                });
                roleTextView.setVisibility(View.GONE);
            } else if (memberItem.getTag() == TeamMemberAdapter.TeamMemberItemTag.DELETE) {
                // delete team member
                headImageView.setBackgroundResource(R.drawable.ic_chat_selector_delete);
                //nameTextView.setText(context.getString(R.string.remove));
                nameTextView.setText("");
                headImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAdapter().setMode(TeamMemberAdapter.Mode.DELETE);
                        getAdapter().notifyDataSetChanged();
                    }
                });
                roleTextView.setVisibility(View.GONE);
            } else {
                // show team member
                refreshTeamMember(memberItem, false);
            }
        } else if (getAdapter().getMode() == TeamMemberAdapter.Mode.DELETE) {
            if (memberItem.getTag() == TeamMemberAdapter.TeamMemberItemTag.NORMAL) {
                refreshTeamMember(memberItem, true);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    private void refreshTeamMember(final TeamMemberAdapter.TeamMemberItem item, boolean deleteMode) {
        nameTextView.setText(TeamHelper.getTeamMemberDisplayName(item.getTid(), item.getAccount()));
        headImageView.loadBuddyAvatar(item.getAccount());
        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamMemberHolderEventListener != null) {
                    teamMemberHolderEventListener.onHeadImageViewClick(item.getAccount(), item.getGroupMemberBean().getUserId() + "");
                }
            }
        });
        roleTextView.setVisibility(View.GONE);
        if (item.getDesc() != null) {
            if (item.getDesc().equals(admin)) {
                roleTextView.setVisibility(View.VISIBLE);
                roleTextView.setText("群主");
            } else if (item.getDesc().equals(leader)) {
                roleTextView.setVisibility(View.VISIBLE);
                roleTextView.setText("领队");
            } else if (item.getDesc().equals(driver)) {
                roleTextView.setVisibility(View.VISIBLE);
                roleTextView.setText("司机");
            } else if (item.getDesc().equals(normal)) {
                roleTextView.setVisibility(View.GONE);
            }
        } else {
            roleTextView.setVisibility(View.GONE);
        }

        final String account = item.getAccount();
        if (deleteMode && !isSelf(account)) {
            deleteImageView.setVisibility(View.VISIBLE);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAdapter().getRemoveMemberCallback().onRemoveMember(account);
                }
            });
        } else {
            deleteImageView.setVisibility(View.GONE);
        }
    }

    private boolean isSelf(String account) {
        return account.equals(NimUIKit.getAccount());
    }
}
