package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityRegisterBinding;
import cn.xmzt.www.mine.model.RegisterViewModel;

/**
 * @author tanlei
 * @date 2019/8/5
 * @describe 新的注册界面
 */

public class RegisterActivity extends TourBaseActivity<RegisterViewModel, ActivityRegisterBinding> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected RegisterViewModel createViewModel() {
        return new RegisterViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
