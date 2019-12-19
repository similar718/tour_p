package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityBindAliPayBinding;
import cn.xmzt.www.mine.bean.CashWithdrawalBean;
import cn.xmzt.www.mine.model.BindALiPayViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe 绑定支付宝界面
 */

public class BindALiPayActivity extends TourBaseActivity<BindALiPayViewModel, ActivityBindAliPayBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_bind_ali_pay;
    }

    @Override
    protected BindALiPayViewModel createViewModel() {
        return new BindALiPayViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getBindInfo(CashWithdrawalBean.SysUserExtractionAccountEntity entity) {
        viewModel.setEntity(entity) ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
