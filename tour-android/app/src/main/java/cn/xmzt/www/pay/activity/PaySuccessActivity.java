package cn.xmzt.www.pay.activity;


import android.content.Intent;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityPaySuccessBinding;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.route.activity.RouteOrderDetailActivity;

/**
 * 选择支付方式
 */
public class PaySuccessActivity extends TourBaseActivity<BaseViewModel, ActivityPaySuccessBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_pay_success;
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
            case R.id.tv_back_home:{
                //返回首页
                Intent intent = new Intent();
                intent.setClass(PaySuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.tv_order_info:{
                //订单详情
                Intent intent=new Intent(PaySuccessActivity.this, RouteOrderDetailActivity.class);
                intent.putExtra("A",orderCode);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish(); // 成功之后finish掉
                break;
            }
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