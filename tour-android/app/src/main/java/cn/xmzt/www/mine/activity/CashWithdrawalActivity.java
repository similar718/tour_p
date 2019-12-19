package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCashWithdrawalBinding;
import cn.xmzt.www.mine.bean.UserWalletBean;
import cn.xmzt.www.mine.event.ALiPayEvent;
import cn.xmzt.www.mine.model.CashWithdrawalViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe 提现界面
 */

public class CashWithdrawalActivity extends TourBaseActivity<CashWithdrawalViewModel, ActivityCashWithdrawalBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_cash_withdrawal;
    }

    @Override
    protected CashWithdrawalViewModel createViewModel() {
        return new CashWithdrawalViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(ALiPayEvent event) {
        viewModel.getCashAccount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getUserWalletBean(UserWalletBean bean) {
        dataBinding.tvMax.setText("本次可提现" + bean.getEnableNum() + "元");
        viewModel.setUserWalletBean(bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
