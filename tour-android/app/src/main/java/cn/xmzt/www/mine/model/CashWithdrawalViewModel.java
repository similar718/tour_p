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
import cn.xmzt.www.mine.activity.CashWithdrawalActivity;
import cn.xmzt.www.mine.bean.CashWithdrawalBean;
import cn.xmzt.www.mine.bean.UserWalletBean;
import cn.xmzt.www.mine.event.CashSuccessEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe
 */

public class CashWithdrawalViewModel extends BaseViewModel {
    private CashWithdrawalActivity activity;
    private CashWithdrawalBean bean;
    private UserWalletBean userWalletBean;

    public void setUserWalletBean(UserWalletBean userWalletBean) {
        this.userWalletBean = userWalletBean;
    }

    public void setActivity(CashWithdrawalActivity activity) {
        this.activity = activity;
        getCashAccount();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.crash_account_tv://提现账户管理
                activity.startActivity(new Intent(activity, CashAccountActivity.class));
                break;
            case R.id.tv_all://全部提现
                if (userWalletBean.getEnableNum() > 0) {
                    activity.dataBinding.cashEt.setText(userWalletBean.getEnableNum() + "");
                }
                break;
            case R.id.cash_tv://提现
                if (this.bean.getSysUserExtractionAccount().size() > 0) {//已经绑定支付宝账号(提现)
                    if (activity.dataBinding.cashEt.getText().toString().equals("")) {
                        ToastUtils.showText(activity, "提现金额不能为空");
                    } else {
                        cash();
                    }
                } else {//未绑定支付宝账号(去绑定支付宝账号)
                    EventBus.getDefault().postSticky(new CashWithdrawalBean.SysUserExtractionAccountEntity());
                    activity.startActivity(new Intent(activity, BindALiPayActivity.class));
                }
                break;
        }
    }

    /**
     * 提现
     */
    private void cash() {
        ApiRepertory.getInstance().getApiService().cash(TourApplication.getToken(), this.bean.getSysUserExtractionAccount().get(0).getId(),
                Double.valueOf(activity.dataBinding.cashEt.getText().toString()))
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        ToastUtils.showText(activity,"您已提交提现申请，将在1-5个工作日到账");
                        EventBus.getDefault().post(new CashSuccessEvent());
                        activity.finish();
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
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
                        activity.dataBinding.tvDetails.setText("最低提现" + userInfoBean.getRel().getExtractionMinnumber() + "元，" +
                                "1天最多可提现" + userInfoBean.getRel().getExtractionCount() + "次");
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }
}
