package cn.xmzt.www.smartteam.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivitySmartTeamGroupInfoBinding;
import cn.xmzt.www.dialog.GroupAddDelayDaysDialog;
import cn.xmzt.www.dialog.InviteFriendsToJoinUsDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.activity.AddCarActivity;
import cn.xmzt.www.intercom.activity.GroupCarListActivity;
import cn.xmzt.www.intercom.activity.GroupMemberDelActivity;
import cn.xmzt.www.intercom.activity.GroupMemberListActivity;
import cn.xmzt.www.intercom.activity.GroupPersonalInfoActivity;
import cn.xmzt.www.intercom.activity.MessageRemindActivity;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.mine.event.UpdateVehicleEvent;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.smartteam.bean.SmartTeamInfoBean;
import cn.xmzt.www.smartteam.vm.SmartTeamGroupInfoViewModel;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * @describe 智能组队的群详情
 */

public class SmartTeamGroupInfoActivity extends TourBaseActivity<SmartTeamGroupInfoViewModel, ActivitySmartTeamGroupInfoBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_smart_team_group_info;
    }
    public List<GroupMemberBean> carList=new ArrayList<GroupMemberBean>();//车辆列表
    private boolean isLeader=false;
    @Override
    protected SmartTeamGroupInfoViewModel createViewModel() {
        viewModel = new SmartTeamGroupInfoViewModel();
        viewModel.teamInfoLiveData.observe(this, new Observer<SmartTeamInfoBean>() {
            @Override
            public void onChanged(@Nullable SmartTeamInfoBean result) {
                List<GroupMemberBean> memberList = result.getGroupMemberVOS();
                if (memberList == null) {
                    memberList = new ArrayList<>();
                }
                dataBinding.topC.setText("对讲聊天("+memberList.size()+")");

                dataBinding.tvCarName.setText(result.getLicencePlate());
                dataBinding.tvGroupName.setText(result.getGroupTitle());
                dataBinding.tvMyNikename.setText(result.getNickname());
                dataBinding.tvDissolution.setText(result.getDissolveDate());
                isLeader=result.isLeader();
                if(isLeader){
                    dataBinding.memberList.isLD=true;//是群主或者领队
                    dataBinding.carList.isLD=true;//是群主或者领队
                    dataBinding.llGroupName.setEnabled(true);
                    dataBinding.dissolutionLayout.setEnabled(true);
                    viewModel.isDissolutionShow.set(true);
                    dataBinding.tvLogoutGroup.setText("解散队伍");
                }else {
                    dataBinding.llGroupName.setEnabled(false);
                    dataBinding.dissolutionLayout.setEnabled(false);
                    viewModel.isDissolutionShow.set(false);
                    dataBinding.tvLogoutGroup.setText("退出队伍");
                }
                viewModel.isShareLocation.set(result.isShareLocation());
                viewModel.isAutoAnswer.set(result.isAutoplay());

                carList.clear();
                for(int i=0;i<memberList.size();i++){
                    GroupMemberBean member=memberList.get(i);
                    if(member.getDriver()){
                        carList.add(member);
                    }
                }
                if (carList.size() ==0){
                    dataBinding.tvNoCar.setVisibility(View.VISIBLE);
                }else {
                    dataBinding.tvNoCar.setVisibility(View.GONE);
                }
                if(dataBinding.memberList.isLD){
                    if (memberList.size() >13){
                        dataBinding.tvAllMember.setVisibility(View.VISIBLE);
                        dataBinding.memberList.setDatas(memberList.subList(0,13));
                    } else {
                        dataBinding.tvAllMember.setVisibility(View.GONE);
                        dataBinding.memberList.setDatas(memberList);
                    }
                }else {
                    if (memberList.size() >14){
                        dataBinding.tvAllMember.setVisibility(View.VISIBLE);
                        dataBinding.memberList.setDatas(memberList.subList(0,14));
                    } else {
                        dataBinding.tvAllMember.setVisibility(View.GONE);
                        dataBinding.memberList.setDatas(memberList);
                    }
                }
                if (carList.size() >15){
                    dataBinding.carList.setDatas(carList.subList(0,15));
                    dataBinding.tvAllCar.setVisibility(View.VISIBLE);
                } else {
                    dataBinding.tvAllCar.setVisibility(View.GONE);
                    dataBinding.carList.setDatas(carList);
                }
            }
        });
        viewModel.result.observe(this, new Observer<BaseDataBean<String>>() {
            @Override
            public void onChanged(BaseDataBean<String> stringBaseDataBean) {
                // 添加车辆成功
                dataBinding.tvCarName.setText(Constants.mMyCarInfo);
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("groupId");
        dataBinding.setActivity(this);
        dataBinding.setMv(viewModel);
        dataBinding.carList.isDriver=true;
        viewModel.getSmartTeamGroupInfo();
        EventBus.getDefault().register(this);
        dataBinding.memberList.setItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId()==R.id.itemLayout){
                    //跳到成员详情
                    GroupMemberBean mGroupMemberBean=dataBinding.memberList.getItem(position);
                    Intent intent=new Intent(SmartTeamGroupInfoActivity.this, GroupPersonalInfoActivity.class);
                    intent.putExtra("userId",""+mGroupMemberBean.getUserId());
                    intent.putExtra("teamId",viewModel.groupId);
                    startActivity(intent);
                }else if(view.getId()==R.id.iv_add){
                    //分享邀请好友加入群
                    if(viewModel.teamInfo!=null){
                        showInviteFriendsToJoinUsDialog();
                    }
                }else if(view.getId()==R.id.iv_minus){
                    //跳到移除成员
                    Intent intent=new Intent(SmartTeamGroupInfoActivity.this,GroupMemberDelActivity.class);
                    intent.putExtra("A",""+viewModel.groupId);
                    startActivity(intent);
                }
            }
        });
        dataBinding.carList.setItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId()==R.id.itemLayout){
                    //跳到车辆详情
                }else if(view.getId()==R.id.iv_add){
                    //跳到添加车辆
                    Intent intent = new Intent(SmartTeamGroupInfoActivity.this, AddCarActivity.class);
                    intent.putExtra("A",viewModel.groupId);
                    startActivity(intent);
                }
            }
        });

    }
    @Subscribe
    public void onGroupManageEvent(GroupManageEvent mGroupManageEvent) {
        int type= mGroupManageEvent.getType();
        viewModel.getSmartTeamGroupInfo();
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
               onBackPressed();
                break;
            case R.id.tv_all_member: {
                //查看全部成员
                Intent intent = new Intent(this, SmartTeamGroupMemberListActivity.class);
                intent.putExtra("A", viewModel.teamInfo.getGroupId());
                intent.putExtra("B", viewModel.teamInfo.getNickname());
                intent.putExtra("C", viewModel.teamInfo.getTeamPwdcard());
                intent.putExtra("D", viewModel.teamInfo.getImage());
                startActivity(intent);
                break;
            }
            case R.id.tv_all_car: {
                //查看全部车辆
                Intent intent = new Intent(this, SmartTeamGroupCarListActivity.class);
                intent.putExtra("A", viewModel.groupId);
                startActivity(intent);
                break;
            }
            case R.id.ll_my_vehicle:{
                //添加我的车辆
//                Intent intent = new Intent(SmartTeamGroupInfoActivity.this, SmartTeamAddCarActivity.class);
//                intent.putExtra("A",viewModel.groupId);
//                if(viewModel.teamInfo!=null){
//                    intent.putExtra("B",viewModel.teamInfo.getLicencePlate());
//                }
//                startActivity(intent);
                ArrayList<String> list = new ArrayList<>();
                if (!TextUtils.isEmpty(Constants.mMyCarInfo)){
                    list.add(Constants.mMyCarInfo);
                }
//                if(viewModel.teamInfo!=null){
//                    list.add(viewModel.teamInfo.getLicencePlate());
//                }
                IntentManager.getInstance().goCommonVehiclesActivity(SmartTeamGroupInfoActivity.this,true,false,list);

                break;
            }
            case R.id.ll_group_name: {
                //修改队伍名称
                if(viewModel.teamInfo!=null){
                    IntentManager.getInstance().goGroupEditorActivity(this,viewModel.teamInfo.getNickname(), 3, true, viewModel.teamInfo.getGroupTitle(), viewModel.teamInfo.getGroupId());
                }
                break;
            }
            case R.id.ll_my_group_nikename: {
                //修改我在本群昵称
                if(viewModel.teamInfo!=null){
                    IntentManager.getInstance().goGroupEditorActivity(this, viewModel.teamInfo.getNickname(),2, true, viewModel.teamInfo.getNickname(), viewModel.teamInfo.getGroupId());
                }
                break;
            }
            case R.id.dissolution_layout: {
                //自动延长解散群时间
                showGroupAddDelayDaysDialog();
                break;
            }
            case R.id.iv_automatic_answer:{
                //自动接听开关
                viewModel.getIsShareLocationOrISAutoplay(viewModel.groupId,!viewModel.teamInfo.isAutoplay(),viewModel.teamInfo.isShareLocation());
                break;
            }
            case R.id.ll_message: {
                //消息提醒
                Intent intent1 = new Intent(this, MessageRemindActivity.class);
                intent1.putExtra("teamId", viewModel.teamInfo.getGroupId());
                startActivity(intent1);
                break;
            }
            case R.id.iv_share_location: {
                //分享位置开关
                viewModel.getIsShareLocationOrISAutoplay(viewModel.groupId,viewModel.teamInfo.isAutoplay(),!viewModel.teamInfo.isShareLocation());
                break;
            }
            case R.id.tv_logout_group: {
                //退出或者解散队伍
                showHintDialog();
                break;
            }
        }
    }
    private TextTitleDialog hintDialog;
    private void showHintDialog(){
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    viewModel.removeTeam();
                }
            });
        }
        if(isLeader){
            hintDialog.setTitle("您确定要解散队伍？");
        }else {
            hintDialog.setTitle("您确定要退出队伍？");
        }
        hintDialog.show();;
    }
    private GroupAddDelayDaysDialog groupAddDelayDaysDialog;
    private void showGroupAddDelayDaysDialog(){
        if(groupAddDelayDaysDialog==null){
            groupAddDelayDaysDialog = new GroupAddDelayDaysDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.tv_cancel:
                            break;
                        case R.id.tv_add_5:
                            viewModel.getExtendGroupDissolveDate(5);
                            break;
                        case R.id.tv_add_10:
                            viewModel.getExtendGroupDissolveDate(10);
                            break;
                        default:
                            break;
                    }
                    groupAddDelayDaysDialog.cancel();
                }
            });
        }
        groupAddDelayDaysDialog.show();;
    }

    private InviteFriendsToJoinUsDialog inviteFriendsToJoinUsDialog;
    private void showInviteFriendsToJoinUsDialog(){
        if(inviteFriendsToJoinUsDialog==null){
            inviteFriendsToJoinUsDialog = new InviteFriendsToJoinUsDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inviteFriendsToJoinUsDialog.cancel();
                    if(view.getId()==R.id.dialog_invite_tv){
                        String tettle= viewModel.teamInfo.getNickname()+"邀请你加入自驾队伍";
                        String description= "口令"+viewModel.teamInfo.getTeamPwdcard()+"，打开小马在途app，找到【工具菜单>智能出行>加入队伍】，然后输入下方口令，即可加入";
                        String url = "https://weixin.xmzt.cn/#/pages/groupInvite/index?groupId="+viewModel.groupId+"&refCode="+TourApplication.getUser().getRefCode()+"&refName="+viewModel.teamInfo.getNickname();
                        ShareFunction.getInstance().shareWeb(SmartTeamGroupInfoActivity.this
                                , tettle, viewModel.teamInfo.getImage(), description, url,
                                ShareFunction.SHARE_WEIXIN);
                    }
                }
            });
        }
        inviteFriendsToJoinUsDialog.setViewData(viewModel.teamInfo.getTeamPwdcard());
        inviteFriendsToJoinUsDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LocationShareStatusEventBus(UpdateVehicleEvent event) { // 添加车辆
        if (event.getType() == 2){ // 添加车辆信息 调用车辆添加接口
            String data = viewModel.teamInfo != null ? viewModel.teamInfo.getLicencePlate() : null;
            viewModel.smartTeamAddOrUpdtOrDelDriver(TextUtils.isEmpty(data) ? 1 : 2,viewModel.groupId,event.getLicencePlate().get(0).getPlateNumber());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
