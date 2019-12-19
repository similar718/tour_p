package cn.xmzt.www.home.activity;


import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCustomizeSucceedBinding;

/**
 * 私人定制定制成功
 */
public class CustomizeSucceedActivity extends TourBaseActivity<BaseViewModel, ActivityCustomizeSucceedBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_customize_succeed;
    }
    @Override
    protected BaseViewModel createViewModel() {
        viewModel = new BaseViewModel();
        return viewModel ;
    }
    @Override
    protected void initData() {
        dataBinding.setActivity(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}