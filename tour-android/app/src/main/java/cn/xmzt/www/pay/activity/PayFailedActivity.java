package cn.xmzt.www.pay.activity;


import android.content.Intent;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityPayFailedBinding;
import cn.xmzt.www.utils.IsFastClick;

/**
 * 选择支付方式
 */
public class PayFailedActivity extends TourBaseActivity<BaseViewModel, ActivityPayFailedBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_pay_failed;
    }
    @Override
    protected BaseViewModel createViewModel() {
        viewModel = new BaseViewModel();
        return viewModel ;
    }
    private String orderCode;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        orderCode=intent.getStringExtra("A");
        dataBinding.setActivity(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_retry:
                //点击重试
               if(IsFastClick.isFastClick()){
                   finish();
               }
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}