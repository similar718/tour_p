package cn.xmzt.www.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import cn.xmzt.www.R;
import cn.xmzt.www.home.activity.CustomizeEditActivity;
import cn.xmzt.www.home.activity.PopularityMustPlayActivity;
import cn.xmzt.www.mine.activity.MessageCenterActivity;
import cn.xmzt.www.mine.activity.SignInActivity;
import cn.xmzt.www.nim.uikit.common.media.imagepicker.view.SystemBarTintManager;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.route.activity.RouteOrderDetailActivity;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotDetailActivity;
import cn.xmzt.www.service.LocationService;
import cn.xmzt.www.smartteam.activity.TripSignInActivity;
import cn.xmzt.www.smartteam.activity.TripSignInListActivity;
import cn.xmzt.www.utils.WriteToSdUtils;

import com.blankj.utilcode.util.BarUtils;
import com.umeng.socialize.UMShareAPI;


/**
 * @author tanlei
 * @date 2019/7/27
 * @describe
 */

public abstract class TourBaseActivity<VM extends BaseViewModel, DB extends ViewDataBinding> extends BaseActivity {

    protected VM viewModel;
    public DB dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, setLayoutId());
        if (viewModel == null) {
            viewModel = createViewModel();
        }
        viewModel.setIView(this);
//        PushAgent.getInstance(this).onAppStart();
        if (this instanceof RouteDetailActivity1 || this instanceof RouteOrderDetailActivity
                || this instanceof ScenicSpotDetailActivity || this instanceof PopularityMustPlayActivity
                || this instanceof SignInActivity||this instanceof MessageCenterActivity || this instanceof CustomizeEditActivity
                || this instanceof TripSignInActivity || this instanceof TripSignInListActivity) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));
            }else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this,R.color.color_88_49_BE_FF));

                View decor = window.getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
//            transparencyBar(activity);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.color_88_49_BE_FF);
            }
        }

        initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // 获取到的权限设置问题 如果获取到了位置权限 需要开启定位服务
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 有写入数据库的权限
            // 将Assets里面的自定义地图写入到sd卡中 方便在其他界面获取数据
            this.getApplicationContext().startService(new Intent(this, LocationService.class));
        }
        // 如果获取到了写入权限 需要将自定义的地图写入到sd卡中
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 有写入数据库的权限
            // 将Assets里面的自定义地图写入到sd卡中 方便在其他界面获取数据
            WriteToSdUtils.setAssetsDataToSd(this.getApplicationContext());
        }
    }

    public void requestPermissionActivity(){
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    protected abstract int setLayoutId();

    protected abstract VM createViewModel();

    protected abstract void initData();
}
