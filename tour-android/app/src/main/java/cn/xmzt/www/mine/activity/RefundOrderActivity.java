package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityRefundOrderBinding;
import cn.xmzt.www.mine.model.RefundOrderViewModel;

/**
 * @author tanlei
 * @date 2019/8/19
 * @describe 退款订单
 */

public class RefundOrderActivity extends TourBaseActivity<RefundOrderViewModel, ActivityRefundOrderBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_refund_order;
    }

    @Override
    protected RefundOrderViewModel createViewModel() {
        return new RefundOrderViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
