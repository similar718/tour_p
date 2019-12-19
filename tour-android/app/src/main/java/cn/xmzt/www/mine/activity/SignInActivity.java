package cn.xmzt.www.mine.activity;

import android.widget.RelativeLayout;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySigninBinding;
import cn.xmzt.www.mine.model.SignInViewModel;
import cn.xmzt.www.utils.StatusBarUtil;

/**
 * @author tanlei
 * @date 2019/8/23
 * @describe 签到
 */

public class SignInActivity extends TourBaseActivity<SignInViewModel,ActivitySigninBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_signin;
    }

    @Override
    protected SignInViewModel createViewModel() {
        return new SignInViewModel();
    }

    @Override
    protected void initData() {
//        StatusBarUtil.setStatusBarLightMode(this,false);
        StatusBarUtil.setStatusBarLightMode(this,false);

        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        RelativeLayout.LayoutParams mLayoutParams= (RelativeLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
