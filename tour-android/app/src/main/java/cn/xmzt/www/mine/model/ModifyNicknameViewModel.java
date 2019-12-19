package cn.xmzt.www.mine.model;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.ModifyNicknameActivity;
import cn.xmzt.www.mine.event.NicknameEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author tanlei
 * @date 2019/8/7
 * @describe
 */

public class ModifyNicknameViewModel extends BaseViewModel {
    public ModifyNicknameActivity activity;
    /**
     * 昵称
     */
    public ObservableField<String> nickName = new ObservableField();
    /**
     * 是否可以清空昵称
     */
    public ObservableBoolean isDelete = new ObservableBoolean(true);

    public void setActivity(ModifyNicknameActivity activity) {
        this.activity = activity;
    }

    public void setNickName(NicknameEvent nicknameEvent) {
        nickName.set(nicknameEvent.getNickName());
    }

    /**
     * 昵称输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onNickNameChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() > 0) {
            isDelete.set(true);
        } else {
            isDelete.set(false);
        }
        nickName.set(s.toString());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_tv://保存
                ApiRepertory.getInstance().getApiService().updateUsername(TourApplication.getToken(), activity.dataBinding.nicknameEt.getText().toString())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                    @Override
                    public void onNext(BaseDataBean<Object> objectBaseDataBean) {
                        if (objectBaseDataBean.isSuccess()) {
                            ToastUtils.showText(activity,"修改昵称成功");
                            nickName.set(activity.dataBinding.nicknameEt.getText().toString());
                            EventBus.getDefault().postSticky(new NicknameEvent(nickName.get()));
                            activity.finish();
                        }
                    }
                });
                break;
            case R.id.delete_iv://清空用户名
                nickName.set("");
                isDelete.set(false);
                break;
            case R.id.title_back_iv:
                activity.finish();
                break;
            default:
                break;
        }
    }
}
