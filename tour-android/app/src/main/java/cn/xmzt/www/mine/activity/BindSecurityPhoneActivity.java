package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityBindSecurityPhoneBinding;
import cn.xmzt.www.mine.model.BindSecurityPhoneViewModel;

/**
 * 绑定安全手机号界面
 */
public class BindSecurityPhoneActivity extends TourBaseActivity<BindSecurityPhoneViewModel, ActivityBindSecurityPhoneBinding> {
    private String tag;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bind_security_phone;
    }

    @Override
    protected BindSecurityPhoneViewModel createViewModel() {
        return new BindSecurityPhoneViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        if (null != getIntent()) {
            tag = getIntent().getStringExtra("tag");
        }
        viewModel.setTag(tag);
    }
}
