package cn.xmzt.www.mine.activity;

import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityPhoneSecurityLoseBinding;
import cn.xmzt.www.mine.model.PhoneSecurityLoseViewModel;

/**
 * @author tanlei
 * @date 2019/8/26
 * @describe 原手安全机号丢失或停用界面
 */

public class PhoneSecurityLoseActivity extends TourBaseActivity<PhoneSecurityLoseViewModel, ActivityPhoneSecurityLoseBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_phone_security_lose;
    }

    @Override
    protected PhoneSecurityLoseViewModel createViewModel() {
        return new PhoneSecurityLoseViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setActivity(this);
    }

    public void onClick(View view) {
        this.finish();
    }
}
