package cn.xmzt.www.intercom.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityGroupMemberManageBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.adapter.GroupMemberAdapter;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.GroupMemberManageViewModel;
import cn.xmzt.www.popup.MemberEditPopupWindow;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe 群成员管理
 */
public class GroupMemberManageActivity extends TourBaseActivity<GroupMemberManageViewModel, ActivityGroupMemberManageBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_member_manage;
    }

    @Override
    protected GroupMemberManageViewModel createViewModel() {
        viewModel = new GroupMemberManageViewModel();
        viewModel.result.observe(this, new Observer<BaseDataBean<Object>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<Object> result) {
                viewModel.getMemberList();
                EventBus.getDefault().post(new GroupManageEvent(2));//提示上一个页面刷新
            }
        });
        viewModel.memberList.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(@Nullable List<GroupMemberBean> result) {
                List<GroupMemberBean> result0=new ArrayList<>();
                List<GroupMemberBean> result1=new ArrayList<>();
                List<GroupMemberBean> result2=new ArrayList<>();
                for(int i=0;i<result.size();i++){
                    GroupMemberBean member=result.get(i);
                    if(member.getRole()==1){
                        result0.add(member);
                    }else if(member.isLeader()){
                        result1.add(member);
                    }else {
                        result2.add(member);
                    }
                }
                adapter.setDatas(result0,result1,result2);
                if (adapter.getItemCount()>0) {
                    dataBinding.tvNoData.setVisibility(View.GONE);
                    dataBinding.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    dataBinding.tvNoData.setVisibility(View.VISIBLE);
                    dataBinding.recyclerView.setVisibility(View.GONE);
                }
            }
        });
        return viewModel;
    }
    GroupMemberAdapter adapter =null;
    GridLayoutManager manager=null;
    private int selfUserId=0;//自己的id
    private int selfRole=0;//自己在群组中的角色
    private boolean selfLeader=false;//自己在群组中是否是领队
    private boolean isRemoveMember=false;//是否是移除成员
    private String groupName="";
    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("A");
        selfUserId=intent.getIntExtra("B",0);
        selfRole=intent.getIntExtra("C",0);
        selfLeader=intent.getBooleanExtra("leader",false);
        groupName=intent.getStringExtra("D");
        isRemoveMember=intent.getBooleanExtra("E",false);
        dataBinding.setActivity(this);
        EventBus.getDefault().register(this);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new GroupMemberAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getMemberList();
    }
    @Subscribe
    public void onGroupManageEvent(GroupManageEvent mGroupManageEvent) {
        int type= mGroupManageEvent.getType();
        if(type==11){
            adapter.addData(mGroupManageEvent.getmGroupMemberBean());
        }else if(type==2){
            viewModel.getMemberList();
        }
    }
    private boolean isAddMember=false;
    @Override
    protected void onResume() {
        super.onResume();
        if(isAddMember){//是否是添加成员二维码界面返回
            viewModel.getMemberList();
            isAddMember=false;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.editLayout:
                showCarEditMenu();
                break;
            default:
                break;
        }
    }
    private MemberEditPopupWindow memberEditMenu=null;
    private void showCarEditMenu(){
        if(memberEditMenu==null){
            memberEditMenu=new MemberEditPopupWindow(this);
            memberEditMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.tv_add){
                        isAddMember=true;
                        //添加成员
                        Intent intent=new Intent(GroupMemberManageActivity.this,GroupQRcodeActivity.class);
                        intent.putExtra("name",groupName);
                        intent.putExtra("teamId",""+viewModel.groupId);
                        startActivity(intent);
                    }else if(v.getId()==R.id.tv_remove){
                        Intent intent=new Intent(GroupMemberManageActivity.this,GroupMemberDelActivity.class);
                        intent.putExtra("A",""+viewModel.groupId);
                        startActivity(intent);
                    }
                    memberEditMenu.dismiss();
                }
            });
        }
        memberEditMenu.showAsDropDown(dataBinding.ivEdit);
    }
    private GroupMemberBean editGroupMemberBean=null;
    private TextTitleDialog hintDialog;
    private void showHintDialog(GroupMemberBean mGroupMemberBean){
        editGroupMemberBean=mGroupMemberBean;
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    viewModel.updtDriverLeaderInfo(viewModel.groupId,!editGroupMemberBean.isLeader(),editGroupMemberBean.getUserId());
                }
            });
        }
        if(editGroupMemberBean.isLeader()){
            hintDialog.setTitle("确定取消这位成员领队身份？");
        }else {
            hintDialog.setTitle("确定将这位成员设为领队？");
        }
        hintDialog.show();;
    }
    @Override
    public void onItemClick(View view, int position) {
        Object obj=adapter.getItem(position);
        switch (view.getId()) {
            case R.id.iv_edit:
                //编辑成员
                if(obj instanceof GroupMemberBean){
                    GroupMemberBean memberBean= (GroupMemberBean) obj;
                    showHintDialog(memberBean);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
