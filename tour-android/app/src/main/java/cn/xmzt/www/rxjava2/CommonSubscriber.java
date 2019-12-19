package cn.xmzt.www.rxjava2;

import com.blankj.utilcode.util.NetworkUtils;
import cn.xmzt.www.base.IView;

import java.net.ConnectException;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 统一处理异常信息(用於FlowableSubscriber)
 *
 * @param <T>
 */
public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private IView mView;

    public CommonSubscriber(IView mView) {
        this.mView = mView;
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof QzdsException) {
            QzdsException q = (QzdsException) throwable;
            if(mView!=null){
                mView.showLoadFail(throwable.getMessage());
            }

        } else if (throwable instanceof ConnectException) {
            if (!NetworkUtils.isConnected()) {
                if(mView!=null){
                    mView.showLoadFail("网络异常");
                }
            } else {
                if(mView!=null){
                    mView.showLoadFail("服务器异常");
                }
            }
        } else {
            if(mView!=null){
                mView.showLoadFail(throwable.getMessage());
            }
        }
    }

    @Override
    public void onComplete() {

    }
}
