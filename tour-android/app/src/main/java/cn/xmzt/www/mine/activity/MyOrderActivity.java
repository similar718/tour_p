package cn.xmzt.www.mine.activity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMyOrderBinding;
import cn.xmzt.www.mine.event.OrderTypeEvent;
import cn.xmzt.www.mine.model.MyOrderViewModel;
import cn.xmzt.www.route.eventbus.RefreshEvent;

/**
 * 我的订单
 */

public class MyOrderActivity extends TourBaseActivity<MyOrderViewModel, ActivityMyOrderBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected MyOrderViewModel createViewModel() {
        return new MyOrderViewModel();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(OrderTypeEvent event) {
        viewModel.setOrderType(event.getType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshOrder(RefreshEvent event) {
        viewModel.refreshOrder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
