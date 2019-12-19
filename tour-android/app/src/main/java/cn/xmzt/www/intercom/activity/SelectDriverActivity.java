package cn.xmzt.www.intercom.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySelectDriverBinding;
import cn.xmzt.www.intercom.adapter.SelectDriverAdapter;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.vm.SelectGroupDriverViewModel;
import cn.xmzt.www.view.ScrollGridLayoutManager;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @describe 选择司机
 */
public class SelectDriverActivity extends TourBaseActivity<SelectGroupDriverViewModel, ActivitySelectDriverBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_driver;
    }

    @Override
    protected SelectGroupDriverViewModel createViewModel() {
        viewModel = new SelectGroupDriverViewModel();
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
    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("A");
        dataBinding.setActivity(this);
        manager=new ScrollGridLayoutManager(this, 1, GridLayoutManager.VERTICAL,false);
        dataBinding.recyclerView.setLayoutManager(manager);
        adapter = new SelectDriverAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getChooseDriverList();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_confirm:{
                //选择确认
                GroupMemberBean groupDriverBean=adapter.getSelectItem();
                if(groupDriverBean!=null){
                    EventBus.getDefault().post(groupDriverBean);
                    finish();
                }else {
                    ToastUtils.showShort("请先选择司机");
                }
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
}
