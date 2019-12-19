package cn.xmzt.www.mine.model;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.WebActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.dialog.AccountWrittenOffDialog;
import cn.xmzt.www.dialog.LogoffConfirmDialog;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.CloseAccountActivity;
import cn.xmzt.www.mine.activity.VerifyActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

/**
 * @author tanlei
 * @date 2019/8/10
 * @describe
 */

public class CloseAccountViewModel extends BaseViewModel {
    private CloseAccountActivity activity;
    private AccountWrittenOffDialog dialog;

    public void setActivity(CloseAccountActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.close_account_next_btn://下一步
                ApiRepertory.getInstance().getApiService().logoutCheck(TourApplication.getToken())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>() {
                    @Override
                    public void onNext(BaseDataBean<String> userInfoBean) {
                        if (null != userInfoBean) {
                            if (userInfoBean.isSuccess()) {
                                String rel=userInfoBean.getRel();
                                if (!TextUtils.isEmpty(rel)) {
                                    dialog = new AccountWrittenOffDialog(activity, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (view.getId() == R.id.confirm_tv) {
                                                activity.startActivity(new Intent(activity, VerifyActivity.class));
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.setViewData(rel);
                                    dialog.show();
                                } else {
                                    activity.startActivity(new Intent(activity, VerifyActivity.class));
                                }
                            } else {
                                ToastUtils.showText(activity, userInfoBean.getReMsg());
                            }
                        }
                    }
                });
                break;
            case R.id.tv_close://注销协议
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("title", "用户注销协议");
                intent.putExtra("url", Constants.getXzUrl(15));
                activity.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
