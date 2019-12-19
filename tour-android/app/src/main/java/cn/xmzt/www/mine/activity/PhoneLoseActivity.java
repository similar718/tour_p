package cn.xmzt.www.mine.activity;

import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityPhoneLoseBinding;
import cn.xmzt.www.mine.model.PhoneLoseViewModel;

/**
 * @author tanlei
 * @date 2019/8/26
 * @describe 原手机号丢失或停用界面
 */

public class PhoneLoseActivity extends TourBaseActivity<PhoneLoseViewModel, ActivityPhoneLoseBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_phone_lose;
    }

    @Override
    protected PhoneLoseViewModel createViewModel() {
        return new PhoneLoseViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setActivity(this);
    }

    public void onClick(View view) {
        this.finish();
    }
}
