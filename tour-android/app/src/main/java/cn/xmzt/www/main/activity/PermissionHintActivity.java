package cn.xmzt.www.main.activity;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityPermissionHintBinding;
import cn.xmzt.www.main.vm.PermissionHintViewModel;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;

/**
 * @describe 权限同意见面
 */
public class PermissionHintActivity extends TourBaseActivity<PermissionHintViewModel, ActivityPermissionHintBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_permission_hint;
    }

    @Override
    protected PermissionHintViewModel createViewModel() {
        viewModel = new PermissionHintViewModel();
        viewModel.setIView(this);
        return viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.setActivity(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarLightMode(this,true);

        dataBinding.tvThink.setOnClickListener(new View.OnClickListener() { // 我在想想
            @Override
            public void onClick(View v) {
                // 这个是弹出系统的权限申请请求 界面跳转并且关闭当前界面
                SPUtils.putBoolean("isPermission",true); // 权限已经ok了
                goNext();
            }
        });

        dataBinding.dialogAgreeIv.setOnClickListener(new View.OnClickListener() { // 同意
            @Override
            public void onClick(View v) {
                SPUtils.putBoolean("isPermission",true); // 权限已经ok了
                AndPermission.with(PermissionHintActivity.this).runtime().permission(
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.RECORD_AUDIO,
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION
                ).onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        // 弹出我们批量申请的dialog 界面跳转并且关闭当前界面
                        goNext();
                    }
                }).onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        goNext();
                    }
                }).start();
            }
        });
    }

    private void goNext(){
        if (!SPUtils.getBoolean("isFirst", false)) {
            startActivity(new Intent(PermissionHintActivity.this, GuideActivity.class));
            finish();
        } else {
            startActivity(new Intent(PermissionHintActivity.this, MainActivity.class));
            finish();
        }
    }
}
