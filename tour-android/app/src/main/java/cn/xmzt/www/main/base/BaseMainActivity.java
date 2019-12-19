package cn.xmzt.www.main.base;

import android.Manifest;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.IView;
import cn.xmzt.www.dialog.LoadingDialig;
import cn.xmzt.www.nim.uikit.common.activity.UI;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Averysk
 */
public abstract class BaseMainActivity<VM extends BaseViewModel, DB extends ViewDataBinding> extends UI implements IView {

    protected VM viewModel;
    public DB dataBinding;

    public static List<BaseMainActivity> list = new ArrayList<>();
    public LoadingDialig mLoadingDialig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(this);
        dataBinding = DataBindingUtil.setContentView(this, setLayoutId());
        if (viewModel == null) {
            viewModel = createViewModel();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        viewModel.setIView(this);
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.remove(this);
    }

    protected abstract int setLayoutId();

    protected abstract VM createViewModel();

    protected abstract void initData();

    @Override
    public void showLoading() {
        if (mLoadingDialig == null) {
            mLoadingDialig = new LoadingDialig(this, "正在加载");
        }
        mLoadingDialig.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialig != null) {
            mLoadingDialig.dismiss();
        }
    }

    @Override
    public void showLoadFail(String msg) {
        ToastUtils.showShort(msg);
    }

}
