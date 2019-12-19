package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySettingPasswordBinding;
import cn.xmzt.www.mine.model.SettingPassWordViewModel;

/**
 * @author tanlei
 * @date 2019/9/16
 * @describe 设置登录密码
 */

public class SettingPassWordActivity extends TourBaseActivity<SettingPassWordViewModel,ActivitySettingPasswordBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting_password;
    }

    @Override
    protected SettingPassWordViewModel createViewModel() {
        return new SettingPassWordViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
