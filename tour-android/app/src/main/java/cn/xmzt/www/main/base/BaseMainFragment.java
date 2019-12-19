package cn.xmzt.www.main.base;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.IView;
import cn.xmzt.www.dialog.LoadingDialig;
import cn.xmzt.www.nim.uikit.common.fragment.TFragment;

/**
 * @author  Averysk
 */
public abstract class BaseMainFragment<VM extends BaseViewModel, DB extends ViewDataBinding> extends TFragment implements IView {

    public LoadingDialig mLoadingDialig;
    protected VM viewModel;
    public DB dataBinding;

    private boolean isInitData=false;

    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        if(dataBinding==null){
            return null;
        }
        return dataBinding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (viewModel == null) {
            viewModel = createViewModel();
            viewModel.setIView(this);
        }
        if(getUserVisibleHint()){
            isInitData=true;
            initData();
        }
    }
    protected abstract int getLayoutId();

    protected abstract VM createViewModel();
    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(dataBinding !=null && !isInitData && isVisibleToUser){
            Log.e("shiyong",getClass().getName()+":setUserVisibleHint isVisibleToUser="+isVisibleToUser);
            isInitData=true;
            initData();
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialig == null) {
            mLoadingDialig = new LoadingDialig(getActivity(), "正在加载");
        }
        mLoadingDialig.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialig != null){
            mLoadingDialig.dismiss();
        }
    }
    @Override
    public void showLoadFail(String msg) {
        hideLoading();
        if(!TextUtils.isEmpty(msg)){
            ToastUtils.showShort(msg);
        }
    }

}
