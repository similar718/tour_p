package cn.xmzt.www.base;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.dialog.LoadingDialig;

/**
 *  基类Fragment
 */
public abstract class BaseFragment<VM extends BaseViewModel, DB extends ViewDataBinding> extends Fragment implements IView {
    public LoadingDialig mLoadingDialig;
    protected VM viewModel;
    public DB dataBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean isInitData=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
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
            Log.e("shiyong","onActivityCreated"+this.getClass().getName());
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(dataBinding!=null&&!isInitData&&isVisibleToUser){
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
