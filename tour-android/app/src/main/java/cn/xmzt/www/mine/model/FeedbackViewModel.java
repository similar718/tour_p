package cn.xmzt.www.mine.model;

import androidx.databinding.ObservableBoolean;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.FeedbackActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

/**
 * @author tanlei
 * @date 2019/8/28
 * @describe
 */

public class FeedbackViewModel extends BaseViewModel {
    private FeedbackActivity activity;
    public ObservableBoolean is = new ObservableBoolean();

    public void setActivity(FeedbackActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.commit_tv://提交意见反馈
                ApiRepertory.getInstance().getApiService().saveFeedback(activity.dataBinding.et.getText().toString(), TourApplication.getToken(), 1)
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                ToastUtils.showText(activity,"提交反馈成功");
                                activity.finish();
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }
                    }
                });
                break;
        }
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0) {
            is.set(true);
        } else {
            is.set(false);
        }
        activity.dataBinding.tvNumber.setText(400 - charSequence.toString().length() + "");
    }
}
