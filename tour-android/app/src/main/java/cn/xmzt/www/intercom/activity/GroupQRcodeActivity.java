package cn.xmzt.www.intercom.activity;

import android.content.Intent;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGroupQrCodeBinding;
import cn.xmzt.www.intercom.bean.TourTripBean;
import cn.xmzt.www.intercom.vm.GroupQRcodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author tanlei
 * @date 2019/9/6
 * @describe 群组二维码界面
 */

public class GroupQRcodeActivity extends TourBaseActivity<GroupQRcodeViewModel, ActivityGroupQrCodeBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_qr_code;
    }

    @Override
    protected GroupQRcodeViewModel createViewModel() {
        return new GroupQRcodeViewModel();
    }

    private String name;
    private String tripId;//行程id
    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        tripId=intent.getStringExtra("teamId");
        if(tripId==null){
            if(mTourTripBean!=null){
                tripId=mTourTripBean.getId()+"";
            }
        }
        dataBinding.tvName.setText(name);
        viewModel.setTeamId(tripId);
    }
    private TourTripBean mTourTripBean;
    @Subscribe(sticky = true)
    public void onTourTripBean(TourTripBean mTourTripBean) {
        this.mTourTripBean=mTourTripBean;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
