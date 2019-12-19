package cn.xmzt.www.intercom.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityTransferVehicleBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.adapter.SelectDriverAdapter;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.TransferVehicleViewModel;
import cn.xmzt.www.view.ScrollGridLayoutManager;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @describe 转让车辆、更换司机
 */
public class TransferVehicleActivity extends TourBaseActivity<TransferVehicleViewModel, ActivityTransferVehicleBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_transfer_vehicle;
    }

    @Override
    protected TransferVehicleViewModel createViewModel() {
        viewModel = new TransferVehicleViewModel();
        viewModel.selfGroupMemberBean.observe(this, new Observer<GroupMemberBean>() {
            @Override
            public void onChanged(@Nullable GroupMemberBean result) {
                if(type==1){//转让车辆给你个司机
                    driverUserId=result.getUserId();
                    licencePlate=result.getLicencePlate();
                }
            }
        });
        viewModel.result.observe(this, new Observer<BaseDataBean<Object>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<Object> result) {
                EventBus.getDefault().post(new GroupManageEvent(3));//提示上一个页面刷新
                finish();
            }
        });
        viewModel.driverList.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(@Nullable List<GroupMemberBean> result) {
                if(result!=null&&result.size()>0){
                    adapter.setDatas(result);
                }
            }
        });
        return viewModel;
    }
    SelectDriverAdapter adapter =null;
    ScrollGridLayoutManager manager=null;
    private int driverUserId;//  当前车辆用户id
    private int type=0;//0自己更换司机 1转让车辆
    private String licencePlate="";//当前车辆用户的车牌号
    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("A");
        driverUserId=intent.getIntExtra("B",0);
        type=intent.getIntExtra("C",0);
        licencePlate=intent.getStringExtra("D");
        dataBinding.setActivity(this);
        manager=new ScrollGridLayoutManager(this, 1, GridLayoutManager.VERTICAL,false);
        dataBinding.recyclerView.setLayoutManager(manager);
        adapter = new SelectDriverAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        if(type==1&&TextUtils.isEmpty(licencePlate)){
            viewModel.getGroupUser();
        }
        viewModel.getChooseDriverList();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_confirm:{
                //选择确认
                showHintDialog();
                break;
            }
            default:
                break;
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        if(view.getId()==R.id.itemLayout){
            if(position!=adapter.selectPosition){
                int temp=adapter.selectPosition;
                adapter.selectPosition=position;
                GroupMemberBean driverBean=adapter.getItem(position);
                driverBean.setSelect(true);
                if(temp>=0){
                    GroupMemberBean selectDriverBean=adapter.getItem(temp);
                    selectDriverBean.setSelect(false);
                    adapter.notifyItemChanged(temp);
                }
                adapter.notifyItemChanged(position);
            }
        }
    }
    private TextTitleDialog hintDialog;
    private void showHintDialog(){
        GroupMemberBean groupDriverBean=adapter.getSelectItem();
        if(groupDriverBean==null){
            if(type==0){
                ToastUtils.showShort("请选择要更换的司机");
            }else if(this.type==1){
                ToastUtils.showShort("请选择要转让车辆给哪个司机");
            }
            return;
        }
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    viewModel.updtDriverUser(driverUserId,viewModel.groupId,licencePlate,groupDriverBean.getUserId());
                }
            });
        }
        //0自己更换司机 1转让车辆
        if(type==0){
            hintDialog.setTitle("确定要更换司机？");
        }else if(this.type==1){
            hintDialog.setTitle("确定要转让车辆？");
        }
        hintDialog.show();;
    }
}
