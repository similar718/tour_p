package cn.xmzt.www.mine.model;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.SignInRuleExplainDialog;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.SignInActivity;
import cn.xmzt.www.mine.adapter.SignInAdapter1;
import cn.xmzt.www.mine.bean.SignDayBean;
import cn.xmzt.www.mine.bean.SignInBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/23
 * @describe
 */

public class SignInViewModel extends BaseViewModel {
    private SignInActivity activity;
    private List<SignDayBean> list = new ArrayList<>();
    private SignInBean rel;
    private SignInAdapter1 adapter;

    public void setActivity(SignInActivity activity) {
        this.activity = activity;
        getSignInInfo();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.tv_sign://签到
                goSignIn();
                break;
            case R.id.tv_rule://奖励规则
                SignInRuleExplainDialog dialog = new SignInRuleExplainDialog(activity);
                dialog.show();
                break;
        }
    }

    /**
     * 去签到
     */
    private void goSignIn() {
        ApiRepertory.getInstance().getApiService().signIn(TourApplication.getToken())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> objectBaseDataBean) {
                if (objectBaseDataBean.isSuccess()) {
                    getSignInInfo();
                } else {
                    ToastUtils.showText(activity, objectBaseDataBean.getReMsg());
                }
            }
        });
    }

    /**
     * 获取用户签到信息
     */
    private void getSignInInfo() {
        ApiRepertory.getInstance().getApiService().getSignInInfo(TourApplication.getToken())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<SignInBean>>() {
            @Override
            public void onNext(BaseDataBean<SignInBean> objectBaseDataBean) {
                if (null != objectBaseDataBean) {
                    if (objectBaseDataBean.isSuccess()) {
                        rel = objectBaseDataBean.getRel();

                    } else {
                        ToastUtils.showText(activity, objectBaseDataBean.getReMsg());
                    }
                }
            }
        });
    }
}
