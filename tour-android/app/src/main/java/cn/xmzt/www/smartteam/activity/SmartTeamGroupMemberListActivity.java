package cn.xmzt.www.smartteam.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGroupMemberListBinding;
import cn.xmzt.www.databinding.ActivitySmartTeamGroupMemberListBinding;
import cn.xmzt.www.dialog.InviteFriendsToJoinUsDialog;
import cn.xmzt.www.intercom.activity.GroupMemberDelActivity;
import cn.xmzt.www.intercom.activity.GroupPersonalInfoActivity;
import cn.xmzt.www.intercom.activity.GroupQRcodeActivity;
import cn.xmzt.www.intercom.adapter.GroupMemberListAdapter;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.vm.GroupMemberManageViewModel;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * @describe 智能组队群成员列表
 */
public class SmartTeamGroupMemberListActivity extends TourBaseActivity<GroupMemberManageViewModel, ActivitySmartTeamGroupMemberListBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_smart_team_group_member_list;
    }

    @Override
    protected GroupMemberManageViewModel createViewModel() {
        viewModel = new GroupMemberManageViewModel();
        viewModel.memberList.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(@Nullable List<GroupMemberBean> result) {
                dataBinding.memberList.setDatas(result);
            }
        });
        return viewModel;
    }
    private String nickname="";
    private String teamPwdcard="";
    private String image="";
    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("A");
        nickname=intent.getStringExtra("B");
        teamPwdcard=intent.getStringExtra("C");
        image=intent.getStringExtra("D");
        dataBinding.setActivity(this);
        dataBinding.memberList.isDriver=false;
        viewModel.getMemberList();
        dataBinding.memberList.setItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId()==R.id.itemLayout){
                    //跳到成员详情
                    GroupMemberBean mGroupMemberBean=dataBinding.memberList.getItem(position);
                    Intent intent=new Intent(SmartTeamGroupMemberListActivity.this, GroupPersonalInfoActivity.class);
                    intent.putExtra("userId",""+mGroupMemberBean.getUserId());
                    intent.putExtra("teamId",viewModel.groupId);
                    startActivity(intent);
                }else if(view.getId()==R.id.iv_add){
                    //分享邀请好友加入群
                    if(teamPwdcard!=null){
                        showInviteFriendsToJoinUsDialog();
                    }
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private InviteFriendsToJoinUsDialog inviteFriendsToJoinUsDialog;
    private void showInviteFriendsToJoinUsDialog(){
        if(inviteFriendsToJoinUsDialog==null){
            inviteFriendsToJoinUsDialog = new InviteFriendsToJoinUsDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inviteFriendsToJoinUsDialog.cancel();
                    if(view.getId()==R.id.dialog_invite_tv){
                        String tettle= nickname+"邀请你加入自驾队伍";
                        String description= "口令"+teamPwdcard+"，打开小马在途app，找到【工具菜单>智能出行>加入队伍】，然后输入下方口令，即可加入";
                        String url = "https://weixin.xmzt.cn/#/pages/groupInvite/index?groupId="+viewModel.groupId+"&refCode="+TourApplication.getUser().getRefCode()+"&refName="+nickname;
                        ShareFunction.getInstance().shareWeb(SmartTeamGroupMemberListActivity.this
                                , tettle, image, description, url,
                                ShareFunction.SHARE_WEIXIN);
                    }
                }
            });
        }
        inviteFriendsToJoinUsDialog.setViewData(teamPwdcard);
        inviteFriendsToJoinUsDialog.show();
    }
}
