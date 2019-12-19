package cn.xmzt.www.intercom.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGroupMemberCarListBinding;
import cn.xmzt.www.intercom.adapter.GroupMemberCarListAdapter;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.vm.GroupMemberManageViewModel;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * @describe 群成员列表
 */
public class GroupMemberCarListActivity extends TourBaseActivity<GroupMemberManageViewModel, ActivityGroupMemberCarListBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_member_car_list;
    }

    @Override
    protected GroupMemberManageViewModel createViewModel() {
        viewModel = new GroupMemberManageViewModel();
        viewModel.memberList.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(@Nullable List<GroupMemberBean> result) {
                List<GroupMemberBean> result0 = new ArrayList<>();
                List<GroupMemberBean> result1 = new ArrayList<>();
                List<GroupMemberBean> result2 = new ArrayList<>();
                if (!mIsCar) { // 人员
                    for (int i = 0; i < result.size(); i++) {
                        GroupMemberBean member = result.get(i);
                        if (member.getRole() == 1) {
                            result0.add(member);
                        } else if (member.isLeader()) {
                            result1.add(member);
                        } else {
                            result2.add(member);
                        }
                    }
                } else { // 车辆 司机
                    for (int i = 0; i < result.size(); i++) {
                        GroupMemberBean member = result.get(i);
                        if (member.getRole() == 1 && member.getDriver()) {
                            result0.add(member);
                        } else if (member.isLeader()&& member.getDriver()) {
                            result1.add(member);
                        } else if (member.getDriver()){
                            result2.add(member);
                        }
                    }
                }
                adapter.setDatas(result0,result1,result2);
            }
        });
        return viewModel;
    }
    GroupMemberCarListAdapter adapter =null;
    GridLayoutManager manager=null;
    private boolean mIsCar = false;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("A");
        mIsCar = intent.getBooleanExtra("isCar",false);
        dataBinding.setActivity(this);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(5);
        adapter = new GroupMemberCarListAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getMemberList();
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
    public void onItemClick(View view, int position) {
        GroupMemberBean obj=adapter.getItem(position);
        switch (view.getId()) {
            case R.id.itemLayout: {
                //成员信息
                Intent intent=new Intent(GroupMemberCarListActivity.this,GroupPersonalInfoActivity.class);
                intent.putExtra("userId",""+obj.getUserId());
                intent.putExtra("teamId",viewModel.groupId);
                startActivity(intent);
                break;
            }
            case R.id.iv_add:{
                //添加成员
                Intent intent=new Intent(GroupMemberCarListActivity.this,GroupQRcodeActivity.class);
                intent.putExtra("teamId",""+viewModel.groupId);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
