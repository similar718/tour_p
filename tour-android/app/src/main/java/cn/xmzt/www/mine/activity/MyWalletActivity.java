package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMyWalletBinding;
import cn.xmzt.www.mine.event.CashSuccessEvent;
import cn.xmzt.www.mine.model.MyWalletViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe 我的钱包
 */

public class MyWalletActivity extends TourBaseActivity<MyWalletViewModel, ActivityMyWalletBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected MyWalletViewModel createViewModel() {
        return new MyWalletViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        viewModel.getMoneySum();
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cashSuccess(CashSuccessEvent event){
        viewModel.getMoneySum();
        viewModel.getMoneyDetails();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
