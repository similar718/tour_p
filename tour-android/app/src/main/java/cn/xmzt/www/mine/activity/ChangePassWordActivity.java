package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityChangePasswordBinding;
import cn.xmzt.www.mine.model.ChangePassWordViewModel;

/**
 * @author tanlei
 * @date 2019/8/10
 * @describe 修改密码界面
 */

public class ChangePassWordActivity extends TourBaseActivity<ChangePassWordViewModel, ActivityChangePasswordBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected ChangePassWordViewModel createViewModel() {
        return new ChangePassWordViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
