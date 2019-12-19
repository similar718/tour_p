package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCashAccountBinding;
import cn.xmzt.www.mine.event.ALiPayEvent;
import cn.xmzt.www.mine.model.CashAccountViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe 提现账号界面
 */

public class CashAccountActivity extends TourBaseActivity<CashAccountViewModel,ActivityCashAccountBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_cash_account;
    }

    @Override
    protected CashAccountViewModel createViewModel() {
        return new CashAccountViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(ALiPayEvent event){
        viewModel.getCashAccount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
