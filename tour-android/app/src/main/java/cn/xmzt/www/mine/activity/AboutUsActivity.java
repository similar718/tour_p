package cn.xmzt.www.mine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.xmzt.www.R;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.mine.bean.AppVersionBean;
import cn.xmzt.www.nim.uikit.common.media.imagepicker.view.SystemBarTintManager;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.APKVersionCodeUtils;
import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * @author tanlei
 * @date 2019/8/28
 * @describe 关于我们
 */

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    private String versionName="";
    private TextView tv_version_update;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
//            transparencyBar(activity);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.white);
        }
        setContentView(R.layout.activity_about_us);
        TextView tv = findViewById(R.id.tv);
        ImageView imageView = findViewById(R.id.iv_log);
        tv_version_update= findViewById(R.id.tv_version_update);
        GlideUtil.loadImgRadius(imageView,20,R.drawable.icon_logo);
        versionName=APKVersionCodeUtils.getVerName(getApplicationContext());
        tv.setText("版本V" + versionName);
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        tv_version_update.setOnClickListener(this);
        getLatestVersion();
    }
    private AppVersionBean mAppVersionBean;
    /**
     *获取当前服务器App最新版本
     */
    public void getLatestVersion() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getLatestVersion(1,1);
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<AppVersionBean>>() {
                    @Override
                    public void onNext(BaseDataBean<AppVersionBean> body) {
                        if(body.isSuccess()){
                            mAppVersionBean=body.getRel();
                            if(mAppVersionBean!=null&&!versionName.equals(mAppVersionBean.getVersionReplaceV())){
                                tv_version_update.setVisibility(View.VISIBLE);
                            }else {
                                tv_version_update.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
    private void downLoad(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            ToastUtils.showText(this,"新版本下载失败");
        }
    }
    @Override
    public void onClick(View v) {
        if(R.id.title_back_iv==v.getId()){
            onBackPressed();
        }else  if(R.id.tv_version_update==v.getId()){
            if(mAppVersionBean!=null){
                downLoad(mAppVersionBean.getUrl());
            }
        }
    }
}
