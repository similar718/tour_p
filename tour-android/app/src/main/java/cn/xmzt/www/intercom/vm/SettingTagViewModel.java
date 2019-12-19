package cn.xmzt.www.intercom.vm;

import android.content.Intent;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.intercom.activity.SettingTagActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import static android.app.Activity.RESULT_OK;

/**
 * @author tanlei
 * @date 2019/9/7
 * @describe
 */

public class SettingTagViewModel extends BaseViewModel {
    private SettingTagActivity activity;
    private String userId;
    private String groupId;

    public void setUserId(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public void setActivity(SettingTagActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel://取消
                activity.finish();
                break;
            case R.id.tv_save://保存
                updtGroupMemberRemark();
                break;
        }
    }

    private void updtGroupMemberRemark() {
        ApiRepertory.getInstance().getApiService().updtGroupMemberRemark(TourApplication.getToken(),
                groupId,activity.dataBinding.et.getText().toString(), userId)
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
            @Override
            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        Intent intent = new Intent();
                        intent.putExtra("content",activity.dataBinding.et.getText().toString());
                        activity.setResult(RESULT_OK,intent);
                        activity.finish();
                    }
                }
            }
        });
    }
}
