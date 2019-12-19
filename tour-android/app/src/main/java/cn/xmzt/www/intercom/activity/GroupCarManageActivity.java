package cn.xmzt.www.intercom.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityGroupCarManageBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.adapter.GroupCarAdapter;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.GroupCarViewModel;
import cn.xmzt.www.popup.CarEditPopupWindow;
import cn.xmzt.www.popup.ItemCarEditPopupWindow;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe 群车辆管理
 */
public class GroupCarManageActivity extends TourBaseActivity<GroupCarViewModel, ActivityGroupCarManageBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_car_manage;
    }


    @Override
    protected GroupCarViewModel createViewModel() {
        viewModel = new GroupCarViewModel();
        viewModel.result.observe(this, new Observer<BaseDataBean<String>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<String> result) {
                viewModel.getGroupCarListVehicle(groupId);
            }
        });
        viewModel.carList.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(@Nullable List<GroupMemberBean> result) {
                List<GroupMemberBean> result1=new ArrayList<>();
                List<GroupMemberBean> result2=new ArrayList<>();
                for(int i=0;i<result.size();i++){
                    GroupMemberBean mCarBean=result.get(i);
                    if(mCarBean.isLeader()){
                        result1.add(mCarBean);
                    }else {
                        result2.add(mCarBean);
                    }
                }
                adapter.setDatas(result1,result2);
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
    GroupCarAdapter adapter =null;
    GridLayoutManager manager=null;
    private String groupId="";
    private int selfUserId=0;//自己的id
    private int selfRole=0;//自己在群组中的角色 1是群主 2领队 3司机 12群主、领队 123群主、领队司机
    private String limits="";
    @Override
    protected void initData() {
        Intent intent=getIntent();
        groupId=intent.getStringExtra("A");
        selfUserId=intent.getIntExtra("B",0);
        selfRole=intent.getIntExtra("C",0);
        dataBinding.setActivity(this);
        limits=String.valueOf(selfRole);
        if(limits.contains("1")){//群主
            dataBinding.ivEdit.setVisibility(View.VISIBLE);
        }else {
            dataBinding.ivEdit.setVisibility(View.GONE);
        }
        EventBus.getDefault().register(this);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new GroupCarAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getGroupCarListVehicle(groupId);
    }
    @Subscribe
    public void onGroupManageEvent(GroupManageEvent mGroupManageEvent) {
        int type= mGroupManageEvent.getType();
        viewModel.getGroupCarListVehicle(groupId);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.editLayout:
                showCarEditMenu();
                break;
            case R.id.all_CheckBox:
               //全选车辆
                dataBinding.ivCheckBox.setSelected(!dataBinding.ivCheckBox.isSelected());
                adapter.selectAll(dataBinding.ivCheckBox.isSelected());
                break;
            case R.id.tv_remove:
                //移除选中车辆
                showHintDialog(2);
                break;
            default:
                break;
        }
    }
    private CarEditPopupWindow carEditMenu=null;
    private void showCarEditMenu(){
        if(carEditMenu==null){
            carEditMenu=new CarEditPopupWindow(this);
            carEditMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.tv_add_car){
                        //添加车辆
                        Intent intent = new Intent(GroupCarManageActivity.this, AddCarActivity.class);
                        intent.putExtra("A",groupId);
                        startActivity(intent);
                    }else if(v.getId()==R.id.tv_remove_car){
                        //多选车辆
                        if(adapter.getItemCount()>0){
                            adapter.isMultiEdit=true;
                            dataBinding.bottomLayout.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }

                    }
                    carEditMenu.dismiss();
                }
            });
        }
        carEditMenu.showAsDropDown(dataBinding.ivEdit,0,-getResources().getDimensionPixelOffset(R.dimen.dp_20));
    }
    private ItemCarEditPopupWindow itemCarEditMenu=null;
    private GroupMemberBean editCarBean=null;
    private void showItemCarEditMenu(View view,GroupMemberBean mCarBean){
        editCarBean=mCarBean;
        if(itemCarEditMenu==null){
            itemCarEditMenu=new ItemCarEditPopupWindow(this);
            itemCarEditMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.tv_change_driver){
                        mSetLicencePlate = "";
//                        if (editCarBean.getUserId() != 0){
                            //更换司机
                            Intent intent = new Intent(GroupCarManageActivity.this, TransferVehicleActivity.class);
                            intent.putExtra("A",groupId);
                            intent.putExtra("B",editCarBean.getUserId());
                            intent.putExtra("C",0); //0自己更换司机 1转让车辆
                            intent.putExtra("D",editCarBean.getLicencePlate());
                            startActivity(intent);
//                        } else {
//                            mSetLicencePlate = editCarBean.getLicencePlate();
//                            //选择司机
//                            Intent intent = new Intent(GroupCarManageActivity.this, SelectDriverActivity.class);
//                            intent.putExtra("A", groupId);
//                            startActivity(intent);
//                        }
                    }else if(v.getId()==R.id.tv_remove_car){
                        //单选移除车辆
                        showHintDialog(1);
                    }
                    itemCarEditMenu.dismiss();
                }
            });
        }
        itemCarEditMenu.showAsDropDown(view);
    }
    private TextTitleDialog hintDialog;
    private int dialogType=0;//1 表示单个移除 2表示多个移除
    private void showHintDialog(int dialogType){
        this.dialogType=dialogType;
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    if(GroupCarManageActivity.this.dialogType==1){
                        viewModel.delDriver(groupId,""+editCarBean.getUserId());
                    }else if(GroupCarManageActivity.this.dialogType==2){
                        String driverIds=adapter.getSelectDriverIds();
                        viewModel.delDriver(groupId,""+driverIds);
                    }
                }
            });
        }
        hintDialog.setTitle("确定移除车辆？");
        hintDialog.show();;
    }
    @Override
    public void onItemClick(View view, int position) {
        Object obj=adapter.getItem(position);
        switch (view.getId()) {
            case R.id.iv_edit:
                //编辑车辆
                if(obj instanceof GroupMemberBean){
                    GroupMemberBean mCarBean= (GroupMemberBean) obj;
                    if(adapter.isMultiEdit){
                        mCarBean.setSelect(!mCarBean.isSelect());
                        adapter.notifyItemChanged(position);
                    }else {
                        showItemCarEditMenu(view,mCarBean);
                    }
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


    private String mSetLicencePlate = "";

    @Subscribe
    public void onSelectDriver(GroupMemberBean driverBean) {
        if (!TextUtils.isEmpty(mSetLicencePlate)){
            viewModel.addDriver(groupId,mSetLicencePlate,driverBean.getUserId());
        }
    }
}
