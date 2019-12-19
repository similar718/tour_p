package cn.xmzt.www.smartteam.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGroupCarListBinding;
import cn.xmzt.www.databinding.ActivitySmartTeamGroupCarListBinding;
import cn.xmzt.www.intercom.activity.AddCarActivity;
import cn.xmzt.www.intercom.activity.GroupMemberDelActivity;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.GroupCarViewModel;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * @describe 智能组队群车辆列表
 */
public class SmartTeamGroupCarListActivity extends TourBaseActivity<GroupCarViewModel, ActivitySmartTeamGroupCarListBinding>{
    @Override
    protected int setLayoutId() {
        return R.layout.activity_smart_team_group_car_list;
    }

    @Override
    protected GroupCarViewModel createViewModel() {
        viewModel = new GroupCarViewModel();
        viewModel.carList.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(@Nullable List<GroupMemberBean> result) {
                dataBinding.recyclerView.setDatas(result);
            }
        });
        return viewModel;
    }
    private String groupId;
    private boolean isQZLD=false;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        groupId=intent.getStringExtra("A");
        isQZLD=intent.getBooleanExtra("B",false);
        dataBinding.setActivity(this);
        dataBinding.recyclerView.isLD=isQZLD;
        dataBinding.recyclerView.isDriver=true;
        viewModel.getGroupCarList(groupId);
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
}
