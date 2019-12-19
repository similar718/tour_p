package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityFindPasswordBinding;
import cn.xmzt.www.mine.model.FindPasswordViewModel;

/**
 * @author tanlei
 * @date 2019/8/6
 * @describe 忘记密码界面
 */

public class FindPasswordActivity extends TourBaseActivity<FindPasswordViewModel, ActivityFindPasswordBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_find_password;
    }

    @Override
    protected FindPasswordViewModel createViewModel() {
        return new FindPasswordViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
