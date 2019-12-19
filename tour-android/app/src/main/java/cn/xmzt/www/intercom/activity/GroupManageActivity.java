package cn.xmzt.www.intercom.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGroupManageBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.vm.GroupManageViewModel;

/**
 * @describe 群管理
 */
public class GroupManageActivity extends TourBaseActivity<GroupManageViewModel, ActivityGroupManageBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_manage;
    }
    private GroupMemberBean selfGroupMemberBean=null;
    @Override
    protected GroupManageViewModel createViewModel() {
        viewModel = new GroupManageViewModel();
        viewModel.selfGroupMemberBean.observe(this, new Observer<GroupMemberBean>() {
            @Override
            public void onChanged(@Nullable GroupMemberBean result) {
                selfGroupMemberBean=result;
            }
        });
        return viewModel;
    }
    private String groupName="";
    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("A");
        groupName=intent.getStringExtra("B");
        dataBinding.setActivity(this);
        viewModel.getGroupUser();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_member:{
                //群成人管理
                Intent intent = new Intent(this, GroupMemberManageActivity.class);
                intent.putExtra("A",viewModel.groupId);
                if(selfGroupMemberBean!=null){
                    intent.putExtra("B",selfGroupMemberBean.getUserId());
                    intent.putExtra("C",selfGroupMemberBean.getRole());
                    intent.putExtra("leader",selfGroupMemberBean.isLeader());
                }
                intent.putExtra("D",groupName);
                startActivity(intent);
                break;
            }
            case R.id.tv_car:{
                //群车辆管理
                Intent intent = new Intent(this, GroupCarManageActivity.class);
                intent.putExtra("A",viewModel.groupId);
                if(selfGroupMemberBean!=null){
                    intent.putExtra("B",selfGroupMemberBean.getUserId());
                    intent.putExtra("C",selfGroupMemberBean.getRole());
                    intent.putExtra("D",selfGroupMemberBean.getLicencePlate());
                }
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}
