package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMyCouponBinding;
import cn.xmzt.www.mine.model.MyCouponViewModel;

/**
 * 我的优惠券
 */

public class MyCouponActivity extends TourBaseActivity<MyCouponViewModel, ActivityMyCouponBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_coupon;
    }

    @Override
    protected MyCouponViewModel createViewModel() {
        return new MyCouponViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
