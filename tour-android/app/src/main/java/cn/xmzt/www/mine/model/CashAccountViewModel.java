package cn.xmzt.www.mine.model;

import android.content.Intent;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.BindALiPayActivity;
import cn.xmzt.www.mine.activity.CashAccountActivity;
import cn.xmzt.www.mine.bean.CashWithdrawalBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe
 */

public class CashAccountViewModel extends BaseViewModel {
    private CashAccountActivity activity;
    private CashWithdrawalBean bean;

    public void setActivity(CashAccountActivity activity) {
        this.activity = activity;
        getCashAccount();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.tv_bind://绑定或修改支付宝
                if (bean.getSysUserExtractionAccount().size() > 0) {//说明已经绑定过了支付宝
                    EventBus.getDefault().postSticky(bean.getSysUserExtractionAccount().get(0));
                } else {//未绑定过支付宝
                    EventBus.getDefault().postSticky(new CashWithdrawalBean.SysUserExtractionAccountEntity());
                }
                activity.startActivity(new Intent(activity, BindALiPayActivity.class));
                break;
        }
    }

    /**
     * 获取当前用户的所有提现账号以及提现次数和金额
     */
    public void getCashAccount() {
        ApiRepertory.getInstance().getApiService().getCashAccount(TourApplication.getToken())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<CashWithdrawalBean>>() {
            @Override
            public void onNext(BaseDataBean<CashWithdrawalBean> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        bean = userInfoBean.getRel();
                        if (userInfoBean.getRel().getSysUserExtractionAccount().size() > 0) {//绑定了账号
                            activity.dataBinding.tvName.setText(userInfoBean.getRel().getSysUserExtractionAccount().get(0).getAccount());
                            activity.dataBinding.tvBind.setText("更改账号");
                        } else {//未绑定账号
                            activity.dataBinding.tvName.setText("绑定支付宝");
                            activity.dataBinding.tvBind.setText("绑定账号");
                        }
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }
}
