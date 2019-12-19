package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityWxBindPhoneBinding;
import cn.xmzt.www.mine.bean.WxRegisterBean;
import cn.xmzt.www.mine.model.WxBindPhoneViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/30
 * @describe 微信登录强制绑定手机号
 */

public class WxBindPhoneActivity extends TourBaseActivity<WxBindPhoneViewModel, ActivityWxBindPhoneBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_wx_bind_phone;
    }

    @Override
    protected WxBindPhoneViewModel createViewModel() {
        return new WxBindPhoneViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getData(WxRegisterBean bean){
        viewModel.setBean(bean);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
