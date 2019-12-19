package cn.xmzt.www.intercom.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGroupCarListBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.GroupCarViewModel;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * @describe 群车辆列表
 */
public class GroupCarListActivity extends TourBaseActivity<GroupCarViewModel, ActivityGroupCarListBinding>{
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_car_list;
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
        EventBus.getDefault().register(this);
        dataBinding.setActivity(this);
        viewModel.getGroupCarListVehicle(groupId);
        dataBinding.recyclerView.isQZLD=isQZLD;
        dataBinding.recyclerView.isDriver=true;
        dataBinding.recyclerView.setItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId()==R.id.itemLayout){
                    if (dataBinding.recyclerView.isQZLD) {
                        mSetLicencePlate = "";
                        //跳到车辆详情
                        GroupMemberBean mGroupMemberBean = dataBinding.recyclerView.getItem(position);
//                        if (mGroupMemberBean.getUserId() == 0) { // 设置司机
//                            mSetLicencePlate = mGroupMemberBean.getLicencePlate();
//                            //选择司机
//                            Intent intent = new Intent(GroupCarListActivity.this, SelectDriverActivity.class);
//                            intent.putExtra("A", groupId);
//                            startActivity(intent);
//                        } else {
                            //更换司机
                            Intent intent = new Intent(GroupCarListActivity.this, TransferVehicleActivity.class);
                            intent.putExtra("A", groupId);
                            intent.putExtra("B", mGroupMemberBean.getUserId());
                            intent.putExtra("C", 0); //0自己更换司机 1转让车辆
                            intent.putExtra("D", mGroupMemberBean.getLicencePlate());
                            startActivity(intent);
//                        }
                    }
                }else if(view.getId()==R.id.iv_add){
                    //跳到添加车辆
                    Intent intent = new Intent(GroupCarListActivity.this, AddCarActivity.class);
                    intent.putExtra("A",groupId);
                    startActivity(intent);
                }
            }
        });
    }
    @Subscribe
    public void onGroupManageEvent(GroupManageEvent mGroupManageEvent) {
        viewModel.getGroupCarListVehicle(groupId);
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

    private String mSetLicencePlate = "";

    @Subscribe
    public void onSelectDriver(GroupMemberBean driverBean) {
        if (!TextUtils.isEmpty(mSetLicencePlate)){
            viewModel.addDriver(groupId,mSetLicencePlate,driverBean.getUserId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
