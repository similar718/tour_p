package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityVerificationPhoneBinding;
import cn.xmzt.www.mine.model.VerificationPhoneViewModel;

/**
 * 验证手机号码
 */

public class VerificationPhoneActivity extends TourBaseActivity<VerificationPhoneViewModel,ActivityVerificationPhoneBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_verification_phone;
    }

    @Override
    protected VerificationPhoneViewModel createViewModel() {
        return new VerificationPhoneViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
