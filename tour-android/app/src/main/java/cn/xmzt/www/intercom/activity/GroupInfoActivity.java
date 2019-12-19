package cn.xmzt.www.intercom.activity;

import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGroupInfoBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.GroupInfoViewModel;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/9/12
 * @describe 群资料页
 */

public class GroupInfoActivity extends TourBaseActivity<GroupInfoViewModel,ActivityGroupInfoBinding> {

    public static void start(Context context, String tid, String lineId, int tripId) {
        Intent intent = new Intent();
        intent.putExtra("A",tid);
        intent.setClass(context, GroupInfoActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_info;
    }
    public List<GroupMemberBean> carList=new ArrayList<GroupMemberBean>();//车辆列表
    @Override
    protected GroupInfoViewModel createViewModel() {
        viewModel = new GroupInfoViewModel();
        viewModel.memberList.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(@Nullable List<GroupMemberBean> result) {
                if(viewModel.selfGroupMemberBean!=null){
                    if(viewModel.selfGroupMemberBean.getRole()==1||viewModel.selfGroupMemberBean.isLeader()){
                        dataBinding.memberList.isQZLD=true;//是群主或者领队
                        dataBinding.carList.isQZLD=true;//是群主或者领队
                    }
                }
//                carList.clear();
//                for(int i=0;i<result.size();i++){
//                    GroupMemberBean member=result.get(i);
//                    if(member.getDriver()){
//                        carList.add(member);
//                    }
//                }
//                if (carList.size() ==0){
//                    dataBinding.tvNoCar.setVisibility(View.VISIBLE);
//                }else {
//                    dataBinding.tvNoCar.setVisibility(View.GONE);
//                }
                if(dataBinding.memberList.isQZLD){
                    if (result.size() >13){
                        dataBinding.tvAllMember.setVisibility(View.VISIBLE);
                        dataBinding.memberList.setDatas(result.subList(0,13));
                    } else {
                        dataBinding.tvAllMember.setVisibility(View.GONE);
                        dataBinding.memberList.setDatas(result);
                    }
//                    if (carList.size() >14){
//                        dataBinding.carList.setDatas(carList.subList(0,14));
//                        dataBinding.tvAllCar.setVisibility(View.VISIBLE);
//                    } else {
//                        dataBinding.tvAllCar.setVisibility(View.GONE);
//                        dataBinding.carList.setDatas(carList);
//                    }
                }else {
                    if (result.size() >14){
                        dataBinding.tvAllMember.setVisibility(View.VISIBLE);
                        dataBinding.memberList.setDatas(result.subList(0,14));
                    } else {
                        dataBinding.tvAllMember.setVisibility(View.GONE);
                        dataBinding.memberList.setDatas(result);
                    }
//                    if (carList.size() >15){
////                        dataBinding.carList.setDatas(carList.subList(0,15));
////                        dataBinding.tvAllCar.setVisibility(View.VISIBLE);
////                    } else {
////                        dataBinding.tvAllCar.setVisibility(View.GONE);
////                        dataBinding.carList.setDatas(carList);
////                    }
                }
            }
        });
        viewModel.vehicleInfo.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(List<GroupMemberBean> groupMemberBeans) { // 车牌号信息获取
                if(viewModel.selfGroupMemberBean!=null&&viewModel.selfGroupMemberBean.getRole()==1||viewModel.selfGroupMemberBean.isLeader()){
                    dataBinding.memberList.isQZLD=true;//是群主或者领队
                    dataBinding.carList.isQZLD=true;//是群主或者领队
                }
                if (groupMemberBeans.size() == 0&&!dataBinding.carList.isQZLD){
                    dataBinding.tvNoCar.setVisibility(View.VISIBLE);
                    return;
                }
                dataBinding.tvNoCar.setVisibility(View.GONE);
                carList.clear();
                carList = new ArrayList<>(groupMemberBeans);

                if(dataBinding.memberList.isQZLD){
                    if (carList.size() >14){
                        dataBinding.carList.setDatas(carList.subList(0,14));
                        dataBinding.tvAllCar.setVisibility(View.VISIBLE);
                    } else {
                        dataBinding.tvAllCar.setVisibility(View.GONE);
                        dataBinding.carList.setDatas(carList);
                    }
                }else {
                    if (carList.size() >15){
                        dataBinding.carList.setDatas(carList.subList(0,15));
                        dataBinding.tvAllCar.setVisibility(View.VISIBLE);
                    } else {
                        dataBinding.tvAllCar.setVisibility(View.GONE);
                        dataBinding.carList.setDatas(carList);
                    }
                }
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("A");
        dataBinding.setModel(viewModel);
        dataBinding.carList.isDriver=true;
        viewModel.setActivity(this);
        viewModel.getGroupRoomInfo();
        EventBus.getDefault().register(this);
        dataBinding.memberList.setItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId()==R.id.itemLayout){
                    //跳到成员详情
                    GroupMemberBean mGroupMemberBean=dataBinding.memberList.getItem(position);
                    Intent intent=new Intent(GroupInfoActivity.this, GroupPersonalInfoActivity.class);
                    intent.putExtra("userId",""+mGroupMemberBean.getUserId());
                    intent.putExtra("teamId",viewModel.groupId);
                    startActivity(intent);
                }else if(view.getId()==R.id.iv_add){
                    //分享行程
                    if(viewModel.mGroupRoomBean!=null){
                        String url="https://g.xmzt.cn/g/g?t=5&i="+viewModel.mGroupRoomBean.getGroupId()+"&p="+ TourApplication.getRefCode();
                        ShareFunction.getInstance().showWebWeChatShareAction(GroupInfoActivity.this, "我发现了一个不错的自驾游线路，想邀请你同游",viewModel.mGroupRoomBean.getmPhotoUrl(),
                                viewModel.mGroupRoomBean.getIntro(),url);
                    }
                }else if(view.getId()==R.id.iv_minus){
                    //跳到移除成员
                    Intent intent=new Intent(GroupInfoActivity.this,GroupMemberDelActivity.class);
                    intent.putExtra("A",""+viewModel.groupId);
                    startActivity(intent);
                    /*
                    Intent intent = new Intent(GroupInfoActivity.this, GroupMemberManageActivity.class);
                    intent.putExtra("A",viewModel.groupId);
                    if(viewModel.selfGroupMemberBean!=null){
                        intent.putExtra("B",viewModel.selfGroupMemberBean.getUserId());
                        intent.putExtra("C",viewModel.selfGroupMemberBean.getRole());
                    }
                    intent.putExtra("D",viewModel.mGroupRoomBean.getGroupTitle());
                    intent.putExtra("E",true);
                    startActivity(intent);*/
                }
            }
        });
        dataBinding.carList.setItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId()==R.id.itemLayout){
                    // 车辆的点击事件
                    if (dataBinding.carList.isQZLD){
                        mSetLicencePlate = "";
                        // 去设置司机 或者更换司机界面
                        GroupMemberBean mGroupMemberBean=dataBinding.carList.getItem(position);
//                        if (mGroupMemberBean.getUserId() == 0) { // 设置司机
//                            mSetLicencePlate = mGroupMemberBean.getLicencePlate();
//                            //选择司机
//                            Intent intent = new Intent(GroupInfoActivity.this, SelectDriverActivity.class);
//                            intent.putExtra("A", viewModel.groupId);
//                            startActivity(intent);
//                        } else {
                            //更换司机
                            Intent intent = new Intent(GroupInfoActivity.this, TransferVehicleActivity.class);
                            intent.putExtra("A",viewModel.groupId);
                            intent.putExtra("B",mGroupMemberBean.getUserId());
                            intent.putExtra("C",0); //0自己更换司机 1转让车辆
                            intent.putExtra("D",mGroupMemberBean.getLicencePlate());
                            startActivity(intent);
//                        }
                    }
                }else if(view.getId()==R.id.iv_add){
                    //跳到添加车辆
                    Intent intent = new Intent(GroupInfoActivity.this, AddCarActivity.class);
                    intent.putExtra("A",viewModel.groupId);
                    startActivity(intent);
                }
            }
        });

    }

    private String mSetLicencePlate = "";

    @Subscribe
    public void onSelectDriver(GroupMemberBean driverBean) {
        if (!TextUtils.isEmpty(mSetLicencePlate)){
            viewModel.addDriver(mSetLicencePlate,driverBean.getUserId());
        }
    }

    @Subscribe
    public void onGroupManageEvent(GroupManageEvent mGroupManageEvent) {
        int type= mGroupManageEvent.getType();
        viewModel.getGroupRoomInfo();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
