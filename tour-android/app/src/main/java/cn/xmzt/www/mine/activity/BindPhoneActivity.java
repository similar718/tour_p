package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityBindPhoneBinding;
import cn.xmzt.www.mine.model.BindPhoneViewModel;

/**
 * 绑定手机号界面
 */
public class BindPhoneActivity extends TourBaseActivity<BindPhoneViewModel, ActivityBindPhoneBinding> {
    private String tag;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected BindPhoneViewModel createViewModel() {
        return new BindPhoneViewModel();
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
